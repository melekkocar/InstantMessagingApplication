package com.SmsApp.melek.SmsApp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends Activity {

    protected Button mLoginButton;
    protected EditText mPasswordEditText;
    protected EditText mUserNameEditText;
    protected ProgressBar mProgressBar;
    protected TextView mForgettingPassword;
    private Button mSignUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        //spinner loading bar on action bar

        setContentView(R.layout.activity_login);

        mUserNameEditText = (EditText) findViewById(R.id.field_userName_login);
        mPasswordEditText = (EditText) findViewById(R.id.field_password_login);
        mLoginButton = (Button) findViewById(R.id.button_login);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarLogIn);
        mForgettingPassword = (TextView) findViewById(R.id.textView_login_forgettingPasswordField);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressBarIndeterminateVisibility(true);
                String userName = mUserNameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();

                userName = userName.trim();
                password = password.trim();

                if (userName.isEmpty() || password.isEmpty()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Input error!");
                    builder.setMessage("Please check your username, password");
                    builder.setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    //TODO Login user
                    mProgressBar.setVisibility(View.VISIBLE);
                    ParseUser.logInInBackground(userName, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser parseUser, ParseException e) {
                            setProgressBarIndeterminateVisibility(false);
                            mProgressBar.setVisibility(View.INVISIBLE);
                            if (e == null) {
                                //TODO Success
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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

        mSignUpButton = (Button) findViewById(R.id.button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mForgettingPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
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
