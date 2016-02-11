package com.vlath.beheexplorer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
@SuppressWarnings("deprecation")
public class Second extends ActionBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);

		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String color = settings.getString("color", "#FFFFFF");
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
	    setTitle(R.string.about);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setTitle("");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);




		MenuItem stop = menu.findItem(R.id.action_stop);
	    stop.setVisible(true);
	    MenuItem f = menu.findItem(R.id.action_refresh);
	    f.setVisible(false);
	    MenuItem t = menu.findItem(R.id.action_hist);
	    t.setVisible(false);
	    MenuItem m = menu.findItem(R.id.action_book);
	    m.setVisible(false);	


	   
	    return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch(item.getItemId()){
		
				
		
				
		 
		
		case R.id.action_about:
			Intent intent=new Intent(getApplicationContext(),Second.class);
			startActivity(intent);
         
			
	
		case R.id.action_pref:
			Intent ent = new Intent(getApplicationContext(),Settings.class);
			startActivity(ent);
			
			
			
		break;
		
		
		
		
		

			
		
		
		case R.id.action_show:

			break;
		case R.id.action_hist:
			
			
		break;
		case R.id.action_notes:
			Intent rc = new Intent(getApplicationContext(),Notes.class);
			startActivity(rc);
			break;
			
		
		}
	
		
		return super.onOptionsItemSelected(item);
	}
@Override
	public void onResume(){
	super.onResume();
	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
	String color = settings.getString("color", "#FFFFFF");
	getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
}
}