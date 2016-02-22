package com.vlath.beheexplorer.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebIconDatabase;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.vlath.beheexplorer.utils.PreferenceUtils;
import com.vlath.beheexplorer.utils.ThemeUtils;
import com.vlath.beheexplorer.view.BeHeView;
import com.vlath.beheexplorer.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

@SuppressWarnings("deprecation")
public class BeHeActivity extends ActionBarActivity {
    BeHeActivity activity = this;
    String result;
    Set<String> list;
    ArrayList<String> mList = new ArrayList<String>();
    ArrayList<String> names = new ArrayList<String>();
    ArrayAdapter<String> book;
    HashMap<String,String> recent = new HashMap<String,String>();
    boolean delete;
    boolean java;
    MenuItem desktop;
    String bookmark;
    AutoCompleteTextView txt;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    ArrayAdapter<String> adapter;
    boolean ico;
    ListView mDrawerList;
    String READ = "";
    android.support.v4.app.ActionBarDrawerToggle mDrawerToggle;
    SwipeRefreshLayout swipeLayout;
    BeHeView web;
    @Override
    public void onCreate(Bundle savedInstanceState){
     super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar bar = (Toolbar) findViewById(R.id.toolbar);
        txt = (AutoCompleteTextView) findViewById(R.id.myautocomplete);
        setSupportActionBar(bar);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        File toRead = new File(getApplicationContext().getFilesDir(),"bookmarks.oi");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mList);
        book = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        txt.setAdapter(adapter);
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toRead));
            Object obj = ois.readObject();
            ois.close();
            HashMap<String,String> map = (HashMap<String,String>) obj;
            names.addAll(map.keySet());
        }
        catch(Exception e){

        }

        web = new BeHeView(this,activity,progressBar,false,txt);
        WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, R.drawable.menu,
                R.string.drawer_open,
                R.string.drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset){

            }
        };
        mDrawerList.setAdapter(book);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> map;
                File toRead = new File(getApplicationContext().getFilesDir(), "bookmarks.oi");
                try {
                    ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toRead));
                    Object obj = ois.readObject();
                    ois.close();
                    map = (HashMap<String, String>) obj;
                    web.loadUrl(map.get(mDrawerList.getItemAtPosition(position).toString()));
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                } catch (Exception e) {

                }

            }
        });
        mDrawerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(activity)

                        .setMessage(R.string.delete_dialog)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String, String> map = new HashMap<String, String>();
                                try {

                                    File toWrite = new File(getApplicationContext().getFilesDir(), "bookmarks.oi");
                                    if (toWrite.exists()) {
                                        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toWrite));
                                        Object obj = ois.readObject();
                                        ois.close();
                                        HashMap<String, String> mHash = (HashMap<String, String>) obj;
                                        map = mHash;
                                        map.remove(mDrawerList.getItemAtPosition(position));
                                        toWrite.delete();
                                        names.clear();
                                        names.addAll(map.keySet());
                                        book.notifyDataSetChanged();
                                    } else {

                                    }
                                    ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(toWrite));
                                    oos.writeObject(map);
                                    oos.flush();
                                    oos.close();

                                } catch (Exception e) {

                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                alert.create();
                alert.show();
                return true;
            }
        });
        web.setLayoutParams(new SwipeRefreshLayout.LayoutParams(SwipeRefreshLayout.LayoutParams.MATCH_PARENT, SwipeRefreshLayout.LayoutParams.MATCH_PARENT));
        swipeLayout.addView(web);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


            @Override
            public void onRefresh() {
                if (swipeLayout.canChildScrollUp() == false) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            web.reload();
                            swipeLayout.setRefreshing(false);
                        }
                    }, 1000);
                    swipeLayout.setColorScheme(
                            android.R.color.holo_blue_light,
                            android.R.color.holo_blue_dark
                    );
                }
            }

        });

        txt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txt.isFocused()) {
                    try {
                        mList.addAll(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getStringSet("recent", null));
                        txt.setThreshold(3);
                        txt.showDropDown();
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        txt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String toSearch;
                    toSearch = txt.getText().toString();

                    if (Patterns.WEB_URL.matcher(toSearch).matches()) {
                        if (toSearch.contains("https://")) {
                            web.loadUrl("https://" + txt.getText().toString());
                        } else {
                            web.loadUrl(txt.getText().toString(), null);
                        }

                        View view = activity.getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }
                        web.requestFocus();
                    }
                    return true;
                } else {
                    web.search(txt.getText().toString());
                    View view = activity.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    web.requestFocus();
                    return true;
                }

            }
        });
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        PreferenceUtils utils = new PreferenceUtils(getApplicationContext());
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(utils.getTheme())));
        web.setSearchEngine(PreferenceUtils.getSearchEngine());
        web.initializeSettings();
		boolean ic = PreferenceUtils.getDisplayPageIcon();
		ico = ic;
        web.setIcon(ico);
        if(getIntent().getData() != null){
            web.loadUrl(getIntent().getData().toString());
        }
        else{
            web.loadUrl(new PreferenceUtils(this).getHomePage());
        }
    }
    public void initializeBrowserActivity(){

    }
}
