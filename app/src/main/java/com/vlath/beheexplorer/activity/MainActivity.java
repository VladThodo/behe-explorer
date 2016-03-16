/*
 Copyright 2016 Vlad Todosin
*/
package com.vlath.beheexplorer.activity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.speech.RecognizerIntent;
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

import com.vlath.beheexplorer.utils.HystoryTask;
import com.vlath.beheexplorer.utils.PreferenceUtils;
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
			if(!item.isChecked()) {
				item.setChecked(true);
				web.setPrivate(true);
			}
            else{
				item.setChecked(false);
				web.setPrivate(false);
			}
				break;

			case R.id.action_tabs:
				if (!item.isChecked()) {
					item.setChecked(true);
					web.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
				    web.reload();
				}
				else{
					item.setChecked(false);
					web.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
				    web.reload();
				}
				break;

			case R.id.action_pref:
				Intent mSettings = new Intent(this, Settings.class);
				startActivity(mSettings);
				break;
			case R.id.action_show:
				super.mDrawerLayout.openDrawer(Gravity.LEFT);
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
			case R.id.action_home:
				super.web.loadUrl(new PreferenceUtils(this).getHomePage());
				break;
			case R.id.action_stop:
				web.stopLoading();
				break;
			case R.id.action_pic:
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				photoPickerIntent.setType("image/*");
				photoPickerIntent.putExtra("crop", "true");
				photoPickerIntent.putExtra("return-data", true);
				photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
				startActivityForResult(photoPickerIntent, 2);
				break;
			case R.id.action_show_history:
				HystoryTask task = new HystoryTask(MainActivity.this,web);
				Void[] v = null;
				task.execute(v);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
		@Override
		public void onResume() {
			super.onResume();
			web.initializeSettings();

		}

		@Override
		public void onPause() {
			super.onPause();
		}

		@Override
		protected void onPostCreate(Bundle savedInstanceState) {
			super.onPostCreate(savedInstanceState);
			mDrawerToggle.syncState();

		}

		@Override
		public void onConfigurationChanged(Configuration newConfig) {
			super.onConfigurationChanged(newConfig);
			mDrawerToggle.onConfigurationChanged(newConfig);
		}
	public void voice (View v){
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		startActivityForResult(intent, 1);

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1){
			if(resultCode == RESULT_OK){
				if(data != null){
					List<String> results = data.getStringArrayListExtra(
							RecognizerIntent.EXTRA_RESULTS);
					String spokenText = results.get(0);
					super.txt.setText(spokenText);

				}
			}
		}
		if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
				if (data!=null) {
					Bundle extras = data.getExtras();
					final Bitmap selectedBitmap = extras.getParcelable("data");
					super.view.setImageBitmap(selectedBitmap);
				    new Thread(new Runnable() {
						@Override
						public void run() {
							FileOutputStream writer = null;
							File image = new File(getFilesDir(),"drawer_image.png");
							try{
								writer = new FileOutputStream(image,false);
								selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, writer);
							    writer.flush();
								writer.close();
							}
						    catch(Exception e){

							}
						}
					}).start();
				}
			}
		}
	}
}
