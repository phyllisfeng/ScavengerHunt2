package com.example.scavengerhunt;

import android.app.Fragment;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;


public class MainMenuActivity extends ActionBarActivity {

    private TextView setUserName, setTotalScore;
    private ImageView setProfilePicture;
    private LinearLayout container;
    //private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;
    private List<ParseObject> gameList;
    private ListView listView;
    private GridView gridview;
    private ParseQueryAdapter<ParseObject> mainAdapter;
    private List<Game> gameObjectList;
    private Bitmap gameImage;
    private ParseFile file;
    String objectid;
    private ArrayAdapter<String> adapter;
    List<ParseObject> ob;

    //private GridViewAdapter gridViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        displayUserName();
        //displayUserProfilePicture();
        displayUserTotalScore();
        //displayGames();

    }

    public ParseUser getUserProfile(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        return currentUser;
    }

    public void displayUserName(){
        setUserName = (TextView) findViewById(R.id.mainMenuTextView_userName);
        String userName = this.getUserProfile().getUsername();
        setUserName.setText("Hi " + userName);
    }

    public void displayUserProfilePicture(){
        setProfilePicture = (ImageView) findViewById(R.id.mainMenuImageView_userProfilePicture);
        ParseFile storedPicture = getUserProfile().getParseFile("userProfilePicture");
        storedPicture.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] bytes, ParseException e) {
                if (e == null) {
                    Bitmap userProfilePicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                    setProfilePicture.setImageBitmap(userProfilePicture);
                }
            }
        });
    }

    public void displayUserTotalScore(){
        setTotalScore = (TextView) findViewById(R.id.mainMenuTextView_userTotalScore);
        String score = getUserProfile().get("userTotalScore").toString();
        setTotalScore.setText("Total Score: " + score);
    }

    public void displayGames(){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("GamePlayerItems");
        final ParseQuery<ParseObject> gamelist = new ParseQuery<ParseObject>("Games");
        listView = (ListView)findViewById(R.id.mainMenuListView);
        adapter = new ArrayAdapter<String>(MainMenuActivity.this, R.layout.list_item);
        query.whereContains("playerName", getUserProfile().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> itemlist, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < itemlist.size(); i++) {
                        ob = itemlist;
                        objectid = itemlist.get(i).getString("gameObjectID");
                        gamelist.whereEqualTo("objectID", objectid);
                        gamelist.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                if (e == null) {
                                    for (int j = 0; j < list.size(); j++) {
                                        adapter.add(list.get(j).getString("gameName"));
                                    }
                                    listView.setAdapter(adapter);
                                }
                            }
                        });
                    }
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainMenuActivity.this, GameMenuActivity.class);
                intent.putExtra("gameObjectID", ob.get(position).getString("objectID"));
                startActivity(intent);
            }
        });
    }
/*
    public void displayGames(){
        gameObjectList = new ArrayList<Game>();
        ArrayAdapter<Game> adapter = new ArrayAdapter<Game>(this, R.layout.list_item, gameObjectList);
        setListAdapter(adapter);
        refreshGameList();
    }

    private void refreshGameList(){
        ParseQuery<ParseObject> gamesItems = ParseQuery.getQuery("GamePlayerItems");
        gamesItems.whereContains("playerName", getUserProfile().getUsername());
        gamesItems.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    gameObjectList.clear();
                    for (ParseObject games : list) {
                        String gameObjectID = games.getString("gameObjectID");
                        ParseQuery<ParseObject> gameTable = ParseQuery.getQuery("Games");
                        gameTable.whereEqualTo("objectID", gameObjectID);
                        gameTable.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> listOfGames, ParseException e) {
                                if (e == null) {
                                    ParseFile image = listOfGames.get(0).getParseFile("gameCover");
                                    image.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] bytes, ParseException e) {
                                            if (e == null) {
                                                gameImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            }
                                        }

                                    });
                                    Game play = new Game(listOfGames.get(0).getString("gameName"), gameImage);
                                    gameObjectList.add(play);
                                }
                            }
                        });
                    }
                    ((ArrayAdapter<Game>) getListAdapter()).notifyDataSetChanged();
                } else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
            }
        });
    }*/
