package com.example.sophiekohlberger.svg_test;

import android.graphics.Bitmap;
import android.graphics.Canvas;
//import android.view.animation.Animation;

import java.util.Random;


public class Bonbon extends GameObject {
    private int speed;
    private Bitmap sprite;

    public Bonbon(Bitmap res, int x, int y, int w, int h) {
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        speed = 7;
        sprite = res;
    }

    public void update()
    {
        x -= speed;
    }

    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(sprite,x,y,null);
        }catch(Exception e){}
    }

    //???
    @Override
    public int getWidth()
    {
        //offset slightly for more realistic collision detection
        return width - 10;
    }
}
