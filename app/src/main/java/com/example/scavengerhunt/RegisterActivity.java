package com.example.scavengerhunt;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class RegisterActivity extends ActionBarActivity {

    private static final String TAG = "RegistrationActivity";
    private Button registerButton, cancelButton;
    private EditText usernameEditText, passwordEditText, emailEditText;
    private String username, password, email;
    private ProgressDialog progressDialog;
    ParseFile file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerButtonCallbacks();
    }
    private void registerButtonCallbacks() {
        usernameEditText = (EditText) findViewById(R.id.textbox_registerUsername);
        passwordEditText = (EditText) findViewById(R.id.textbox_registerPassword);
        emailEditText = (EditText) findViewById(R.id.textbox_registerEmail);

        registerButton = (Button) findViewById(R.id.registerbutton_register);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                email = emailEditText.getText().toString();

                if (username == null || username.length() == 0) {
                    showToast(getString(R.string.hint_username));
                    return;
                }
                if (password == null || password.length() == 0) {
                    showToast(getString(R.string.hint_password));
                }
                if (email == null || email.length() == 0) {
                    showToast(getString(R.string.hint_email));
                    return;
                }
                progressDialog = ProgressDialog.show(RegisterActivity.this, getString(R.string.label_login_please_wait), getString(R.string.label_query_in_progress) + " '");

                List<ParseQuery<ParseObject>> parseUserQueryList = new ArrayList<ParseQuery<ParseObject>>();

                ParseQuery parseUsernameQuery = ParseUser.getQuery();
                parseUsernameQuery.whereEqualTo("username", username);
                parseUserQueryList.add(parseUsernameQuery);

                ParseQuery<ParseObject> parseUserQuery = ParseQuery.or(parseUserQueryList);
                parseUserQuery.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> arg0, ParseException arg1) {
                        dismissProgressDialog();
                        if (arg1 == null) {
                            if (arg0 != null && arg0.size() > 0) {
                                ParseUser user = (ParseUser) arg0.get(0);
                                if (username != null) {
                                    String existingUsername = user.getUsername();
                                    progressDialog = ProgressDialog.show(RegisterActivity.this, getString(R.string.label_login_please_wait), getString(R.string.label_query_in_progress) + " " + username + "");
                                    if (username.equals(existingUsername)) {
                                        dismissProgressDialog();
                                        usernameEditText.setText("");
                                        usernameEditText.requestFocus();
                                        username = null;
                                        showToast(getString(R.string.label_loginUsernameAlreadyExists));
                                        return;
                                    }
                                }
                            } else
                                progressDialog = ProgressDialog.show(RegisterActivity.this, getString(R.string.label_login_please_wait), getString(R.string.label_signup_in_progress));
                            int score = 0;
                            ParseUser user = new ParseUser();
                            user.setUsername(username);
                            user.setPassword(password);
                            user.setEmail(email);
                            //user.put("userProfilePicture", file);
                            user.put("userTotalScore", score);
                            user.signUpInBackground(registerCallback);
                        } else
                            showToast(getString(R.string.label_signupErrorMessage) + "  " + getString(R.string.label_loginPleaseTryAgainMessage));
                    }
                });
            }
        });
        cancelButton = (Button) findViewById(R.id.registerbutton_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                finish();
            }
        });
    }

    private final SignUpCallback registerCallback = new SignUpCallback() {
        @SuppressLint("LongLogTag")
        @Override
        public void done(ParseException arg0) {
            dismissProgressDialog();
            if (arg0 == null){
                showToast(" haven't upload picture yet");

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                file = new ParseFile("userProfilePicture", image);
                file.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            showToast("file bitmap to byte");
                        } else {
                            showToast("file convert failed");
                        }
                    }
                });
                ParseUser user = ParseUser.getCurrentUser();
                user.put("userProfilePicture", file);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            showToast("  " + getString(R.string.label_register));
                        }
                    }
                });
                showToast("picture uploaded");
                Log.d(TAG + ".doSignUp", "Success!  User account created for username = " + RegisterActivity.this.username);
                startActivity(new Intent(RegisterActivity.this, MainMenuActivity.class));
                finish();

            } else {
                showToast(getString(R.string.label_signupErrorMessage) + "  " + getString(R.string.label_loginPleaseTryAgainMessage));
            }
        }
    };

    private void dismissProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void showToast (String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
