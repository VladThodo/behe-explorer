/*
 Copyright 2016 Vlad Todosin
*/
package com.vlath.beheexplorer.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.Html;
import android.webkit.*;
import android.widget.EditText;
import android.widget.TextView;
import com.vlath.beheexplorer.controllers.TabManager;
import com.vlath.beheexplorer.database.DbItem;
import com.vlath.beheexplorer.database.HistoryDatabase;

public class BeHeWebClient extends WebViewClient {
    private EditText TEXT;
    Activity act;
    BeHeView mainView;
    boolean priv;
    public BeHeWebClient(EditText textView,Activity ac,boolean privat,BeHeView view){
      super();
      TEXT = textView;
      act = ac;
      priv = privat;
      mainView = view;
    }


    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
       if(mainView.isCurrentTab()) {
           String str;
           if (view.getUrl().contains("https://")) {
               str = view.getUrl().toString().replace("https://", "<font color='#228B22'>https://</font>");
               TEXT.setText(Html.fromHtml(str), TextView.BufferType.SPANNABLE);
               TEXT.clearFocus();
           } else {
               if (!view.getUrl().contains("file")) {
                   TEXT.setText(view.getUrl());
               }
           }
       }
     }

    @Override
    public void onPageFinished(WebView view,String url) {
       if(mainView.isCurrentTab()) {
           String str;
           if (priv) {
           } else {
               DbItem dbItem = new DbItem(url, view.getTitle());
               HistoryDatabase db = new HistoryDatabase(act);
               db.addItem(dbItem);
           }
           if (view.getUrl().contains("https://")) {
               str = view.getUrl().toString().replace("https://", "<font color='#228B22'>https://</font>");
               TEXT.setText(Html.fromHtml(str), TextView.BufferType.SPANNABLE);
               TEXT.clearFocus();
           } else {
               if (!view.getUrl().contains("file")) {
                   TEXT.setText(view.getUrl());
               }
           }
       }
        TabManager.updateTabView();
    }
}


