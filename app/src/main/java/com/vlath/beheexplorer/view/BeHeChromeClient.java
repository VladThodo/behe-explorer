package com.vlath.beheexplorer.view;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
       if(newProgress == 100){
           PBar.setProgress(0);
       }
      else {
           PBar.setProgress(newProgress);
       }
    }
    @Override
    public void onReceivedIcon(WebView view,Bitmap icon){
        ICON = new PreferenceUtils(ACTIVITY).getDisplayPageIcon();
        if(ICON){
            ACTIVITY.getSupportActionBar().setHomeAsUpIndicator(new BitmapDrawable(Bitmap.createScaledBitmap(icon,22,22,false)));
        }
        else{
            ACTIVITY.getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        }
    }
}
