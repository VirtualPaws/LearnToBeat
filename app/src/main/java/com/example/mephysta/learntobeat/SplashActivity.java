package com.example.mephysta.learntobeat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sJantzen on 13.12.2016.
 */

public class SplashActivity  extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, GameMenu.class);
        startActivity(intent);
        finish();
    }
}
