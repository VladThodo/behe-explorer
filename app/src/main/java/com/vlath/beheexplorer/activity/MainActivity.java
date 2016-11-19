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
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
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
import android.widget.Button;
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

import com.vlath.beheexplorer.fragment.WebFragment;
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
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
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
				// userInput.setText(web.het);
				} catch (Exception e) {
					userInput.setText("Web Page");
				}
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton(R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										HashMap<String,String> map = new HashMap<>();
                                       try {
										   result = userInput.getText().toString();
										   File toWrite = new File(getApplicationContext().getFilesDir(), "bookmarks.oi");
										   if (toWrite.exists()) {
											   ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toWrite));
											   Object obj = ois.readObject();
											   ois.close();
											   HashMap<String, String> mHash = (HashMap<String, String>) obj;
											   map.putAll(mHash);
											   map.put(result, txt.getText().toString());

										   } else {
											   map.put(result, txt.getText().toString());
										   }
										   ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(toWrite));
										   oos.writeObject(map);
										   oos.flush();
										   oos.close();

									   Snackbar.make(root, "Adaugat", Snackbar.LENGTH_LONG)
										           .setAction("Vedeti", new View.OnClickListener() {
													   @Override

												   public void onClick(View view) {
														   BookmarkView fragment = new BookmarkView();
														   android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
														   fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit);
														   fragmentTransaction.replace(R.id.root, fragment);
														   fragmentTransaction.commit();
													   }
												   })
												   .show();
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
                navView.getMenu().getItem(2).setCheckable(true);
				break;
			case R.id.action_home:
	//			super.web.loadUrl(new PreferenceUtils(this).getHomePage());
				break;

		}
		return super.onOptionsItemSelected(item);
	}



		@Override
		protected void onPostCreate(Bundle savedInstanceState) {
			super.onPostCreate(savedInstanceState);
			mDrawerToggle.syncState();
			navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

				// This method will trigger on item Click of navigation menu
				@Override
				public boolean onNavigationItemSelected(MenuItem menuItem) {


					//Checking if the item is in checked state or not, if not make it in checked state
					if (menuItem.isChecked())
						menuItem.setChecked(false);
					else menuItem.setChecked(true);
					mDrawerLayout.closeDrawers();
					switch (menuItem.getItemId()) {
						case R.id.inbox:
							clearBackStack();
							btn = (Button)findViewById(R.id.voice);
							btn.setVisibility(View.GONE);
							txt.setText("Bookmarks");
							BookmarkView fragment = new BookmarkView();
							android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
							fragmentTransaction.setCustomAnimations(R.anim.enter,R.anim.exit);
							fragmentTransaction.replace(R.id.root, fragment);
							fragmentTransaction.commit();
							return true;
						case R.id.search:
							clearBackStack();
							btn = (Button)findViewById(R.id.voice);
							btn.setVisibility(View.VISIBLE);
							WebFragment web = new WebFragment();
							web.setArgs(pBar,txt,activity);
							android.support.v4.app.FragmentTransaction fragmenttransaction = getSupportFragmentManager().beginTransaction();
							fragmenttransaction.setCustomAnimations(R.anim.enter,R.anim.exit);
							fragmenttransaction.replace(R.id.root, web);
							fragmenttransaction.commit();
							return true;
						case R.id.sett:
							Intent ine = new Intent(getApplicationContext(),SettingsActivity.class);
							ine.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(ine);
							return true;

						case R.id.history:
							clearBackStack();
							WebFragment webe = new WebFragment();
							webe.setArgs(pBar,txt,activity);
							webe.showHistory(true);
							android.support.v4.app.FragmentTransaction fragmentransaction = getSupportFragmentManager().beginTransaction();
							fragmentransaction.setCustomAnimations(R.anim.enter,R.anim.exit);
							fragmentransaction.replace(R.id.root, webe);
							fragmentransaction.commit();
							return true;
					}

					return true;
				}}
			);
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
	private void clearBackStack() {
		FragmentManager manager = getSupportFragmentManager();
		if (manager.getBackStackEntryCount() > 0) {
			FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
			manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
	}
}
