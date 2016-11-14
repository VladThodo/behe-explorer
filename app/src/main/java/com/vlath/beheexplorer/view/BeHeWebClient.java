/*
 Copyright 2016 Vlad Todosin
*/
package com.vlath.beheexplorer.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.webkit.*;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.activity.BeHeActivity;
import com.vlath.beheexplorer.database.DbItem;
import com.vlath.beheexplorer.database.HistoryDatabase;
import com.vlath.beheexplorer.utils.PreferenceUtils;

public class BeHeWebClient extends WebViewClient {
    private BeHeActivity ACTIVITY;
    boolean ICON;
    private EditText TEXT;
    public BeHeWebClient(BeHeActivity activity,EditText textView){
      super();
      TEXT = textView;
      ICON = new PreferenceUtils(activity).getDisplayPageIcon();
      ACTIVITY = activity;
    }
    @Override
    public void onPageFinished(WebView view,String url) {
        String str;
       if(view.getUrl().contains("https://")) {
           str = view.getUrl().toString().replace("https://", "<font color='#228B22'>https://</font>");
           TEXT.setText(Html.fromHtml(str), TextView.BufferType.SPANNABLE);

       }
       else{
           if(view.getFavicon() != null){
               Bitmap original = view.getFavicon();
               Bitmap b = Bitmap.createScaledBitmap(original, 22, 22, false);
               Drawable d = new BitmapDrawable(ACTIVITY.getResources(), b);
               TEXT.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
               TEXT.setText(view.getUrl());
           }
           else{
               TEXT.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
               TEXT.setText(view.getUrl());
           }
       }
       if(!ACTIVITY.getBeHeView().isPrivate()) {
           DbItem dbItem = new DbItem(url, view.getTitle());
           HistoryDatabase db = new HistoryDatabase(ACTIVITY);
           db.addItem(dbItem);
       }
        else{
       }
       }
}

