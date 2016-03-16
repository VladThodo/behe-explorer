/*
 Copyright 2016 Vlad Todosin
*/

package com.vlath.beheexplorer.adapters;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Gravity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.utils.IconUtility;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TabAdapter extends ArrayAdapter<String> {
    View rowView;
    List<String> names;
    Context context;
    private static LayoutInflater inflater = null;

    public TabAdapter(Activity mainActivity, List<String> name, ListView list) {
        super(mainActivity, R.layout.tab_item,name);
        context = mainActivity;
        names = name;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public class Holder {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            Holder holder = new Holder();
            HashMap<String, String> map = new HashMap<>();
            File toRead = new File(context.getApplicationContext().getFilesDir(), "bookmarks.oi");
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toRead));
                Object obj = ois.readObject();
                ois.close();
                map = (HashMap<String, String>) obj;
            } catch (Exception e){}
            rowView = inflater.inflate(R.layout.tab_item, null);
            holder.tv = (TextView) rowView.findViewById(R.id.textView1);
            holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
            holder.tv.setText(names.get(position));
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.history);
            IconUtility utility = new IconUtility(holder.img, context, map.get(names.get(position)), icon);
            Void[] u = null;
            utility.execute(u);
            return rowView;
        } catch (Exception e) {

        }
        return rowView;
    }
}
