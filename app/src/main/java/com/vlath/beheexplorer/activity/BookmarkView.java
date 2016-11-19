/* Copyright (c) Vlad Todosin */

package com.vlath.beheexplorer.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.adapters.BookAdapter;
import com.vlath.beheexplorer.utils.PreferenceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class BookmarkView extends Fragment {
    ArrayList<String> m = new ArrayList<>();
    ArrayList<String> u = new ArrayList<>();
    Activity mActivity;
    GridView mGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        View v = inflater.inflate(R.layout.bookmarkv, container, false);
        mGrid = (GridView) v.findViewById(R.id.gridview);
        readBookmarks();
        BookAdapter adt = new BookAdapter(v.getContext(), m, u);
        mGrid.setAdapter(adt);
        adt.notifyDataSetChanged();
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent im = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                im.putExtra("url", u.get(i));
                im.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(im);
            }
        });
        return v;
    }

    public void showBookmarks(Activity act,ArrayList<String> e,ArrayList<String> c) {
        mActivity = act;
        m.addAll(e);
        u.addAll(c);
        BookAdapter adt = new BookAdapter(mActivity.getApplicationContext(), m, u);
        mGrid.setAdapter(adt);
        adt.notifyDataSetChanged();
        mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent im = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                im.putExtra("url", u.get(i));
                im.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(im);
            }
        });
    }

    public void readBookmarks() {
        try {

            File toRead = new File(mActivity.getApplicationContext().getFilesDir(), "bookmarks.oi");
            if (toRead.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toRead));
                Object obj = ois.readObject();
                ois.close();
                ois.close();
                HashMap<String, String> mHash = (HashMap<String, String>) obj;
                m.addAll(mHash.keySet());
                u.addAll(mHash.values());
            }
        } catch (Exception ee) {

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
