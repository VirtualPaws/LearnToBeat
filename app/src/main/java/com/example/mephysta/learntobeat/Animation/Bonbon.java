package com.example.mephysta.learntobeat.Animation;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Bonbon {
    private int speed;
    private Bitmap sprite;
    protected int x;
    protected int y;
    protected int width;
    protected int height;

    public Bonbon(Bitmap res, int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
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

    public Rect findLocation() {
        Rect rect= new Rect();

        rect.left = x;
        rect.top = y;
        rect.right = rect.left + width;
        rect.bottom =rect.top + height;

        return rect;
    }
}
