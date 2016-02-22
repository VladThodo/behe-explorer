package com.vlath.beheexplorer.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.vlath.beheexplorer.R;

public class ReadingActivity extends ActionBarActivity {
    @Override
    public void onCreate(Bundle saved){
    super.onCreate(saved);
    setContentView(R.layout.reading);
    Toolbar bar = (Toolbar)findViewById(R.id.toolbar);
    setSupportActionBar(bar);
    TextView textTitle = (TextView) findViewById(R.id.textViewTitle);
    textTitle.setText(getIntent().getExtras().getString("title"));
    TextView textBody = (TextView) findViewById(R.id.textViewBody);
    textBody.setText(getIntent().getExtras().getString("text"));
}
}
