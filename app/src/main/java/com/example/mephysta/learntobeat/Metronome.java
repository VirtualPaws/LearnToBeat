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
    private Thread runnableMetronome;

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
        long start = System.currentTimeMillis();
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
                        long now = System.currentTimeMillis();
                        //Log.d("TIME TEST", t + ": " + (now - start));
                        //Log.d("i", i + "");
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

    public void stop() {
        play = false;
        audioGenerator.destroyAudioTrack();
    }


    public boolean isPlay() {
        return play;
    }

    public void setPlay(boolean play) {
        this.play = play;
    }

    public double getBpm() {
        return bpm;
    }

    public void setBpm(double bpm) {
        this.bpm = bpm;
    }

    public int getBeat() {
        return beat;
    }

    public void setBeat(int beat) {
        this.beat = beat;
    }

    public int getNoteValue() {
        return noteValue;
    }

    public void setNoteValue(int noteValue) {
        this.noteValue = noteValue;
    }

    public int getSilence() {
        return silence;
    }

    public void setSilence(int silence) {
        this.silence = silence;
    }

    public double getBeatSound() {
        return beatSound;
    }

    public void setBeatSound(double beatSound) {
        this.beatSound = beatSound;
    }

    public double getSound() {
        return sound;
    }

    public void setSound(double sound) {
        this.sound = sound;
    }

    public int getTick() {
        return tick;
    }
}