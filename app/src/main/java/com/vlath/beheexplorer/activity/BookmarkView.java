/* Copyright (c) Vlad Todosin */

package com.vlath.beheexplorer.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.adapters.BookAdapter;

import java.util.ArrayList;

public class BookmarkView extends ActionBarActivity {
    ArrayList<String> m = new ArrayList<>();
    @Override
    public  void onCreate(Bundle saved){
    super.onCreate(saved);
       setContentView(R.layout.bookmarkv);
       m.add("este");
       m.add("DA ba");
       m.add("sal");
       m.add("salsagfghsfjhdgfgjfnjfn");
       Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(bar);
       GridView grid = (GridView) findViewById(R.id.gridview);
       BookAdapter adt = new BookAdapter(this,m,this);
       grid.setAdapter(adt);
       adt.notifyDataSetChanged();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("");
        getMenuInflater().inflate(R.menu.book, menu);
        return true;
    }
}
