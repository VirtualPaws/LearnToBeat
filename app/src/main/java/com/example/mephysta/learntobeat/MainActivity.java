package com.example.mephysta.learntobeat;

import android.app.Activity;
import android.content.Intent;
import android.icu.math.BigDecimal;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    public static Metronome metronome;
    public static Thread metronomeThread;
    public static double start;                             // Metronom Startzeit

    public static boolean isPlaying = false;                // trigger für das Metronom
    public static final int BPM = 60;                       // Beats Per Minute
    public static final double TOLERANCE = 0.25;            // Toleranz beim Treffen des genauen Takts
    public static final int COLOR_SUCCESS = 0xFF00FF00;     // Feedback Farbe bei Erfolg
    public static final int COLOR_FAIL = 0xFFFF0000;        // Feedback Farbe bei Misserfolg
    public static int successCounter = 0;                  // Zähler der richtigen Klicks
    public static int failCounter = 0;                     // Zähler der falschen Klicks
    public static final int DIVISOR_SECONDS = 1000;         // Divisor ms in s

    public static final int TOTAL_LEVEL_TIME = 30000;       // Maximale Levelzeit ( in ms)
    public static final int COUNTDOWN = 3;                  // Startcountdown bevor Level beginnt

    private ProgressBar mProgress;                          // Progressbalken
    private int mProgressStatus = 0;                        // Status des Progressbalkens (0-100)
    private Handler mHandler = new Handler();               // für async Update des Progressbalkens

    public static Map<Integer, Double> beat2time = new HashMap<>();

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer mp5 = MediaPlayer.create(this, R.raw.acoustic_snare);

        for(int i = 0; i<BPM; i++){
            beat2time.put(i,(60/(double)BPM)*i + (60/(double)BPM)/2);
            Log.d(i + ". beat", " " + beat2time.get(i));
        }

        metronome = new Metronome(BPM, 1000, 18.35, 16.35);
        metronomeThread = new Thread(metronome);

        mProgress = (ProgressBar) findViewById(R.id.progress_bar);


        /*
         * Der Countdowntimer wird mit Levelbeginn gestartet und zählt dann die maximale Levelzeit runter.
         * Wenn die Zeit abgelaufen ist, wird auch das Metronom gestoppt.
         * @param TOTAL_LEVEL_TIME -> Wie lange das Level geht. Momentan 30 sec
         * @param DIVISOR_SECONDS -> Wie oft ein Event gehandelt wird. Momentan jede Sekunde.
         */
        final CountDownTimer countDowntimer = new CountDownTimer(TOTAL_LEVEL_TIME, DIVISOR_SECONDS) {
            /*
             * Eventhandling bei jedem Tick des CountDowns (Momentan jede Sekunde).
             * Ein Progessbalken wird aktualisiert, damit man erkennt, wie lange das Level noch geht.
             */
            public void onTick(long millisUntilFinished) {
                Log.d("CD","Time: " + millisUntilFinished / DIVISOR_SECONDS);

                // Aktualisiert den Progressbalken.
                mProgressStatus = (int)(Math.round((((TOTAL_LEVEL_TIME - millisUntilFinished)) * (100d/30d) / DIVISOR_SECONDS) + (100d/30d)));
                mHandler.post(new Runnable() {
                    public void run() {
                        mProgress.setProgress(mProgressStatus);
                    }
                });
            }
            /*
             * Handling wenn Countdowntimer abgelaufen ist.
             * Momentan wird der Progessbalken aktualisiert und das Metronom gestoppt.
             * TODO Auswertung anzeigen mit weiterführender Navigation.
             */
            public void onFinish() {
                Log.d("CD","FERTIG. Metronome wird beendet und Auswertung gezeigt.");
                mProgress.setProgress(100);
                isPlaying = false;
                metronomeThread.interrupt();
                finishLevel();
            }
        };

        // OnclickListener
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.drum1:
                        if (isPlaying) {
                            isPlaying = false;
                            metronomeThread.interrupt();
                        } else if (!isPlaying) {
                            isPlaying = true;
                            metronomeThread.start();
                            start = System.currentTimeMillis();
                            countDowntimer.start();
                        }
                        break;
                    case R.id.drum5:
                        //mp5.start();
                        Button resultBtn = (Button) findViewById(R.id.result);
                        double duration = (double)((System.currentTimeMillis() - start) / DIVISOR_SECONDS);
                        boolean hitCorrectly = false;
                        if(duration < 60) {
                            for (double value : beat2time.values()) {
                                if (duration < value + TOLERANCE && duration > value - TOLERANCE) {
                                    Log.d("Hit", "TREFFER: " + duration);
                                    hitCorrectly = true;
                                }
                            }
                        }else{
                            Log.d("Hit", "ZEIT ABGELAUFEN");
                        }
                        Log.d("Hit", " " + duration);
                        if(hitCorrectly){
                            resultBtn.setBackgroundColor(COLOR_SUCCESS);
                            successCounter++;
                        }else{
                            resultBtn.setBackgroundColor(COLOR_FAIL);
                            failCounter++;
                        }
                        break;
                    default:
                        Log.d("", "Unknown button: " + v.getId());
                }
            }
        };

        Button btn1 = (Button) findViewById(R.id.drum1);
        Button btn5 = (Button) findViewById(R.id.drum5);

        btn1.setOnClickListener(listener);
        btn5.setOnClickListener(listener);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*
     * Beendet das Level und zeigt die Auswertung.
     */
    private void finishLevel() {
        Intent intent = new Intent(this, LevelFinished.class);
        intent.putExtra("result_success", successCounter);
        intent.putExtra("result_fail", failCounter);
        startActivity(intent);
        finish();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
