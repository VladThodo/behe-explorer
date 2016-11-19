/*
 Copyright (c) 2016 Vlad Todosin
*/

package com.vlath.beheexplorer.activity;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.vlath.beheexplorer.controllers.UI;
import com.vlath.beheexplorer.fragment.WebFragment;
import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.view.AnimatedProgressBar;
@SuppressWarnings("deprecation")
public class BeHeActivity extends ActionBarActivity implements UI {
    private boolean _doubleBackToExitPressedOnce    = false;
    RelativeLayout root;
    BeHeActivity activity = this;
    String result;
    ImageView view;
    public DrawerLayout mDrawerLayout;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    EditText txt;
    Toolbar bar;
    Button btn;
    NavigationView navView;
    AnimatedProgressBar pBar;
    @Override
    public void onCreate(Bundle savedInstanceState){
     super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bar = (Toolbar) findViewById(R.id.toolbar);
        txt = (EditText)findViewById(R.id.edit);
        pBar = (AnimatedProgressBar) findViewById(R.id.progressBar);
        WebFragment fragment = new WebFragment();
        fragment.setArgs(pBar,txt,activity);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.root, fragment);
        fragmentTransaction.commit();
        setSupportActionBar(bar);
        btn = (Button)findViewById(R.id.voice);
        root = (RelativeLayout) findViewById(R.id.root);
        navView =(NavigationView)findViewById(R.id.navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,bar,
                R.string.drawer_open,
                R.string.drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset){

            }
        };
        mDrawerLayout.setDrawerElevation(20);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(getIntent().getData() != null){
        }
        else{
        }
    }
        @Override
        public void onBackPressed() {
                if (_doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    this.finish();
                }
            else {
                    this._doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Press again to quit", Toast.LENGTH_SHORT).show();
                }
           new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                   _doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }


    @Override
    public void initializeTheme() {

    }

    @Override
    public void setMainTab(int position) {

    }

    @Override
    public void setIconColorScheme() {

    }
   @Override
   public void onDestroy(){
       super.onDestroy();

   }



}
