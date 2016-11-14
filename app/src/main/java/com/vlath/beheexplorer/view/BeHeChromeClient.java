/*
 Copyright 2016 Vlad Todosin
*/
package com.vlath.beheexplorer.view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.activity.BeHeActivity;
import com.vlath.beheexplorer.utils.PreferenceUtils;


public class BeHeChromeClient extends WebChromeClient {
    boolean ICON;
    private ProgressBar PBar;
    private BeHeActivity ACTIVITY;
    public BeHeChromeClient(BeHeActivity activity,ProgressBar pBar){
    super();
        ACTIVITY = activity;
        PBar = pBar;
        ICON = new PreferenceUtils(activity).getDisplayPageIcon();
    }
    @Override
    public void onProgressChanged(WebView view,int newProgress){
       if(newProgress < 100){
           PBar.setVisibility(View.VISIBLE);
           PBar.setProgress(view.getProgress());
       }
      else {
           PBar.setProgress(0);
           PBar.setVisibility(View.GONE);
       }
    }
    @Override
    public void onReceivedIcon(WebView view,Bitmap icon){

    }
}
