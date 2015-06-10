package com.example.scavengerhunt;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import static com.parse.Parse.*;

public class TitlePageActivity extends ActionBarActivity {

    private Button signInButton, newUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_page);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "3DC3YWorHUbnxBH6vtsqd4HKH05GkBGOSUGvgyPM", "FBPtS7XYm0R0lCJRij2glHOVBSeXZfh1xynpacbq");

        setupButtonCallbacks();
    }

    private void setupButtonCallbacks() {
        //setContentView(R.layout.activity_title_page);
        signInButton = (Button) findViewById(R.id.loginbutton_sign_in);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write intent
                startActivity(new Intent(TitlePageActivity.this, LoginActivity.class));

            }
        });
        newUserButton = (Button) findViewById(R.id.loginbutton_new_user);
        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write intent
                startActivity(new Intent(TitlePageActivity.this, RegisterActivity.class));

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_title_page, menu);
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
