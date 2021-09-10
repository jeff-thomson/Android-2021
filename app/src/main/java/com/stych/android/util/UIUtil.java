package com.stych.android.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class UIUtil {
    public static void setViewsEnabled(boolean enabled, View... views) {
        if (views != null) {
            for (View view : views) {
                view.setEnabled(enabled);
            }
        }
    }

    public static void setViewsVisible(int enabled, View... views) {
        if (views != null) {
            for (View view : views) {
                view.setVisibility(enabled);
            }
        }
    }

    public static int getDeviceWidth(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
