package com.example.mephysta.learntobeat.Animation;

import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;

// Calculations of collisions

public class UiUtils {
    //defines how much of the hit area is reduced for the paw and bonbon
    private static final float PAW_RECT_WIDTH_FACTOR = 0.4F;
    private static final float BONBON_RECT_WIDTH_FACTOR = 0.15F;

    //return the hit area needed for the collision of a paw
    public static Rect getPawHitRect(ImageView pawImageView) {
        Rect locationRect = getLocationOnScreen(pawImageView);
        Rect hitRect = new Rect();

        // We only reduce the paw hit-width on the left-hand side as there is the larger amount of unseen hitting space
        int widthReduction = (int) (pawImageView.getWidth() * PAW_RECT_WIDTH_FACTOR);
        hitRect.left = locationRect.left + widthReduction;
        hitRect.top = locationRect.top;
        hitRect.right = locationRect.right;
        hitRect.bottom = locationRect.bottom;

        return hitRect;
    }

    //return the hit area needed for the collision of a bonbon
    public static Rect getBonbonHitRect(Bonbon bonbon) {
        Rect hitRect = new Rect();
        Rect locationRect = findBonbonLocation(bonbon);

        int widthReduction = (int) (bonbon.getWidth() * BONBON_RECT_WIDTH_FACTOR);
        hitRect.left = locationRect.left + widthReduction;
        hitRect.top = locationRect.top;
        hitRect.right = locationRect.right - widthReduction;
        hitRect.bottom = locationRect.bottom;

        return hitRect;
    }

    // return bonbon coordinates into rectangle
    private static Rect findBonbonLocation(Bonbon bonbon) {
        Rect rect= new Rect();

        rect.left = bonbon.getX();
        rect.top = bonbon.getY();
        rect.right = rect.left + bonbon.getWidth();
        rect.bottom =rect.top + bonbon.getHeight();

        return rect;
    }


    //Get location of a specific view on the screen
    public static Rect getLocationOnScreen(View view) {
        Rect rectangle = new Rect();

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        rectangle.left = location[0];
        rectangle.top = location[1];
        rectangle.right = location[0] + view.getWidth();
        rectangle.bottom = location[1] + view.getHeight();

        return rectangle;
    }
    /*
     * Check if two rectangles intersect
     */
    public static boolean isCollision(Rect bon, Rect paw)
    {
        return Rect.intersects(bon, paw);
    }
}