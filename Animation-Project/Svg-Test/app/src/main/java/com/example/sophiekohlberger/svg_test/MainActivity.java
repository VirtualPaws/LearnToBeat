package com.example.sophiekohlberger.svg_test;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //get location of the paw
    private Rect getLocationOnScreen(View imgAnim) {
        Rect mRect = new Rect();
        int[] location = new int[2];

        imgAnim.getLocationOnScreen(location);

        mRect.left = location[0];
        mRect.top = location[1];
        mRect.right = location[0] + imgAnim.getWidth();
        mRect.bottom = location[1] + imgAnim.getHeight();

        return mRect;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final GamePanel view = (GamePanel) findViewById(R.id.a);
        final ArrayList<Bonbon> bonbons = new ArrayList<Bonbon>();

        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        final ImageView imgAnim = (ImageView) findViewById(R.id.anim);

        //click on paw and background
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet sets = new AnimationSet(false);
                sets.addAnimation(animRotate);
                sets.addAnimation(animTranslate);

                sets.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //get location of the imageview
                        int[] location = new int[2];
                        imgAnim.getLocationOnScreen(location);
                        int x = location[0];
                        int y = location[1];
                        Rect paw = getLocationOnScreen(imgAnim);
                        System.out.println("PAW: " + paw);

                        for(int b=0; b<bonbons.size(); b++){
                            Rect bon = bonbons.get(b).findLocation();
                            System.out.println("BONBON: " + bon);

                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imgAnim.startAnimation(sets);
            }

        });
    }
}