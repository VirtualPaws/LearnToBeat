package com.example.mephysta.learntobeat;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer mp1 = MediaPlayer.create(this, R.raw.acoustic_snare);
        final MediaPlayer mp2 = MediaPlayer.create(this, R.raw.acoustic_snare);
        final MediaPlayer mp3 = MediaPlayer.create(this, R.raw.acoustic_snare);
        final MediaPlayer mp4 = MediaPlayer.create(this, R.raw.acoustic_snare);
        final MediaPlayer mp5 = MediaPlayer.create(this, R.raw.acoustic_snare);
        final MediaPlayer mp6 = MediaPlayer.create(this, R.raw.acoustic_snare);
        final MediaPlayer mp7 = MediaPlayer.create(this, R.raw.acoustic_snare);
        final MediaPlayer mp8 = MediaPlayer.create(this, R.raw.acoustic_snare);

        // OnclickListener
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.drum1:
                        mp1.start();
                        break;
                    case R.id.drum2:
                        mp2.start();
                        break;
                    case R.id.drum3:
                        mp3.start();
                        break;
                    case R.id.drum4:
                        mp4.start();
                        break;
                    case R.id.drum5:
                        mp5.start();
                        break;
                    case R.id.drum6:
                        mp6.start();
                        break;
                    case R.id.drum7:
                        mp7.start();
                        break;
                    case R.id.drum8:
                        mp8.start();
                        break;
                    default:
                        Log.d("", "Unknown button: " + v.getId());
                }
            }
        };

        Button btn1 = (Button) findViewById(R.id.drum1);
        Button btn2 = (Button) findViewById(R.id.drum2);
        Button btn3 = (Button) findViewById(R.id.drum3);
        Button btn4 = (Button) findViewById(R.id.drum4);
        Button btn5 = (Button) findViewById(R.id.drum5);
        Button btn6 = (Button) findViewById(R.id.drum6);
        Button btn7 = (Button) findViewById(R.id.drum7);
        Button btn8 = (Button) findViewById(R.id.drum8);

        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);
        btn4.setOnClickListener(listener);
        btn5.setOnClickListener(listener);
        btn6.setOnClickListener(listener);
        btn7.setOnClickListener(listener);
        btn8.setOnClickListener(listener);

        // METRONOME
        Metronome metronome = new Metronome();

        // TODO set vars

        metronome.setBpm(120);
        metronome.setBeat(1000);
        metronome.setBeatSound(16.35);
        metronome.setSound(18.35);
        metronome.play();

    }
}
