package com.vlath.beheexplorer.activity;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vlath.beheexplorer.R;

@SuppressWarnings("deprecation")
public class Settings extends PreferenceActivity  implements OnSharedPreferenceChangeListener {

	Toolbar bar;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.settings);
       
     
        
	}
	 @Override
	    protected void onPostCreate(Bundle savedInstanceState) {
	        super.onPostCreate(savedInstanceState);

	        LinearLayout root = (LinearLayout)findViewById(android.R.id.list).getParent().getParent().getParent();
	         bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
		 SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		 String color = settings.getString("color", "#FFFFFF");
		 bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
		 root.addView(bar, 0); // insert at top
	        bar.setNavigationOnClickListener(new View.OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                finish();
	            }
	        });
	    }
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String color = settings.getString("color", "#FFFFFF");
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
		
	
		
	}
@Override
	public void onResume() {
	super.onResume();
	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
	String color = settings.getString("color", "#FFFFFF");
	bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
	}
}
