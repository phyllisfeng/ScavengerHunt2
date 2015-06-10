package com.example.scavengerhunt;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
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

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends ActionBarActivity {

    private static final String TAG = "LoginActivity";
    private Button continueButton, cancelButton;
    private EditText usernameEditText, passwordEditText;
    private String username, password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButtonCallbacks();
    }

    @Override
    public void onResume(){
        super.onResume();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null && currentUser.getObjectId() != null){
            username = currentUser.getUsername();
            usernameEditText.setText(username);
            showToast(" " + getString(R.string.title_activity_login));
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        }
    }

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

    private void signInButtonCallbacks() {
        usernameEditText = (EditText) findViewById(R.id.textbox_loginUsername);
        passwordEditText = (EditText) findViewById(R.id.textbox_loginPassword);

        continueButton = (Button) findViewById(R.id.loginbutton_continue_login);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                if (username == null || username.length() == 0 ){
                    showToast(getString(R.string.hint_username));
                    return;
                }
                if (password == null || password.length() == 0){
                    showToast(getString(R.string.hint_password));
                    return;
                }
                progressDialog = ProgressDialog.show(LoginActivity.this, getString(R.string.label_login_please_wait), getString(R.string.label_query_in_progress) + " " + username + "");
                ParseUser.logInInBackground(username, password, loginCallback);
            }
        });
        cancelButton = (Button) findViewById(R.id.loginbutton_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                finish();
            }
        });
    }

    private final LogInCallback loginCallback = new LogInCallback(){
        @SuppressLint("LongLogTag")
        @Override
        public void done (ParseUser arg0, ParseException arg1){
            dismissProgressDialog();
            if (arg0 != null){
                Log.d(TAG + ".doParseLogin", "Success! Current User ObjectID: " + arg0.getObjectId());
                showToast(" " + getString(R.string.title_activity_login));
                startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                finish();
            } else{
                Log.d(TAG + ".doParseLogin", "Failed", arg1);
                showToast(getString(R.string.label_loginErrorMessage) + " " + arg1.getMessage() + ". " + getString(R.string.label_loginPleaseTryAgainMessage));
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
