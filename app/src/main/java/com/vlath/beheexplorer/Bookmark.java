package com.vlath.beheexplorer;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Administrator on 07.02.2016.
 */
public  class Bookmark {
 private Bitmap PAGE_ICON;
 private String PAGE_NAME;
    public Bookmark(String name, Bitmap icon){
        PAGE_ICON = icon;
        PAGE_NAME = name;
    }
    public Bitmap getIcon(){
        return PAGE_ICON;
    }
    public String getName(){
        return PAGE_NAME;
    }
}
