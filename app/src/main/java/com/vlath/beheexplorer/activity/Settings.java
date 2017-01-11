/*
 Copyright 2016 Vlad Todosin
*/
package com.vlath.beheexplorer.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.fragment.PreferenceFragment;
import com.vlath.beheexplorer.utils.PreferenceUtils;


@SuppressWarnings("deprecation")
public class Settings extends ActionBarActivity {
	@Override
	public void onCreate(Bundle saved){
		super.onCreate(saved);
		setContentView(R.layout.settings_activity);
		Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(bar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getResources().getString(R.string.action_settings));
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(PreferenceUtils.getTheme(this))));
		FragmentManager mFragmentManager = getFragmentManager();
		FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
		PreferenceFragment fragment = new PreferenceFragment();
		mFragmentTransaction.replace(R.id.contentMain, fragment);
		mFragmentTransaction.commit();

	}
}
