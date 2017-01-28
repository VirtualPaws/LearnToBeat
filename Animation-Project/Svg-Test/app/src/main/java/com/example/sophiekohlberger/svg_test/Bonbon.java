package com.example.sophiekohlberger.svg_test;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bonbon {
    private int speed;
    private Bitmap sprite;

    private int x;
    private int y;
    private int width;
    private int height;

    public Bonbon(Bitmap res, int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        width = w;
        height = h;

        speed = 15;
        sprite = res;
    }

    //make the bonbon move
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /*public void reset(){
        this.x = width-50;
    }*/
}
