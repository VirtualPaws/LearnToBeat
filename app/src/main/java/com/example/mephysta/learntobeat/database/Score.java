package com.example.mephysta.learntobeat.database;

/**
 * Created by sJantzen on 16.01.2017.
 */

public class Score {

    private long id;
    private long levelId;
    private int score;
    private int bpm;
    private String playerName;
    private int time;

    public Score (int score, int bpm, int time){
        this.score = score;
        this.bpm = bpm;
        this.time = time;
    }

    public Score(long id, int score, int bpm, int time){
        this.id = id;
        this.score = score;
        this.bpm = bpm;
        this.time =  time;
    }

    public Score(long id, long levelId, int score, int bpm, String playerName, int time){
        this.id = id;
        this.levelId = levelId;
        this.score = score;
        this.bpm = bpm;
        this.playerName = playerName;
        this.time =  time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLevelId() {
        return levelId;
    }

    public void setLevelId(long levelId) {
        this.levelId = levelId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString(){
        return "" + playerName + " has total score: " + score + " from max. " + bpm;
    }
}
