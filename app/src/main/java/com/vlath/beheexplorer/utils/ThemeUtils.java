/*
 Copyright 2016 Vlad Todosin
*/
package com.vlath.beheexplorer.utils;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.vlath.beheexplorer.activity.BeHeActivity;
import com.vlath.beheexplorer.activity.MainActivity;

public class ThemeUtils {
   private static BeHeActivity THEME_ACTIVITY;
    public ThemeUtils(BeHeActivity activity){
        THEME_ACTIVITY = activity;
    }
    public  void setTheme() {
        PreferenceUtils prefs = new PreferenceUtils(THEME_ACTIVITY.getApplicationContext());
        THEME_ACTIVITY.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(prefs.getTheme())));
    }
    public   void setIncognitoTheme(){
        THEME_ACTIVITY.getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2F4F4F")));
    }

}
