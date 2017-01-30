package com.example.sophiekohlberger.svg_test;

import android.content.res.Resources;
import android.util.DisplayMetrics;

//provides helper functions
public class Utils {
    public static int convertDpToPx(int dp) {
        return Math.round(dp * (Resources.getSystem().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}