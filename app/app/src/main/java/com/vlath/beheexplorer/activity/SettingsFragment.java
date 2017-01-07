package com.vlath.beheexplorer.activity;


import android.os.Bundle;
import android.preference.PreferenceFragment;
import com.vlath.beheexplorer.R;

public class SettingsFragment extends PreferenceFragment {


	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
	}

}
