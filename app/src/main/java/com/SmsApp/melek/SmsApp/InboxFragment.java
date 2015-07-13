package com.SmsApp.melek.SmsApp;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class InboxFragment extends ListFragment {
    private static final String TAG = InboxFragment.class.getSimpleName();
    protected List<ParseObject> mMessagesTaken;
    protected SwipeRefreshLayout mSwipeResfreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        //resfresh yapılması için tanımlama yapıyoruz

        mSwipeResfreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeResfreshLayout.setOnRefreshListener(mOnResfreshListener);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        retrieveMessages();


    }

    private void retrieveMessages() {
        ParseQuery<ParseObject> messages = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
        messages.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
        messages.orderByDescending(ParseConstants.KEY_CREATED_AT);
        messages.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                getActivity().setProgressBarIndeterminateVisibility(false);

                if(mSwipeResfreshLayout.isRefreshing()){
                    mSwipeResfreshLayout.setRefreshing(false);

                }



                if (e == null) {
                    mMessagesTaken = parseObjects;

                    int i = 0;
                    String[] usernames = new String[mMessagesTaken.size()];
                    for (ParseObject message : mMessagesTaken) {
                        usernames[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
                        i++;
                    }


                    if(getListView().getAdapter() == null) {

                        MessageAdapter messageAdapter = new MessageAdapter(getListView().getContext(), mMessagesTaken);
                        setListAdapter(messageAdapter);

                    }else{



                    }




                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
                    builder.setTitle("error!");
                    builder.setMessage(e.getMessage());
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                getActivity().setProgressBarIndeterminateVisibility(false);

            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject message = mMessagesTaken.get(position);
        String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
        ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
        Uri uri = Uri.parse(file.getUrl());

        //view image
        if (messageType.equals(ParseConstants.TYPE_IMAGE)) {
            Intent intent = new Intent(getActivity(), ViewImageActivity.class);
            intent.setData(uri);
            startActivity(intent);
        }
        //view video
        else if (messageType.equals(ParseConstants.TYPE_VIDEO)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setDataAndType(uri, "video/*");
            startActivity(intent);
        } else {
            file.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        // data has the bytes for the resume
                        Intent intent = new Intent(getActivity(), SendMessageActivity.class);
                        String text = new String(data);
                        intent.putExtra(ParseConstants.DIFF_KEY_MESSAGE, text);
                        startActivity(intent);
                    } else {
                        // something went wrong
                    }
                }
            });


            //mesajın veritabanından silinmesi için

            List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);

            if(ids.size()== 0); {

                message.deleteInBackground();
            }

        }


    }
    protected SwipeRefreshLayout.OnRefreshListener mOnResfreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {

            retrieveMessages();
          //  Toast.makeText(getActivity() , "resfresh!!" , Toast.LENGTH_SHORT).show();
        }
    };
}
