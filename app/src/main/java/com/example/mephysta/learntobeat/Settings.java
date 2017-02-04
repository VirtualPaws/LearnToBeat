package com.example.mephysta.learntobeat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.view.View.OnClickListener;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.example.mephysta.learntobeat.MainActivity.isPlaying;

/**
 * Created by Mephysta on 19.01.2017.
 */

public class Settings extends Activity {
    private GoogleApiClient client;
    public int bpmSettings = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        // OnclickListener
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.save:
                        Log.d("", "Clicked Save: " + v.getId());
                        saveBPM();
                        break;
                    default:
                        Log.d("", "Unknown button: " + v.getId());
                }
            }
        };

        Button btnSave = (Button) findViewById(R.id.save);
        btnSave.setOnClickListener(listener);
        //number picker
        NumberPicker bpmValue = (NumberPicker) findViewById(R.id.bpm_picker);
        bpmValue.setMinValue(50);
        bpmValue.setMaxValue(120);
        bpmValue.setValue(80);
        bpmValue.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
               Log.d("bpm picker",":" + newVal);
               bpmSettings = newVal;
            }
        });

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
                .setName("Settings Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    /*
   * Startet das Level neu.
   */
    private void saveBPM(){
        Intent intent = new Intent(this, GameMenu.class);
        startActivity(intent);
        finish();
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
