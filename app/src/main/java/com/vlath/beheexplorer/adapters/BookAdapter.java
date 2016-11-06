/* Copyright (c) 2016 Vlad Todosin */
package com.vlath.beheexplorer.adapters;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.*;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.vlath.beheexplorer.R;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String>    names;
    Activity main;
    public BookAdapter(Context c, ArrayList<String> etc,Activity mActivity) {
        mContext = c;
        names = etc;
        main = mActivity;
    }


    public int getCount() {
        return names.size();
    }


    public Object getItem(int position) {
        return null;
    }


    public long getItemId(int position) {
        return position;
    }

    public View getView(int position,View convertView, ViewGroup parent) {
        View myView;
        if (convertView == null) {
            LayoutInflater li = ((Activity) mContext).getLayoutInflater();
            myView = li.inflate(R.layout.bookmarkitem, null);
            ImageView img = (ImageView) myView.findViewById(R.id.img2);
            TextView txt = (TextView) myView.findViewById(R.id.txt2);
            ColorGenerator gen = ColorGenerator.MATERIAL;
            int col = gen.getRandomColor();
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(String.valueOf(names.get(position).charAt(0)), col);
            img.setImageDrawable(drawable);
            txt.setText(names.get(position));
        } else {
            myView = (View) convertView;
        }


      return  myView;
    }
}
