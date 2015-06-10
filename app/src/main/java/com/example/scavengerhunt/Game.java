package com.example.scavengerhunt;

import android.graphics.Bitmap;

/**
 * Created by Joyce on 2015/6/6.
 */
public class Game {
    private String gameTitle;
    private Bitmap gameCover;

    Game(String gameName, Bitmap gamePic){
        gameTitle = gameName;
        gameCover = gamePic;
    }

    public String getGameTitle(){
        return gameTitle;
    }

    public void setGameTitle(String gameName){
        this.gameTitle = gameName;
    }

    public Bitmap getGameCover(){
        return gameCover;
    }

    public void setGameCover(Bitmap gamePic){
        this.gameCover = gamePic;
    }

    @Override
    public String toString(){
        return this.getGameTitle();
    }
}

