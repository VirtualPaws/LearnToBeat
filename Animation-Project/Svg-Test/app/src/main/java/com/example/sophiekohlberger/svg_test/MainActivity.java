package com.example.sophiekohlberger.svg_test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GamePanel view = (GamePanel) findViewById(R.id.a);

        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);

       final ImageView imgAnim = (ImageView) findViewById(R.id.anim);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet sets = new AnimationSet(false);
                sets.addAnimation(animRotate);
                sets.addAnimation(animTranslate);
                imgAnim.startAnimation(sets);

            }
        });
        imgAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet sets = new AnimationSet(false);
                sets.addAnimation(animRotate);
                sets.addAnimation(animTranslate);
                v.startAnimation(sets);

            }
        });
    }
}