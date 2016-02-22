package com.vlath.beheexplorer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.webkit.*;
import android.widget.AutoCompleteTextView;

import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.activity.BeHeActivity;
import com.vlath.beheexplorer.utils.PreferenceUtils;

public class BeHeWebClient extends WebViewClient {
    private BeHeActivity ACTIVITY;
    boolean ICON;
    private AutoCompleteTextView TEXT;
    public BeHeWebClient(BeHeActivity activity,AutoCompleteTextView textView){
      super();
      TEXT = textView;
      ICON = new PreferenceUtils(activity).getDisplayPageIcon();
      ACTIVITY = activity;
    }
    @Override
    public void onPageFinished(WebView view,String url){
      TEXT.setText(url);
    }
}

