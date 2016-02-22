package com.vlath.beheexplorer.activity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.webkit.*;
import android.webkit.WebSettings.PluginState;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
import com.vlath.beheexplorer.view.BeHeView;
import com.vlath.beheexplorer.R;



@SuppressWarnings("deprecation")
public class MainActivity extends BeHeActivity {


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setTitle("");
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem desktop = menu.findItem(R.id.action_tabs);
		desktop.setTitle(R.string.desktop);

		desktop.setCheckable(true);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
			case R.id.action_incognito:
			if(!item.isChecked()){
				item.setChecked(true);
				CookieManager.getInstance().setAcceptCookie(false);
			/*	web.getSettings().setCacheMode(web.getSettings().LOAD_NO_CACHE);
				web.getSettings().setAppCacheEnabled(false);
				web.clearHistory();
				web.clearCache(true);
				web.clearFormData();
				web.getSettings().setSavePassword(false);
				web.getSettings().setSaveFormData(false);
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2F4F4F")));
			}
            else{
				item.setChecked(false);
				CookieManager.getInstance().setAcceptCookie(true);
				web.getSettings().setCacheMode(web.getSettings().LOAD_NO_CACHE);
				web.getSettings().setAppCacheEnabled(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("cache", false));
				web.clearCache(true);
				web.getSettings().setSavePassword(true);
				web.getSettings().setSaveFormData(true);
				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
				String color = settings.getString("color", "#A9A9A9");
				getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
*/
			}
				break;

			case R.id.action_search:
				String toSearch;
                toSearch= txt.getText().toString();




					if(Patterns.WEB_URL.matcher(toSearch).matches()){
						if(toSearch.contains("https://")){
							//web.loadUrl("https://" + txt.getText().toString());
						}
						else{
							//web.loadUrl(txt.getText().toString());
						}

						View view = activity.getCurrentFocus();
						if (view != null) {
							InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
						}
						web.requestFocus();
					}
				else{
                    web.search(txt.getText().toString());
					View view = this.getCurrentFocus();
					if (view != null) {
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}
					web.requestFocus();
				}
				try {
					list.add(txt.getText().toString());
					PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putStringSet("recent", list);
				}
				catch(Exception e) {
				}

					break;
			case R.id.action_tabs:
				if (!item.isChecked()) {
					item.setChecked(true);
					//web.setUserAgentString("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
				   //web.reload(XWalkView.RELOAD_NORMAL);
				}
				else{
					item.setChecked(false);
					//web.setUserAgentString("Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
				   // web.reload(XWalkView.RELOAD_NORMAL);
				}
				break;
			case R.id.action_pref:
				Intent mSettings = new Intent(this, Settings.class);
				startActivity(mSettings);
				break;
			case R.id.action_show:
				//mDrawerLayout.openDrawer(Gravity.LEFT);
			break;

			case R.id.action_book:
				final Context context = this;
				LayoutInflater li = LayoutInflater.from(context);
				View promptsView = li.inflate(R.layout.promt, null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
				alertDialogBuilder.setView(promptsView);
				final EditText userInput = (EditText) promptsView
						.findViewById(R.id.editTextDialogUserInput);
				try {
					userInput.setText(web.getTitle());
				} catch (Exception e) {
					userInput.setText("Web Page");
				}
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										HashMap<String,String> map = new HashMap<>();
                                       try{
										result = userInput.getText().toString();
										   File toWrite = new File(getApplicationContext().getFilesDir(),"bookmarks.oi");
										   if(toWrite.exists()){
											ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toWrite));
										    Object obj = ois.readObject();
											ois.close();
											HashMap<String,String> mHash = (HashMap<String,String>) obj;
											map.putAll(mHash);
											map.put(result,web.getUrl());

										    }
									       else{
										   map.put(result,web.getUrl());
										   }
									       ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(toWrite));
									       oos.writeObject(map);
										   oos.flush();
										   oos.close();
										   names.add(result);
									       book.notifyDataSetChanged();
									        }
									   catch(Exception ee){

									   }
									}
								});
				alertDialogBuilder.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				break;

			case R.id.action_refresh:
				web.startReaderMode();
				break;

			case R.id.action_stop:
				web.stopLoading();
				break;



			case R.id.action_hist:

				Toast.makeText(getApplicationContext(), R.string.historytast, Toast.LENGTH_LONG).show();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
		@Override
		public void onResume() {
			super.onResume();
			//getSupportActionBar().setHomeButtonEnabled(true);
			//getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			//getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
			String color = settings.getString("color", "#A9A9A9");
			String size = settings.getString("size", "18");
			String search = settings.getString("search", "1");
			web.setSearchEngine(Integer.parseInt(search));
			java = settings.getBoolean("java", true);
			boolean plugins = settings.getBoolean("plugins", true);
			boolean cache = settings.getBoolean("cache", false);
			int sz = Integer.parseInt(size);
		  /*web.getSettings().setJavaScriptEnabled(java);
			if (plugins == true) {
				web.getSettings().setPluginState(PluginState.ON);
			}
			web.getSettings().setAppCacheEnabled(cache);
			web.getSettings().setDefaultFontSize(sz);
			web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

			boolean ic = settings.getBoolean("icon", true);
			ico = ic;
			web.setIcon(ico);*/

		}

		@Override
		public void onPause() {
			super.onPause();
		}

		@Override
		protected void onPostCreate(Bundle savedInstanceState) {
			super.onPostCreate(savedInstanceState);
			mDrawerToggle.syncState();
		    web.initializeSettings();
		}

		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			super.onConfigurationChanged(newConfig);
			mDrawerToggle.onConfigurationChanged(newConfig);
		}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
           String mUrl = data.getStringExtra("search");
			//web.load("https://www.google.com/?gws_rd=cr,ssl&ei=u0W-VoX-FYTt6QT7t5bICQ#q=" + mUrl,null);
			}
		}
	}
}
