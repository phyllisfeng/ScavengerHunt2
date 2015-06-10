package com.example.scavengerhunt;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pfeng_admin on 06/06/2015.
 */
public class ListUsersActivity extends ActionBarActivity {
    private String currentUserId;
    private ArrayList names;
    private ListView usersListView;
    private ArrayAdapter namesArrayAdapter;



    public void onCreate(Bundle savedInstanceState){
        currentUserId = ParseUser.getCurrentUser().getObjectId();
        names = new ArrayList<String>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
//don't include yourself
        query.whereNotEqualTo("objectId", currentUserId);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i=0; i<userList.size(); i++) {
                        names.add(userList.get(i).getUsername());
                    }
                    usersListView = (ListView)findViewById(R.id.usersListView);
                    namesArrayAdapter =
                            new ArrayAdapter<String>(getApplicationContext(),
                                    R.layout.user_list_item, names);
                    usersListView.setAdapter(namesArrayAdapter);
                    /**usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, View v, int i, long l) {
                    // openConversation(names, i);
                    //send invite to user to join the game
                    //
                    }
                    });**/
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error loading user list",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }
/**
 public void openConversation(ArrayList<String> names, int pos) {
 ParseQuery<ParseUser> query = ParseUser.getQuery();
 query.whereEqualTo("username", names.get(pos));
 query.findInBackground(new FindCallback<ParseUser>() {
 public void done(List<ParseUser> user, ParseException e) {
 if (e == null) {
 //start the messaging activity
 } else {
 Toast.makeText(getApplicationContext(),
 "Error finding that user",
 Toast.LENGTH_SHORT).show();
 }
 }
 });
 }**/
}