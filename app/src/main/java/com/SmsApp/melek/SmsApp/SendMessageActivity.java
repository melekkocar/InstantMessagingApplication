package com.SmsApp.melek.SmsApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SendMessageActivity extends ActionBarActivity {

    private EditText mEditTextSendMessage;
    private Button mButtonSendMessage;
    private TextView mTextViewTakenMessage;
    private TextView mTextViewSendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        mButtonSendMessage = (Button) findViewById(R.id.button_sendMessage);
        mEditTextSendMessage = (EditText) findViewById(R.id.editText_messageText);
        mTextViewTakenMessage = (TextView) findViewById(R.id.textView_takenMessage);
       // mTextViewSendMessage = (TextView)findViewById(R.id.textView_sendMessage);

        Intent intent = getIntent();
        String message = intent.getStringExtra(ParseConstants.DIFF_KEY_MESSAGE);
        mTextViewTakenMessage.setText(message);

/*
        AlertDialog.Builder popupBuilder = new AlertDialog.Builder(this);
        TextView myMsg = new TextView(this);
        myMsg.setText("Central");
        myMsg.setGravity(Gravity.CENTER_HORIZONTAL);
        popupBuilder.setView(mTextViewSendMessage);


*/


        mButtonSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mEditTextSendMessage.getText().toString();

                if (message.isEmpty()) {
                    Toast.makeText(SendMessageActivity.this, "please enter a message!", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(SendMessageActivity.this, RecipientsActivity.class);
                    intent.putExtra(ParseConstants.DIFF_KEY_MESSAGE, message);
                    intent.putExtra(ParseConstants.KEY_FILE_TYPE, ParseConstants.TYPE_TEXT);
                    //intent.setData(null);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_send_message, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
