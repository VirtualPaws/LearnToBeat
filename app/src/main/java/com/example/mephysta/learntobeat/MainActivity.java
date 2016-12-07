package com.example.mephysta.learntobeat;

import android.icu.math.BigDecimal;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Metronome metronome;
    Thread metronomeThread;
    double start;

    public static boolean isPlaying = false;
    public static final int BPM = 60;
    public static final double TOLERANCE = 0.25;
    public static final int COLOR_SUCCESS = 0xFF00FF00;
    public static final int COLOR_FAIL = 0xFFFF0000;
    public static final int DIVISOR_SECONDS = 1000;

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
/*
        // METRONOME
        metronome = new Metronome();

        // TODO set vars
        metronome.setBpm(120); //geschwindigkeit
        metronome.setBeat(1000); //anzahl der schläge
        metronome.setBeatSound(16.35); //sound C
        metronome.setSound(18.35); // sound D
*/

        for(int i = 0; i<BPM; i++){
            beat2time.put(i,(60/(double)BPM)*i);
            Log.d(i + ". beat", " " + beat2time.get(i));
        }

        metronome = new Metronome(BPM, 1000, 18.35, 16.35);
        metronomeThread = new Thread(metronome);

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
                        }
                        break;
                    case R.id.drum5:
                        //mp5.start();
                        Button resultBtn = (Button) findViewById(R.id.result);
                        double duration = (double)((System.currentTimeMillis() - start) / DIVISOR_SECONDS);
                        boolean hitCorrectly = false;
                        for(double value : beat2time.values()){
                            if(duration < value + TOLERANCE && duration > value - TOLERANCE){
                                Log.d("RESULT", "treffer!");
                                hitCorrectly = true;
                            }
                        }
                        Log.d("Hit", " " + duration);
                        if(hitCorrectly){
                            resultBtn.setBackgroundColor(COLOR_SUCCESS);
                        }else{
                            resultBtn.setBackgroundColor(COLOR_FAIL);
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
