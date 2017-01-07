/*
 Copyright 2016 Vlad Todosin
*/
package com.vlath.beheexplorer.utils;

import android.content.Context;
import android.preference.PreferenceManager;

public class PreferenceUtils {
private static PreferenceManager MANAGER;
private static Context mContext;
   public PreferenceUtils(Context context){
   mContext = context;
    }
   public static int getSearchEngine(){
   String searchEngine;
   searchEngine = MANAGER.getDefaultSharedPreferences(mContext).getString("search","1");
   int e = Integer.parseInt(searchEngine);
   return e;
    }
    public  int getTextSize(){
      return Integer.parseInt(MANAGER.getDefaultSharedPreferences(mContext).getString("size","18"));
    }
    public static  String getTheme(Context context){
    String color = MANAGER.getDefaultSharedPreferences(context).getString("color", "#A9A9A9");
    return color;
    }
    public   String getTheme(){
        String color = MANAGER.getDefaultSharedPreferences(mContext).getString("color", "#A9A9A9");
        return color;
    }
    public  String getHomePage(){
       return MANAGER.getDefaultSharedPreferences(mContext).getString("home_page","https://www.google.com");
    }
    public  boolean getJavaEnabled(){
    return MANAGER.getDefaultSharedPreferences(mContext).getBoolean("java", true);
    }

    public  boolean getPluginsEnabled(){
    return MANAGER.getDefaultSharedPreferences(mContext).getBoolean("plugins", true);
    }
    public  boolean getCacheEnabled(){
    return MANAGER.getDefaultSharedPreferences(mContext).getBoolean("cache", false);
    }
    public static boolean getDisplayPageIcon(){
    return MANAGER.getDefaultSharedPreferences(mContext).getBoolean("icon",true);
    }

}
