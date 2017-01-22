package com.example.sophiekohlberger.svg_test;

import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Background {
    private Bitmap image;
    private int x,y;

    public Background(Bitmap res) {
        image = res;
    }

    public void draw(Canvas canvas){

        canvas.drawBitmap(image, x, y, null);
    }
}
