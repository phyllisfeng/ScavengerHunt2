package com.example.scavengerhunt;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NewGameActivity extends ActionBarActivity {
    private EditText setGameTitle, setGameDescription, setGameTimeLimit, setItemAmount;
    private String gameTitle, gameDescription, gameTimeLimit, gameItemAmount, gameObjectID, gamePlayerObjectID;
    private int  numOfItem;
    private Bitmap gameCoverDefault;
    private Button createNewGameButton;
    private ProgressDialog progressDialog;
    private ParseObject gameTable, playerTable, itemTable;
    private ParseFile file;

    private SimpleDateFormat sdf;
    private Date mtime;
    private Long millisecTimer;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
        this.createNewGameButtonCallbacks();
            //CreateGameFragment createGameFragment = new CreateGameFragment();
            //createGameFragment.setArguments(getIntent().getExtras());
            //getSupportFragmentManager().beginTransaction().add(R.id.create_game_fragment, createGameFragment).commit();

    }

    public String getGameTitle(){
        setGameTitle = (EditText) findViewById(R.id.editText_NewGameTitle);
        gameTitle = setGameTitle.getText().toString();
        return gameTitle;
    }

    public String getGameDescription(){
        setGameDescription = (EditText) findViewById(R.id.editText_NewGameDescription);
        gameDescription = setGameDescription.getText().toString();
        return gameDescription;
    }

    public Long getTimerInMillisec(){
        setGameTimeLimit = (EditText) findViewById(R.id.editText_NewGameTimeLimit);
        gameTimeLimit = setGameTimeLimit.getText().toString();
        sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            mtime = sdf.parse(gameTimeLimit);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        millisecTimer = mtime.getTime();
        return millisecTimer;
    }

    public int getItemAmount(){
        setItemAmount = (EditText) findViewById(R.id.editText_NewGameItemAmount);
        gameItemAmount = setItemAmount.getText().toString();
        numOfItem = Integer.parseInt(gameItemAmount);
        return numOfItem;
    }

    public ParseFile loadDefaultGameCover(){
        gameCoverDefault = BitmapFactory.decodeResource(getResources(), R.mipmap.new_game_cover);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        gameCoverDefault.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] image = stream.toByteArray();
        file = new ParseFile("gameCover", image);
        file.saveInBackground();
        return file;
    }

    public ParseObject addNewGame(ParseObject gameTable){
        ParseUser user = ParseUser.getCurrentUser();
        gameTable.put("gameName", this.getGameTitle());
        gameTable.put("gameCreator", user.getUsername());
        gameTable.put("gameDescription", this.getGameDescription());
        gameTable.put("timeLimit", this.getTimerInMillisec());
        gameTable.put("itemAmount", this.getItemAmount());
        gameTable.put("gameCover", this.loadDefaultGameCover());
        //gameTable.put("gameStartTime", null);
        //gameTable.put("gameEndTime", null);
        return gameTable;
    }

    public String getObjectID(ParseObject parseTable){
        return parseTable.getObjectId();
    }
    /*
        public ParseObject addPlayer(String gameObjectID, ParseObject playerTable){
            ParseUser user = ParseUser.getCurrentUser();
            int gamescore = 0;
            playerTable.put("playerName", user.getUsername());
            playerTable.put("gameObjectID", gameObjectID);
            playerTable.put("playerGameScore", gamescore);
            return playerTable;
        }
    */
    public ParseObject createItemTable(int numOfItem, String gameObjectID, ParseObject itemTable){
        ParseUser user = ParseUser.getCurrentUser();
        int gamescore = 0;
        itemTable.put("gameObjectID", gameObjectID);
        //itemTable.put("gamePlayerObjectID", gamePlayerObjectID);
        itemTable.put("playerName", user.getUsername());
        itemTable.put("playerGameScore", gamescore);
        itemTable.put("itemNumber", numOfItem);
        //ParseFile[] photo = new ParseFile[numOfItem];
        //Long[] phototime = new Long[numOfItem];
       // itemTable.put("itemPicture", photo);
        //itemTable.put("itemTimeStamp", phototime);
        /*for (int i = 1; i<numOfItem+1; i++){
            //itemTable.add("itemPicture", null);
            //itemTable.add("itemLocation", null);
            //itemTable.add("itemTimeStamp", null);
        }*/
        return itemTable;
    }

    public void createNewGameButtonCallbacks(){
        createNewGameButton = (Button) findViewById(R.id.button_CreateNewGame);
        createNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameTable = new ParseObject("Games");
                addNewGame(gameTable);
                progressDialog = ProgressDialog.show(NewGameActivity.this, getString(R.string.label_newGame_please_wait), getString(R.string.label_newGame_in_progress) + " " + gameTitle + "");
                gameTable.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        dismissProgressDialog();
                        if (e == null) {
                            gameObjectID = getObjectID(gameTable);
                            int numOfItem = getItemAmount();
                            itemTable = new ParseObject("GamePlayerItems");
                            createItemTable(numOfItem, gameObjectID, itemTable);
                            itemTable.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        //Intent intent = new Intent(NewGameActivity.this, GameMenuActivity.class);
                                        //startActivity(intent);
                                        showToast(getString(R.string.title_activity_new_game));
                                    } else
                                        showToast(getString(R.string.label_newItemTable_create_failed));
                                }
                            });

                            // } else
                            // showToast(getString(R.string.label_newPlayerTable_create_failed));
                            //}
                            //});
                        } else
                            showToast(getString(R.string.label_newGame_create_failed));
                    }
                });
                //AddPlayerFragment addPlayerFragment = new AddPlayerFragment();
                //FragmentTransaction transaction = getFragmentManager().beginTransaction();
                //transaction.replace(R.id.create_game_fragment, addPlayerFragment);
                //transaction.addToBackStack(null);
                //transaction.commit();
                //intent to game menu
                Intent intent = new Intent(NewGameActivity.this, GameMenuActivity.class);
               intent.putExtra("gameObjectID", getGameTitle());
                startActivity(intent);
            }
        });
    }

    public void showToast (String message){
        Toast toast = Toast.makeText(NewGameActivity.this, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    public void dismissProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_game, menu);
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
            case R.id.mainmenu_mail:
                //code for mail icon
                return true;
            case R.id.mainmenu_camera:
                //code for camera icon
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
