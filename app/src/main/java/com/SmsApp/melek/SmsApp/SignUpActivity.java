package com.SmsApp.melek.SmsApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignUpActivity extends ActionBarActivity {
    protected Button mButtonOk;
    protected EditText mPasswordEditText;
    protected EditText mEMailEditText;
    protected EditText mUserNameEditText;
    protected EditText mTelephoneEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_sign_up);

        mUserNameEditText = (EditText) findViewById(R.id.editTexy_username_signup);
        mEMailEditText = (EditText) findViewById(R.id.editText_signUp_email);
        mPasswordEditText = (EditText) findViewById(R.id.editText_signUp_password);
        mButtonOk = (Button) findViewById(R.id.button_SignUp_SignUp);
        mTelephoneEditText = (EditText)findViewById(R.id.mTelephoneNumber);

        mButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mUserNameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                String email = mEMailEditText.getText().toString();
                String telephone = mTelephoneEditText.getText().toString();

                userName = userName.trim();
                password = password.trim();
                email = email.trim();
                telephone = telephone.trim();

                if (userName.isEmpty() || password.isEmpty() || email.isEmpty() || telephone.isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                    builder.setTitle("Input error!");
                    builder.setMessage("Please check your username, email, password");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            setSupportProgressBarIndeterminateVisibility(true);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    setSupportProgressBarIndeterminateVisibility(true);
                    ParseUser newUser = new ParseUser();
                    newUser.setUsername(userName);
                    newUser.setEmail(email);
                    newUser.setPassword(password);
                 //   newUser.set(telephone);

                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            setSupportProgressBarIndeterminateVisibility(false);
                            if (e == null) {
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                                builder.setTitle("Input error!");
                                builder.setMessage(e.getMessage());
                                builder.setPositiveButton(android.R.string.ok, null);

                                AlertDialog dialog = builder.create();
                                dialog.show();

                            }
                        }
                    });
                }
            }
        });
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
