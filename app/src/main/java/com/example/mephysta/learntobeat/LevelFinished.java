package com.example.mephysta.learntobeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mephysta.learntobeat.database.DataSource;
import com.example.mephysta.learntobeat.database.Score;

import java.util.logging.Level;

/**
 * Created by sJantzen on 09.01.2017.
 */
public class LevelFinished extends Activity {

    public static final String LOG_TAG = LevelFinished.class.getSimpleName();

    public static int successHits = 0;
    public static int failedHits = 0;
    public static int bpm = 0;
    public static int time = 0;
    public static int tickSum = 0;

    private DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_finished);

        Intent resultIntent = getIntent();
        successHits = resultIntent.getIntExtra("result_success", 0);
        failedHits = resultIntent.getIntExtra("result_fail", 0);
        bpm = resultIntent.getIntExtra("bpm",0);
        time = resultIntent.getIntExtra("time", 0);
        tickSum = resultIntent.getIntExtra("tickSum", 0);

        dataSource = new DataSource(this);

        Log.d(LOG_TAG, "Die Datenquelle wird geöffnet.");
        dataSource.open();

        // save current score
        Log.d(LOG_TAG, "Aktuellen Score (" + successHits + ") in die DB schreiben.");
        dataSource.createScore(1,successHits,bpm,"test",time);

        // get high score
        Score highScore = dataSource.getHighScore();
        Log.d(LOG_TAG, "Aktueller HighScore: " + highScore.getScore());


        Log.d(LOG_TAG, "Die Datenquelle wird geschlossen.");
        dataSource.close();

        TextView resultText = (TextView) findViewById(R.id.resultText);
        resultText.setText(successHits + " von " + tickSum + " Ticks richtig getroffen!");

        // Händelt die verschiedenen Button Klick Events.
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.retry:
                        Log.d("Retry", "Level neu starten.");
                        retry();
                        break;
                    case R.id.menu:
                        Log.d("Menu", "Zurück zum Menu.");
                        startMenu();
                        break;
                }
            }
        };

        Button retryBtn = (Button) findViewById(R.id.retry);
        Button menuBtn = (Button) findViewById(R.id.menu);

        retryBtn.setOnClickListener(listener);
        menuBtn.setOnClickListener(listener);
    }

    /*
     * Startet das Level neu.
     */
    private void retry(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startMenu(){
        Intent intent = new Intent(this, GameMenu.class);
        startActivity(intent);
        finish();
    }
}
