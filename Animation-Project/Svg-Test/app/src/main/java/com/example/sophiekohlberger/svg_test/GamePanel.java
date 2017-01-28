package com.example.sophiekohlberger.svg_test;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {
    // Bonbonfarben

    // bitmap
    private static int[] BONBON_DRAWABLES = new int[]{R.drawable.single_bonbon_o, R.drawable.single_bonbon_g, R.drawable.single_bonbon_b};
    private static final int LIMIT_BONBONS_ON_SCREEN = 3;

    public static int WIDTH = 382;
    public static int HEIGHT = 455;
    private MainThread thread;
    private Background bg;

    private long bonbonStartTime;
    private ArrayList<Bonbon> bonbons;

    private int currentDrawablePointer = 0;

    public GamePanel(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }
    public GamePanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    public ArrayList<Bonbon> getList() {
        return bonbons;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    //stop thread
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        // to prevent infinite loop
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
                thread=null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    //start thread
    public void surfaceCreated(SurfaceHolder holder) {
        //as bitmap
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bg);
        //as svg
        Bitmap bitmap = getBitmap(getContext(), R.drawable.ic_bg, true);
        bg = new Background(bitmap);

        bonbons = new ArrayList<>();
        bonbonStartTime = System.nanoTime();

        // start the game loop
        if(thread == null || !thread.isAlive()) {
            thread = new MainThread(getHolder(), this);
            thread.setRunning(true);
            thread.start();
        }
    }

    public void update() {
        //add bonbons on timer, in ms
        long bonbonTime = (System.nanoTime()- bonbonStartTime) / 1000000;
        int bonbonDensity = 2500; //4500

        //show next bonbon color
        if(bonbonTime > bonbonDensity && bonbons.size() < LIMIT_BONBONS_ON_SCREEN){
            System.out.println("making bonbon");

            int drawableRes = BONBON_DRAWABLES[getCurrentDrawablePointer()];
            //bitmap
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),drawableRes);

            //dpi
            Bonbon bonbon = new Bonbon(bitmap, WIDTH - Utils.convertDpToPx(60), Utils.convertDpToPx(22), bitmap.getWidth(), bitmap.getHeight());
            bonbons.add(bonbon);

            //reset timer
            bonbonStartTime = System.nanoTime();
        }

        //loop through every bonbon and remove those which are not shown anymore
        Iterator<Bonbon> bonbonIterator = bonbons.iterator();
        while(bonbonIterator.hasNext()) {
            Bonbon bonbon = bonbonIterator.next();
            bonbon.update();

            if(bonbon.getX() + bonbon.getWidth() <= 0) {
                bonbonIterator.remove();
            }
        }
    }

    private int getCurrentDrawablePointer() {
        int currentPointer = currentDrawablePointer % BONBON_DRAWABLES.length;
        currentDrawablePointer++;

        return currentPointer;
    }

    @Override
    public void draw(Canvas canvas) {
        //bg same size as screen
        final float scaleFactorX = getWidth() / (WIDTH * 1.f);
        final float scaleFactorY = getHeight() / (HEIGHT * 1.f);

        if (canvas != null) {
            final int savedState = canvas.save(); //before scaling
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);

            //draw bonbons
            for (Bonbon m : bonbons) {
                m.draw(canvas);
            }

            canvas.restoreToCount(savedState); //return back to the same state (unscale), otherwise it would keep scaling
        }
    }

    //svg to bitmap
    private static Bitmap getBitmap(Context context, int drawableId, boolean setSize) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            return getBitmap((VectorDrawable) drawable, setSize);
        } else {
            throw new IllegalArgumentException("unsupported drawable type");
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Bitmap getBitmap(VectorDrawable vectorDrawable, boolean setSize) {
        int height = vectorDrawable.getIntrinsicHeight();
        int width = vectorDrawable.getIntrinsicWidth();

        if (setSize) {
            HEIGHT = height;
            WIDTH = width;
        }

        Bitmap bitmap = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    public void removeBonbon(int index) {
        bonbons.remove(index);
    }
}