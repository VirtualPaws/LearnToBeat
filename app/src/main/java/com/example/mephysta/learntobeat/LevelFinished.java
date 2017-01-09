package com.example.mephysta.learntobeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.logging.Level;

/**
 * Created by sJantzen on 09.01.2017.
 */
public class LevelFinished extends AppCompatActivity {

    public static int successHits = 0;
    public static int failedHits = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level_finished);

        Intent resultIntent = getIntent();
        successHits = resultIntent.getIntExtra("result_success", 0);
        failedHits = resultIntent.getIntExtra("result_fail", 0);

        TextView resultText = (TextView) findViewById(R.id.resultText);
        resultText.setText(successHits + " von 30 Ticks richtig getroffen!");

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
                        // TODO noch nicht implementiert
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
}
