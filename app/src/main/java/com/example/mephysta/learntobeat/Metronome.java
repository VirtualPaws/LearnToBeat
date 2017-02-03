package com.example.mephysta.learntobeat;

import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.Time;
import android.util.Log;

/*
 * Source: http://masterex.github.io/archive/2012/05/28/android-audio-synthesis.html
 */
public class Metronome implements Runnable {
    private double bpm;
    private int beat;
    private int noteValue;
    private int silence;
    private double beatSound;
    private double sound;
    private final int tick = 50; // samples of tick, ursache für doppel klick
    private boolean play = true;
    private AudioGenerator audioGenerator = new AudioGenerator(8000);

    public Metronome(int bpm, int beat, double beatSound, double sound) {
        audioGenerator.createPlayer();
        setBpm(bpm);
        setBeat(beat);
        setBeatSound(beatSound);
        setSound(sound);
    }

    public void calcSilence() {
        silence = (int) (((60/bpm)*8000)-tick);
    }

    public void run() {
        //moves the current thread into the background
        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        play();
    }

    public void play() {
        // TODO
        // Theoretisch muss hier nur eine Sleep rein, so lange, wie das erste Bonbon braucht, um
        // bis zur Mitte des Bildschirms zu gelangen
        try {
            Log.d("METRONOME", "WAIT");
            Thread.sleep(10000);
        } catch (Exception e) {
            Log.d("METRONOME", "Wait failed " + e.getMessage());
        }

        calcSilence();
        double[] tick =
                audioGenerator.getSineWave(this.tick, 8000, beatSound);
        double[] tock =
                audioGenerator.getSineWave(this.tick, 8000, sound);
        double silence = 0;
        double[] sound = new double[8000];
        int t = 0,s = 0,b = 0;
        do {
            for(int i=0;i<sound.length&&play;i++) {
                if(t<this.tick) {
                    if(b == 0) {
                        sound[i] = tock[t];
                    }else {
                        sound[i] = tick[t];
                    }
                    t++;
                } else {
                    sound[i] = silence;
                    s++;
                    if(s >= this.silence) {
                        t = 0;
                        s = 0;
                        b++;
                        if(b > (this.beat-1))
                            b = 0;
                    }
                }
            }
            audioGenerator.writeSound(sound);
        } while(MainActivity.isPlaying);
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public void setBeat(int beat) {
        this.beat = beat;
    }

    public void setBeatSound(double beatSound) {
        this.beatSound = beatSound;
    }

    public void setSound(double sound) {
        this.sound = sound;
    }
}