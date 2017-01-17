package com.example.mephysta.learntobeat.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sJantzen on 16.01.2017.
 */

public class DataSource {

    private static final String LOG_TAG = DataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private DbHelper dbHelper;


    public DataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new DbHelper(context);
    }

    public void open(){
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close(){
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    /**
     * Inserts a new entry in the score table.
     * @param levelId
     * @param score
     * @param bpm
     * @param playerName
     * @param time
     * @return
     */
    public long createScore(long levelId, int score, int bpm, String playerName, int time){
        ContentValues values = new ContentValues();
        values.put(DbHelper.COLUMN_LEVEL_ID, levelId);
        values.put(DbHelper.COLUMN_SCORE, score);
        values.put(DbHelper.COLUMN_BPM, bpm);
        values.put(DbHelper.COLUMN_PLAYERNAME, playerName);
        values.put(DbHelper.COLUMN_TIME, time);

        return database.insert(DbHelper.TABLE_SCORE,null,values);
    }

    /**
     * Returns a score element from DB by the given (score)- id.
     * @param id
     * @return
     */
    public Score getScoreById(long id){
        Cursor cursor = database.query(DbHelper.TABLE_SCORE,
                DbHelper.COLUMNS, DbHelper.COLUMN_ID + "=" + id,
                null, null, null, null);

        cursor.moveToFirst();
        Score score = cursorToScore(cursor);
        cursor.close();

        return score;
    }

    /**
     * Returns a list of all scores.
     * @return
     */
    public List<Score> getAllScores(){
        List<Score> rv = new ArrayList<>();

        Cursor cursor = database.query(DbHelper.TABLE_SCORE,
                DbHelper.COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        Score score;

        while(!cursor.isAfterLast()) {
            score = cursorToScore(cursor);
            rv.add(score);
            Log.d(LOG_TAG, "ID: " + score.getId() + ", Score: " + score.getScore());
            cursor.moveToNext();
        }
        cursor.close();

        return rv;
    }

    /**
     * Returns a list of all scores.
     * @return
     */
    public Score getHighScore(){

        Cursor cursor = database.query(DbHelper.TABLE_SCORE,
                DbHelper.COLUMNS, null, null, null, null, DbHelper.COLUMN_SCORE + " DESC");

        cursor.moveToFirst();
        Score rv = cursorToScore(cursor);
        cursor.close();

        return rv;
    }

    /**
     * Returns a score element from DB by the given cursor.
     * @param cursor
     * @return
     */
    private Score cursorToScore(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DbHelper.COLUMN_ID);
        int idLevelId = cursor.getColumnIndex(DbHelper.COLUMN_LEVEL_ID);
        int idScore = cursor.getColumnIndex(DbHelper.COLUMN_SCORE);
        int idBpm = cursor.getColumnIndex(DbHelper.COLUMN_BPM);
        int idPlayerName = cursor.getColumnIndex(DbHelper.COLUMN_PLAYERNAME);
        int idTime = cursor.getColumnIndex(DbHelper.COLUMN_TIME);

        long id = cursor.getLong(idIndex);
        long levelId = cursor.getLong(idLevelId);
        int scoreValue = cursor.getInt(idScore);
        int bpm = cursor.getInt(idBpm);
        String playerName = cursor.getString(idPlayerName);
        int time = cursor.getInt(idTime);

        return new Score(id, levelId, scoreValue, bpm, playerName, time);
    }

}
