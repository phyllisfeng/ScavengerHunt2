package com.example.scavengerhunt;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class GameMenuActivity extends ActionBarActivity {

    String gameObjectID;
    TextView setGameTitle, setGameDescription, setTimeLimit, setTimeLeft, setItemsToCollect, setMyScore;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_menu);
        addListenerOnButton();

        Intent intent = getIntent();
        gameObjectID = intent.getStringExtra("gameObjectID");
        //ParseObject gameTable = new ParseObject("GameTable");
        setGameTitle = (TextView) findViewById(R.id.gameMenu_gameTitle);
        setGameTitle.setText(" "+gameObjectID);
        /*ParseQuery<ParseObject> query = ParseQuery.getQuery("GameTable");
        query.whereEqualTo("objectID", gameObjectID);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null){
                    String gameTitle = list.get(0).get("gameName").toString();
                    if (gameTitle != null){
                        setGameTitle.setText(gameTitle);
                    }
                }
            }
        });*/

    }

    public void addListenerOnButton()
    {

        // Inflate the menu; this adds items to the action bar if it is present.
        button = (Button) findViewById(R.id.addPlayerButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent =
                        new Intent(GameMenuActivity.this, ListUsersActivity.class);
                startActivity(myIntent);

            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_menu, menu);
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