/*3
    public void displayActiveGames(){
        container = (LinearLayout) findViewById(R.id.container);
        ParseQuery<ParseObject> gamesItems = ParseQuery.getQuery("GamePlayerItems");
        gamesItems.whereContains("playerName", getUserProfile().getUsername());
        gamesItems.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> listOfGamePlayerItems, ParseException e) {
                if (e==null){
                    for (int i = 0; i < listOfGamePlayerItems.size(); i++) {
                        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView = layoutInflater.inflate(R.layout.list_item, null);
                        final TextView vGameName = (TextView) addView.findViewById(R.id.gameTitle);
                        final ImageView vGameIcon = (ImageView) addView.findViewById(R.id.gameIcon);
                        String gameObjectID = listOfGamePlayerItems.get(i).getString("gameObjectID");
                        ParseQuery<ParseObject> gameTable = ParseQuery.getQuery("Games");
                        gameTable.whereEqualTo("objectID", gameObjectID);
                        gameTable.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> listOfGames, ParseException e) {
                                if (e == null) {
                                    String gameTitle = listOfGames.get(0).getString("gameName");
                                    vGameName.setText(gameTitle);
                                    ParseFile image = listOfGames.get(0).getParseFile("gameCover");
                                    image.getDataInBackground(new GetDataCallback() {
                                        @Override
                                        public void done(byte[] bytes, ParseException e) {
                                            if (e == null) {
                                                Bitmap gameImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                                vGameIcon.setImageBitmap(gameImage);
                                            }
                                        }

                                    });
                                }
                            }
                        });
                        container.addView(addView);
                    }
                }
            }
        });
    }*/
/* 2
    public void displayActiveGames(){
        setTotalScore = (TextView) findViewById(R.id.mainMenuTextView_userTotalScore);
        ParseQuery<ParseObject> gamesItems = ParseQuery.getQuery("GamePlayerItems");
        gameList = new ArrayList<ParseObject>();
        gamesItems.whereContains("playerName", getUserProfile().getUsername());
        gamesItems.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        String gameObjectID = list.get(i).getString("gameObjectID");
                        ParseQuery<ParseObject> gameTable = ParseQuery.getQuery("Games");
                        gameTable.whereEqualTo("objectID", gameObjectID);
                        gameTable.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                if (e == null) {
                                    listView = (ListView) findViewById(R.id.listView);
                                    CustomListViewAdapter adapter = new CustomListViewAdapter(MainMenuActivity.this, R.layout.list_item, list);
                                    listView.setAdapter(adapter);
                                }
                            }
                        });
                    }
                }
            }
        });

        //String gameTitle = firstGame.getString("gameName");
        //setTotalScore.setText("Total Score for: " + gameTitle);
    } */
/*
    public void displayActiveGames() {
        container = (LinearLayout) findViewById(R.id.container);
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.list_active_games, null);
        ImageButton gameCover = (ImageButton) addView.findViewById(R.id.activeGamesImageButton);


        List<ParseQuery<ParseObject>> parsePlayerQueryList = new ArrayList<ParseQuery<ParseObject>>();
        ParseQuery<ParseObject> playerQuery = ParseQuery.getQuery("GamePlayers");
        playerQuery.whereEqualTo("playerName", this.getUserProfile().getUsername());
        parsePlayerQueryList.add(playerQuery);
        ParseQuery<ParseObject> playerListQuery = ParseQuery.or(parsePlayerQueryList);
        playerListQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> results, ParseException e) {
                if (e == null){
                    List<ParseQuery<ParseObject>> parseGameQueryList = new ArrayList<ParseQuery<ParseObject>>();
                    ParseQuery<ParseObject> gameQuery = ParseQuery.getQuery("Games");
                    for (int i=0; i<results.size(); i++){
                        String gameID = results.get(i).getParseObject("gameObjectID").toString();
                        gameQuery.whereEqualTo("ObjectID", gameID);
                        parseGameQueryList.add(gameQuery);
                        ParseQuery<ParseObject> gameListQuery = ParseQuery.or(parseGameQueryList);
                        gameListQuery.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> list, ParseException e) {
                                if (e == null){
                                    //success if game end time is less than current time then display game
                                    TextView gameTitle = (TextView) addView.findViewById(R.id.activeGamesTextView);
                                    gameTitle.setText("game1");
                                    container.addView(addView);
                                } else {
                                    //fail
                                }
                            }
                        });

                    }
                } else {
                    //fail
                }
            }
        });
    }
*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case R.id.action_settings:

                return true;
            case R.id.mainmenu_invite:
                startActivity(new Intent(this, ListUsersActivity.class));
                return true;
            case R.id.mainmenu_create_game:
                startActivity(new Intent(this, NewGameActivity.class));
                return true;
            case R.id.mainmenu_mail:
                //code for mail icon
                startActivity(new Intent(this, GameMenuActivity.class));
                return true;
            case R.id.mainmenu_camera:
                //code for camera icon
                startActivity(new Intent(this, CameraActivity.class));
                return true;
            case R.id.action_logout:
                ParseUser.logOut();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
