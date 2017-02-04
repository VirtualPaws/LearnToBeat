package com.example.mephysta.learntobeat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.mephysta.learntobeat.Animation.Bonbon;
import com.example.mephysta.learntobeat.Animation.GamePanel;
import com.example.mephysta.learntobeat.Animation.UiUtils;
import com.example.mephysta.learntobeat.Animation.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    public static Metronome metronome;
    public static Thread metronomeThread;
    public static double start;                             // Metronom Startzeit
    public static boolean isPlaying = false;                // trigger für das Metronom
    public static final int BPM = 60;                       // Beats Per Minute
    public static double timeBetween2Beats;                 // Time between two beats, depending on BPM
    public static final int FPS = 30;                       // Frames Per Second
    public static final double TOLERANCE = 0.25;             // Toleranz beim Treffen des genauen Takts
    public static final int COLOR_SUCCESS = 0xFF00FF00;     // Feedback Farbe bei Erfolg
    public static final int COLOR_FAIL = 0xFFFF0000;        // Feedback Farbe bei Misserfolg
    public static int successCounter = 0;                   // Zähler der richtigen Klicks
    public static int failCounter = 0;                      // Zähler der falschen Klicks
    public static final int DIVISOR_SECONDS = 1000;         // Divisor ms in s
    public static final int TOTAL_LEVEL_TIME = 30000;       // Maximale Levelzeit ( in ms)
    public static final int COUNTDOWN = 3;                  // Startcountdown bevor Level beginnt
    private ProgressBar mProgress;                          // Progressbalken
    private int mProgressStatus = 0;                        // Status des Progressbalkens (0-100)
    private Handler mHandler = new Handler();               // für async Update des Progressbalkens
    public static Map<Integer, Double> beat2time = new HashMap<>();
    private CountDownTimer countDowntimer;

    public static int waitForFirstBonbon;
    public static int bonbonSpeed;

    public GamePanel gamePanelSurfaceView;
    public Animation animTranslate;
    public Animation animRotate;
    public ImageView pawImageView;
    public ArrayList<Bonbon> bonbons;

    public RelativeLayout infoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // PROGRESS BAR
        mProgress = (ProgressBar) findViewById(R.id.progress_bar);

        // METRONOME
        int numberOfBeatsInLevelTime = BPM / 60 * (TOTAL_LEVEL_TIME/DIVISOR_SECONDS);
        /* Example: BPM = 120 - LevelTime = 30s
         *      BPM       x            120    x          120
         *      ---  = -------   -->   --- = ---   -->   --- * 30s = x -->  60 = x
         *      60s    LvlTime         60s   30s         60s                """"""
         */
        timeBetween2Beats = (TOTAL_LEVEL_TIME/DIVISOR_SECONDS)/(double)numberOfBeatsInLevelTime;
        bonbonSpeed = (int)(((GamePanel.WIDTH - Utils.convertDpToPx(60) + 524)/2)/(timeBetween2Beats * FPS));
        waitForFirstBonbon =((((GamePanel.WIDTH - Utils.convertDpToPx(60) +524)* DIVISOR_SECONDS))/bonbonSpeed)/FPS;
        Log.d("TB2B", ""+timeBetween2Beats + " bonbonSpeed: " + bonbonSpeed + " waitForFirstBonbon: " + waitForFirstBonbon);

        for(int i = 0; i<numberOfBeatsInLevelTime; i++){
            beat2time.put(i,((TOTAL_LEVEL_TIME/DIVISOR_SECONDS)/(double)numberOfBeatsInLevelTime)*i + ((TOTAL_LEVEL_TIME/DIVISOR_SECONDS)/(double)BPM)/2);
            Log.d(i + ". beat", " " + beat2time.get(i));
        }
        metronome = new Metronome(BPM, 1000, 18.35, 16.35);
        metronomeThread = new Thread(metronome);

        /*
         * Der Countdowntimer wird mit Levelbeginn gestartet und zählt dann die maximale Levelzeit runter.
         * Wenn die Zeit abgelaufen ist, wird auch das Metronom gestoppt.
         * @param TOTAL_LEVEL_TIME -> Wie lange das Level geht. Momentan 30 sec
         * @param DIVISOR_SECONDS -> Wie oft ein Event gehandelt wird. Momentan jede Sekunde.
         */
        countDowntimer = new CountDownTimer(TOTAL_LEVEL_TIME, 10) {
            /*
             * Eventhandling bei jedem Tick des CountDowns (Momentan jede Sekunde).
             * Ein Progessbalken wird aktualisiert, damit man erkennt, wie lange das Level noch geht.
             */
            public void onTick(long millisUntilFinished) {
                //Log.d("CD","Time: " + millisUntilFinished / DIVISOR_SECONDS);

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
             */
            public void onFinish() {
                Log.d("CD","FERTIG. Metronome wird beendet und Auswertung gezeigt.");
                mProgress.setProgress(100);
                isPlaying = false;
                metronomeThread.interrupt();
                finishLevel();
            }
        };

        gamePanelSurfaceView = (GamePanel) findViewById(R.id.gamePanel);
        bonbons = new ArrayList<Bonbon>();

        animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        pawImageView = (ImageView) findViewById(R.id.anim);
    }

    /*
     * Beendet das Level und zeigt die Auswertung.
     */
    private void finishLevel() {
        Intent intent = new Intent(this, LevelFinished.class);
        intent.putExtra("result_success", successCounter);
        intent.putExtra("result_fail", failCounter);
        intent.putExtra("bpm", BPM);
        intent.putExtra("time", TOTAL_LEVEL_TIME);
        startActivity(intent);
        finish();
    }

    /**
     * Starts and stops the metronome.
     */
    public void startStopMetronome(){
        if (isPlaying) {
            isPlaying = false;
            metronomeThread.interrupt();
            gamePanelSurfaceView.stopAnimation();
        } else if (!isPlaying) {
            // TODO hier muss eine wartezeit rein, damit das Metronom erst Tickt,
            // wenn der erste Bonbon in position ist
            // Außerdem verschwindet das infofenster leider auch erst beim ersten Tick

            try {
                Log.d("METRONOME", "WAIT: " + waitForFirstBonbon);
                Thread.sleep(waitForFirstBonbon);
            } catch (Exception e) {
                Log.d("METRONOME", "Wait failed " + e.getMessage());
            }
            isPlaying = true;
            metronomeThread.start();
            start = System.currentTimeMillis();
            countDowntimer.start();
            // reset hit counter
            successCounter = 0;
            failCounter = 0;
        }
    }

    /**
     * User tries to hit the tick. Checks user input and shows result.
     * @param v
     */
    public void grabBonbon(View v){
        Button resultBtn = (Button) findViewById(R.id.result);
        double duration = (double)((System.currentTimeMillis() - start) / DIVISOR_SECONDS);
        boolean hitCorrectly = false;
        int bonbonToRemoveIndex = 0;
        if(duration < 60) {
            for (int key : beat2time.keySet()) {
                if (duration < beat2time.get(key) + TOLERANCE && duration > beat2time.get(key) - TOLERANCE) {
                    //Log.d("Hit", "TREFFER: " + duration + " value: " + value);
                    hitCorrectly = true;
                    bonbonToRemoveIndex = key;
                }
            }
        }else{
            Log.d("Hit", "ZEIT ABGELAUFEN");
        }
        //Log.d("Hit", " " + duration);
        if(hitCorrectly){
            resultBtn.setBackgroundColor(COLOR_SUCCESS);
            try {
                gamePanelSurfaceView.removeBonbonFromStick(bonbonToRemoveIndex);
            }catch(Exception ie){
                Log.d("GrabBonbon", "Bonbon does not yet exists. " + (bonbonToRemoveIndex));
            }

            successCounter++;
        }else{
            resultBtn.setBackgroundColor(COLOR_FAIL);
            failCounter++;
        }

        AnimationSet sets = new AnimationSet(false);
        sets.addAnimation(animRotate);
        sets.addAnimation(animTranslate);

        sets.setAnimationListener(new Animation.AnimationListener() {

            //Check if paw hit-area collided with a bonbon. If yes, remove the bonbon from the surface view
            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}
        });
        pawImageView.startAnimation(sets);
    }

    public void removeDialog(){
        // dialog Box
        infoDialog = (RelativeLayout) findViewById(R.id.infoDialog);
        infoDialog.removeAllViews();
    }

    public void startGame(View v){
        removeDialog();
        gamePanelSurfaceView.startAnimation();
        startStopMetronome();
    }

}