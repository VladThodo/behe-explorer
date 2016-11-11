/* Copyright (c) Vlad Todosin */

package com.vlath.beheexplorer.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

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

public class BookmarkView extends ActionBarActivity {
    ArrayList<String> m = new ArrayList<>();
    ArrayList<String> u = new ArrayList<>();
    GridView mGrid;
    @Override
    public  void onCreate(Bundle saved){
    super.onCreate(saved);
       setContentView(R.layout.bookmarkv);
       readBookmarks();

       Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
       bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(PreferenceUtils.getTheme(this))));
       mGrid = (GridView) findViewById(R.id.gridview);
        setSupportActionBar(bar);
       GridView grid = (GridView) findViewById(R.id.gridview);
       BookAdapter adt = new BookAdapter(this,m,u,this);
       grid.setAdapter(adt);
       adt.notifyDataSetChanged();
       mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent im = new Intent(getApplicationContext(),MainActivity.class);
               im.putExtra("url",u.get(i));
               im.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(im);
           }
       });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("");
        getMenuInflater().inflate(R.menu.book, menu);
        return true;
    }
public void readBookmarks(){
    try{

        File toRead = new File(getApplicationContext().getFilesDir(),"bookmarks.oi");
        if(toRead.exists()){
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toRead));
            Object obj = ois.readObject();
            ois.close();
            ois.close();
            HashMap<String,String> mHash = (HashMap<String,String>) obj;
            m.addAll(mHash.keySet());
            u.addAll(mHash.values());
        }
    }
    catch(Exception ee){

    }
}

}
