package com.SmsApp.melek.SmsApp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;


public class ForgetPasswordActivity extends ActionBarActivity {

    protected EditText mEmailEditText;
    protected Button mOkButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mEmailEditText = (EditText) findViewById(R.id.editText_emailAddress_FORGETPASSWORD);
        mOkButton = (Button) findViewById(R.id.button_ok_FORGETPASSWORD);

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmailEditText.getText().toString();
                email = email.trim();

                if (email.isEmpty()) {
                    Toast.makeText(ForgetPasswordActivity.this, "please enter an email", Toast.LENGTH_LONG).show();
                } else {
                    ParseUser.requestPasswordResetInBackground(email,
                            new RequestPasswordResetCallback() {
                                public void done(ParseException e) {
                                    if (e == null) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                        builder.setTitle("Resetting successfull");
                                        builder.setMessage("Check your email address for new password.");
                                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                Intent intent = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.show();

                                    } else {
                                        // Something went wrong. Look at the ParseException to see what's up.
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                        builder.setTitle("error!");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forget_password, menu);
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
