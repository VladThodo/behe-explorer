package com.vlath.beheexplorer.utils;

import android.content.Context;
import android.os.AsyncTask;

import com.vlath.beheexplorer.activity.MainActivity;

/**
 * Created by todo on 07.01.2017.
 */
public class ClearCache extends AsyncTask<Void, Void,Void> {
    private Context mContext;
    public ClearCache(Context context){
        mContext = context;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        MainActivity.trimCache(mContext);
        return null;
    }
}
