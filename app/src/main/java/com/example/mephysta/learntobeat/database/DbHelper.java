package com.example.mephysta.learntobeat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sJantzen on 16.01.2017.
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = DbHelper.class.getSimpleName();

    public static final String DB_NAME = "hit_the_beat.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_SCORE = "score";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_LEVEL_ID = "level_id";
    public static final String COLUMN_SCORE = "score";
    public static final String COLUMN_BPM = "bpm";
    public static final String COLUMN_PLAYERNAME = "playername";
    public static final String COLUMN_TIME = "time";

    public static final String[] COLUMNS = {
            COLUMN_ID,
            COLUMN_LEVEL_ID,
            COLUMN_SCORE,
            COLUMN_BPM,
            COLUMN_PLAYERNAME,
            COLUMN_TIME
    };

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_SCORE +
                    "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LEVEL_ID + " INTEGER, " +
                    COLUMN_SCORE + " INTEGER NOT NULL, " +
                    COLUMN_BPM + " INTEGER NOT NULL, " +
                    COLUMN_PLAYERNAME + " TEXT, " +
                    COLUMN_TIME + " INTEGER NOT NULL);";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG, "Die Tabelle wird mit dem SQL-Befehl: " + SQL_CREATE_TABLE + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE);
        }catch(Exception e) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + e.getMessage());
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
