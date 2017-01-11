package com.vlath.beheexplorer.activity;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.utils.ThemeUtils;

/**
 * Copyright (c) 2016 Vlad Todosin
 */
public class SettingsActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference);
        Toolbar bar = (Toolbar)findViewById(R.id.settingsbar);
        setSupportActionBar(bar);
        setTitle(getResources().getString(R.string.action_settings));
        ThemeUtils theme = new ThemeUtils(this);
        theme.setTheme();
        getFragmentManager().beginTransaction().replace(R.id.layout, new SettingsFragment()).commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
