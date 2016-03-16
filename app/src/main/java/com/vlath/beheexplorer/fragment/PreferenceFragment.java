package com.vlath.beheexplorer.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.widget.Toast;

import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.database.HistoryDatabase;

/**
 * Copyright 2016 Vlad Todosin
 */
public class PreferenceFragment extends android.preference.PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.settings);
        Preference mPref = (Preference) findPreference("clear");
        mPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                HistoryDatabase mDb = new HistoryDatabase(getActivity());
                mDb.clearAllItems();
                Toast.makeText(getActivity(),getString(R.string.historytast),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

}
