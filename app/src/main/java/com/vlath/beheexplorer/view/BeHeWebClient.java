/*
 Copyright 2016 Vlad Todosin
*/
package com.vlath.beheexplorer.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
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
    private EditText TEXT;
    Activity act;
    public BeHeWebClient(EditText textView,Activity ac){
      super();
      TEXT = textView;
      act = ac;
    }
    @Override
    public void onPageFinished(WebView view,String url) {
        String str;
        if (view.getUrl().contains("https://")) {
            str = view.getUrl().toString().replace("https://", "<font color='#228B22'>https://</font>");
            TEXT.setText(Html.fromHtml(str), TextView.BufferType.SPANNABLE);
        }


        DbItem dbItem = new DbItem(url, view.getTitle());
        HistoryDatabase db = new HistoryDatabase(act);
        db.addItem(dbItem);

    }
       }


