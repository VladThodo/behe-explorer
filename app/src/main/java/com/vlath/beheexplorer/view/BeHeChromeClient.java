/*
 Copyright 2016 Vlad Todosin
*/
package com.vlath.beheexplorer.view;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.vlath.beheexplorer.controllers.TabManager;


public class BeHeChromeClient extends WebChromeClient {
    private ProgressBar PBar;
    private ActionBarActivity ACTIVITY;
    private BeHeView mWeb;

    public BeHeChromeClient(ProgressBar pBar, BeHeView web) {
        super();
        mWeb = web;
        PBar = pBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (mWeb.isCurrentTab()) {
            if (newProgress < 100) {
                PBar.setVisibility(View.VISIBLE);
                PBar.setProgress(view.getProgress());
            } else {
                PBar.setProgress(0);
                PBar.setVisibility(View.GONE);
            }
        }
    }
    @Override
    public void onReceivedTitle(WebView view,String title){
        super.onReceivedTitle(view,title);
        TabManager.updateTabView();
    }

}