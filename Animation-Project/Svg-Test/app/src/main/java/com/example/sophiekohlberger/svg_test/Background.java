package com.example.sophiekohlberger.svg_test;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by sophiekohlberger on 27.11.16.
 */

public class Background {
    private Bitmap image;
    private int x,y,dx;

    public Background(Bitmap res) {
        image = res;
        dx = GamePanel.MOVESPEED;
    }

    public void update(){
        x+=dx;
        // reset image, thats completely off the screen
        if(x<-GamePanel.WIDTH){
            x=0;
        }

    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(image, x, y, null);
        //if part of the image is off of the screen, compensate through 2nd image
        if(x<0){
            canvas.drawBitmap(image, x+GamePanel.WIDTH,y,null);
        }
    }

    public void setVector(int dx){
        this.dx=dx;
    }
}
