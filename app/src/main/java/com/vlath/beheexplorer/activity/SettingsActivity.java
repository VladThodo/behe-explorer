package com.vlath.beheexplorer.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.utils.PreferenceHelper;
import com.vlath.beheexplorer.utils.ThemeUtils;

/**
 * Copyright (c) 2016 Vlad Todosin
 */
public class SettingsActivity extends ActionBarActivity {
    private MenuItem itm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        ThemeUtils theme = new ThemeUtils(this);
        theme.setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference);
        Toolbar bar = (Toolbar)findViewById(R.id.settingsbar);
        setSupportActionBar(bar);
        setTitle(getResources().getString(R.string.action_settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getFragmentManager().beginTransaction().replace(R.id.layout, new SettingsFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }
    @Override
    public void onBackPressed() {
        if(PreferenceHelper.getIsBrowser() || PreferenceHelper.getIsLook()){
            setTitle(getResources().getString(R.string.action_settings));
            getFragmentManager().beginTransaction().replace(R.id.layout, new SettingsFragment()).commit();
            PreferenceHelper.setIsBrowserScreen(false);
            PreferenceHelper.setIsLookScreen(false);
        }
        else{
            Intent in = new Intent(this,MainActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(in);
        }
    }
}
