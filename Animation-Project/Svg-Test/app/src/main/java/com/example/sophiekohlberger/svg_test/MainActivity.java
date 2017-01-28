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
    private GamePanel gamePanelSurfaceView;
    private ImageView pawImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gamePanelSurfaceView = (GamePanel) findViewById(R.id.activity_main_game_panel);
        pawImageView = (ImageView) findViewById(R.id.activity_main_paw);

        //load animation
        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);

        //click on paw and background
        gamePanelSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet sets = new AnimationSet(false);
                sets.addAnimation(animRotate);
                sets.addAnimation(animTranslate);

                sets.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    //Check if paw hit-area collided with a bonbon. If yes, remove the bonbon from the surface view
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        Rect pawRect = UiUtils.getPawHitRect(pawImageView);

                        ArrayList<Bonbon> bonbons = gamePanelSurfaceView.getList();
                        for(int count=0; count<bonbons.size(); count++){
                            Rect bonbonRect = UiUtils.getBonbonHitRect(bonbons.get(count));

                            boolean hasCollided = UiUtils.isCollision(bonbonRect, pawRect);
                            if(hasCollided) {
                                gamePanelSurfaceView.removeBonbon(count);
                                break;
                            }
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                pawImageView.startAnimation(sets);
            }

        });
    }
}