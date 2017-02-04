package com.example.mephysta.learntobeat.Animation;

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
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.mephysta.learntobeat.MainActivity;
import com.example.mephysta.learntobeat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    // bitmap
    //private static int[] BONBON_DRAWABLES = new int[]{R.drawable.single_bonbon_o, R.drawable.single_bonbon_g, R.drawable.single_bonbon_b};
    //private static int[] STICK_DRAWABLES = new int[]{R.drawable.bonbon_stick_o, R.drawable.bonbon_stick_g, R.drawable.bonbon_stick_b};

    // TODO bonbons sollten alle die gleiche Größe haben
    private static int[] BONBON_DRAWABLES = new int[]{R.drawable.single_bonbon_o};
    private static int[] STICK_DRAWABLES = new int[]{R.drawable.bonbon_stick_o};

    private static final int LIMIT_BONBONS_ON_SCREEN = 10;

    public static int WIDTH = 382;
    public static int HEIGHT = 455;
    private MainThread thread;
    private Background bg;

    private long bonbonStartTime;
    //private ArrayList<Bonbon> bonbons;
    public static Map<Integer, Bonbon> tick2Bonbon;
    private static int bonbonCounter = 0;

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

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * Stops thread.
     * @param holder
     */
    @Override
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

    /**
     * Starts thread.
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //as bitmap
        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.bg);
        //as svg
        Bitmap bitmap = getBitmap(getContext(), R.drawable.ic_bg, true);
        bg = new Background(bitmap);

        tick2Bonbon = new HashMap<>();
        //bonbonStartTime = System.nanoTime();
    }

    /**
     * Starts the animation by creating and starting a new MainThread.
     */
    public void startAnimation(){
        setVisibility(VISIBLE);
        bonbonStartTime = System.nanoTime();
        bonbonCounter = 0;
        if(thread == null || !thread.isAlive()) {
            thread = new MainThread(getHolder(), this);
            thread.setRunning(true);
            thread.start();
        }
    }

    /**
     * Stops the animation by stopping the current running MainThread.
     */
    public void stopAnimation(){
        try {
            thread.setRunning(false);
            thread.join();
            thread=null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        //add bonbons on timer, in ms
        long bonbonTime = (System.nanoTime()- bonbonStartTime) / 1000000;
        int bonbonDensity = (int)MainActivity.timeBetween2Beats * 1000; //4500

        //show next bonbon color
        if(bonbonTime > bonbonDensity && tick2Bonbon.size() < LIMIT_BONBONS_ON_SCREEN && bonbonCounter < MainActivity.beat2time.size()){
            //Log.d("GamePanel","Update: making bonbon");

            int currentDrawablePointer = getCurrentDrawablePointer();

            int stickDrawableRes = STICK_DRAWABLES[currentDrawablePointer];
            Bitmap stickBitmap = BitmapFactory.decodeResource(getResources(),stickDrawableRes);

            int bonbonDrawableRes = BONBON_DRAWABLES[currentDrawablePointer];
            Bitmap bonbonBitmap = BitmapFactory.decodeResource(getResources(),bonbonDrawableRes);

            //dpi
            Bonbon bonbon = new Bonbon(bonbonBitmap, stickBitmap, WIDTH - Utils.convertDpToPx(60), Utils.convertDpToPx(22), bonbonBitmap.getWidth(), bonbonBitmap.getHeight());
            //bonbons.add(bonbon);
            tick2Bonbon.put(bonbonCounter, bonbon);
            bonbonCounter++;

            //reset timer
            bonbonStartTime = System.nanoTime();
        }

        //loop through every bonbon and remove those which are not shown anymore
        Iterator<Map.Entry<Integer, Bonbon>> iterator = tick2Bonbon.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, Bonbon> entry = iterator.next();
            Bonbon bonbon = entry.getValue();
            bonbon.update();

            if(bonbon.getX() + bonbon.getWidth() <= 0){
                iterator.remove();
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
            for(int key : tick2Bonbon.keySet()){
                tick2Bonbon.get(key).draw(canvas);
            }

            canvas.restoreToCount(savedState); //return back to the same state (unscale), otherwise it would keep scaling
        }
    }

    /**
     * svg to bitmap
     * @param context
     * @param drawableId
     * @param setSize
     */
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

    /**
     * Returns a bitmap.
     * @param vectorDrawable
     * @param setSize
     * @return
     */
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

    /**
     * Removes a bonbon from its stick.
     * @param index -> index of bonbon to remove in list
     */
    public void removeBonbonFromStick(int index) {
        tick2Bonbon.get(index).removeBonbonImage();
    }
}