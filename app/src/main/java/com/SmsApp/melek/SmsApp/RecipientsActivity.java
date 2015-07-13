package com.SmsApp.melek.SmsApp;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class RecipientsActivity extends ListActivity {

    private static final String TAG = RecipientsActivity.class.getSimpleName();
    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected Uri mMediaUri;
    protected String mFileType;
    protected String mMessage;
    private MenuItem mMenuItemSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_recipients);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mMediaUri = getIntent().getData();
        mFileType = getIntent().getStringExtra(ParseConstants.KEY_FILE_TYPE);

        mMessage = getIntent().getStringExtra(ParseConstants.DIFF_KEY_MESSAGE);
    }

    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);

        setProgressBarIndeterminateVisibility(true);
        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                setProgressBarIndeterminateVisibility(true);
                if (e == null) {
                    mFriends = friends;
                    int i = 0;
                    String[] usernames = new String[mFriends.size()];
                    for (ParseUser user : mFriends) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter adapter = new ArrayAdapter(getListView().getContext(), android.R.layout.simple_list_item_checked, usernames);
                    setListAdapter(adapter);
                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
                    builder.setTitle("error!");
                    builder.setMessage(e.getMessage());
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                setProgressBarIndeterminateVisibility(false);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipients, menu);
        mMenuItemSend = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_send) {
            ParseObject message = createMessage();
            if (message == null) {
                //error
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.dialog_title_fileerror))
                        .setMessage(getString(R.string.dialog_message_fileError))
                        .setPositiveButton(getString(R.string.dialog_buttonLabel_fileError), null);
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                send(message);
                finish();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void send(ParseObject message) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(RecipientsActivity.this, getString(R.string.sending_message_successfully), Toast.LENGTH_LONG).show();
                } else {
                    //error
                    AlertDialog.Builder builder = new AlertDialog.Builder(RecipientsActivity.this);
                    builder.setTitle(getString(R.string.dialog_title_fileerror))
                            .setMessage(getString(R.string.dialog_message_fileError))
                            .setPositiveButton(getString(R.string.dialog_buttonLabel_fileError), null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    private ParseObject createMessage() {
        ParseObject message = new ParseObject(ParseConstants.CLASS_MESSAGES);
        message.put(ParseConstants.KEY_SENDER_ID, ParseUser.getCurrentUser().getObjectId());
        message.put(ParseConstants.KEY_SENDER_NAME, ParseUser.getCurrentUser().getUsername());
        message.put(ParseConstants.KEY_RECIPIENT_IDS, getRecipientIds());
        message.put(ParseConstants.KEY_FILE_TYPE, mFileType);

        if (mFileType.equals(ParseConstants.TYPE_TEXT)) {
            byte[] data = mMessage.getBytes();
            ParseFile file = new ParseFile("resume.txt", data);
            message.put(ParseConstants.KEY_FILE, file);
            return message;
        } else {
            byte[] fileBytes = FileHelper.getByteArrayFromFile(this, mMediaUri);
            if (fileBytes == null) {
                return null;
            } else {
                if (mFileType.equals(ParseConstants.TYPE_IMAGE)) {
                    fileBytes = FileHelper.reduceImageForUpload(fileBytes);
                }

                String fileName = FileHelper.getFileName(this, mMediaUri, mFileType);
                ParseFile file = new ParseFile(fileName, fileBytes);
                message.put(ParseConstants.KEY_FILE, file);

                return message;
            }
        }
    }

    private ArrayList<String> getRecipientIds() {
        ArrayList<String> recipientIds = new ArrayList<String>();
        for (int i = 0; i < getListView().getCount(); i++) {
            if (getListView().isItemChecked(i)) {
                recipientIds.add(mFriends.get(i).getObjectId());
            }
        }
        return recipientIds;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (l.getCheckedItemCount() > 0) {
            mMenuItemSend.setVisible(true);
        } else if (l.getCheckedItemCount() == 0) {
            mMenuItemSend.setVisible(false);
        }
    }
}
