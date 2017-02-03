package com.example.mephysta.learntobeat.Animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bonbon {
    private int speed;
    private Bitmap bonbonSprite;
    private Bitmap stickSprite;

    private int x;
    private int y;
    private int width;
    private int height;

    public Bonbon(Bitmap bonbon, Bitmap bonbonStick, int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        width = w;
        height = h;

        speed = 15;
        bonbonSprite = bonbon;
        stickSprite = bonbonStick;
    }

    //make the bonbon move
    public void update()
    {
        x -= speed;
    }

    public void draw(Canvas canvas)
    {
        try{
            // We always draw the stick
            canvas.drawBitmap(stickSprite,x,y,null);

            if(bonbonSprite != null) {
                canvas.drawBitmap(bonbonSprite, x, y, null);
            }

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

    // removes the bonbon from images
    public void removeBonbonImage() {
        if(bonbonSprite != null) {
            bonbonSprite.recycle();
            bonbonSprite = null;
        }
    }
}
