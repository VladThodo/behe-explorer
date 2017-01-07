/*
 Copyright 2016 Vlad Todosin
*/
package com.vlath.beheexplorer.activity;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;

import com.vlath.beheexplorer.adapters.BookAdapter;
import com.vlath.beheexplorer.controllers.TabManager;
import com.vlath.beheexplorer.utils.ClearCache;
import com.vlath.beheexplorer.view.AnimatedProgressBar;
import com.vlath.beheexplorer.view.BeHeView;
import com.vlath.beheexplorer.R;


@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {
	private boolean _doubleBackToExitPressedOnce  = false;
	public Context mContext = this;
	ActionBarActivity activity;
	SwitchCompat desktop;
	SwitchCompat privat;
	ImageView view;
	public DrawerLayout mDrawerLayout;
	android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
	EditText txt;
	Toolbar bar;
	Button btn;
	NavigationView navView;
	NavigationView tabView;
	AnimatedProgressBar pBar;
	MenuItem m1;
	MenuItem m2;
	BeHeView web;
	SwipeRefreshLayout swipe;
	TextView txe;
	FrameLayout root;
	Uri data;
	ArrayList<String> m = new ArrayList<>();
	ArrayList<String> u = new ArrayList<>();
	GridView mGrid;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		activity = this;
		bar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(bar);
		txt = (EditText)findViewById(R.id.edit);
		swipe = new SwipeRefreshLayout(this);
		pBar = (AnimatedProgressBar) findViewById(R.id.progressBar);
		btn = (Button)findViewById(R.id.voice);
		txe = new TextView(this);
		root = (FrameLayout) findViewById(R.id.root);
		navView =(NavigationView)findViewById(R.id.left_navigation);
		desktop = (SwitchCompat) navView.getMenu().getItem(6).getActionView();
		root = (FrameLayout) findViewById(R.id.root);
		privat = (SwitchCompat)navView.getMenu().getItem(7).getActionView();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerElevation(20);
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,
				mDrawerLayout,bar,
				R.string.drawer_open,
				R.string.drawer_close){
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset){
			}
		};
		ActionBar mBar = getSupportActionBar();
		mBar.setDisplayHomeAsUpEnabled(true);
	    initializeBeHeView();
		registerForContextMenu(web);
	    data = getIntent().getData();
		initialize();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setTitle("");
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		m1 = menu.findItem(R.id.action_home);
		m2 = menu.findItem(R.id.action_book);
		m1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem menuItem) {
				BeHeView view = TabManager.getCurrentTab();
				view.loadHomepage();
				return false;
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
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
										BeHeView view = TabManager.getCurrentTab();
										try {
											String result = userInput.getText().toString();
											File toWrite = new File(getApplicationContext().getFilesDir(), "bookmarks.oi");
											if (toWrite.exists()) {
												ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toWrite));
												Object obj = ois.readObject();
												ois.close();
												HashMap<String, String> mHash = (HashMap<String, String>) obj;
												map.putAll(mHash);
												map.put(result, view.getUrl());

											} else {
												map.put(result, view.getUrl());
											}
											ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(toWrite));
											oos.writeObject(map);
											oos.flush();
											oos.close();

											Snackbar.make(root, "Adaugat", Snackbar.LENGTH_LONG)
													.setAction("Vedeti", new View.OnClickListener() {
														@Override
														public void onClick(View view) {

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


			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void initialize() {

		    mDrawerToggle.syncState();
			tabView = (NavigationView) findViewById(R.id.right_navigation);
		    TabManager.setNavigationView(tabView);
		    tabView.setItemIconTintList(null);

		    FloatingActionButton addTab = (FloatingActionButton) findViewById(R.id.add_ta);
		    addTab.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					BeHeView behe = new BeHeView(getApplicationContext(),activity,pBar,false,txt);
					behe.loadHomepage();
					TabManager.addTab(behe);
					TabManager.setCurrentTab(behe);
					TabManager.updateTabView();
					refreshTab();
				}
			});
		    FloatingActionButton delTab = (FloatingActionButton) findViewById(R.id.remove_tab);
		    delTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				int size = TabManager.getList().size();
				if(size > 1){
					BeHeView tab = TabManager.getCurrentTab();
					BeHeView main = TabManager.getList().get(0);
					TabManager.setCurrentTab(main);
					TabManager.removeTab(tab);
					TabManager.updateTabView();
					refreshTab();
				}
			    else{
					finish();
				}
			}
		    });
		       tabView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
				 @Override
				 public boolean onNavigationItemSelected(MenuItem item) {
					 List<MenuItem> items = new ArrayList<>();
					 Menu menu = tabView.getMenu();
					 for(int i = 0;i < menu.size();i++){
						 items.add(menu.getItem(i));
					 }
					 for(MenuItem itm : items){
						 itm.setChecked(false);
					 }
					 item.setChecked(true);
					 BeHeView view = TabManager.getTabAtPosition(item);
					 TabManager.setCurrentTab(view);
					 refreshTab();
					 return false;
				 }
			 });

		     navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

				// This method will trigger on item Click of nav_text menu
				@Override
				public boolean onNavigationItemSelected(MenuItem menuItem) {
					if (menuItem.isChecked())
						menuItem.setChecked(false);
					else menuItem.setChecked(true);
					mDrawerLayout.closeDrawers();
					switch (menuItem.getItemId()) {
						case R.id.inbox:
							showBookMarks();
							return true;
						case R.id.search:
							hideBookMarks();
							btn = (Button)findViewById(R.id.voice);
							btn.setVisibility(View.VISIBLE);
							return true;
						case R.id.sett:
							clearBackStack();
							menuItem.setChecked(false);
							Intent ine = new Intent(getApplicationContext(),SettingsActivity.class);
							ine.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(ine);
							return true;
						case R.id.history:
							clearBackStack();
							btn = (Button)findViewById(R.id.voice);
					     	btn.setVisibility(View.VISIBLE);
							return true;
						case R.id.desktop:
							menuItem.setChecked(false);
							return true;
						case R.id.privat:
							menuItem.setChecked(false);
							return true;
						case R.id.tabs:
							mDrawerLayout.openDrawer(GravityCompat.END);
							return true;
					}

					return true;
				}}
			);
		txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean b) {
				if(view.isFocused()){
					txt.setCursorVisible(true);
					m1.setIcon(R.drawable.ic_cancel_black_24dp);
					m1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem menuItem) {
							txt.setText("");
							return false;
						}
					});
					m2.setVisible(false);
				}
				else{
					txt.setCursorVisible(true);
					m1.setIcon(R.drawable.ic_home_black_24dp);
					txt.setSelection(0);
					m2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem menuItem) {

							return false;
						}
					});
					m2.setVisible(true);
				}
			}
		});
		desktop.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener(){
			@Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){

				}
				else{

				}
			}
		});
		privat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

			}
		});
		txt.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				txt.setCursorVisible(true);
				if (actionId == EditorInfo.IME_ACTION_GO) {
					String toSearch;
					toSearch = txt.getText().toString();
					if (toSearch.contains("http://") || toSearch.contains("https://")){
						web.loadUrl(toSearch);
					} else {
						if(toSearch.contains("www")){
							web.loadUrl("http://" + toSearch);
						}
						else{
							if(toSearch.contains(".")){
								web.loadUrl("http://www." + toSearch);
							}
							else{
								web.searchWeb(toSearch);
							}
						}
					}
					View view = getCurrentFocus();
					if (view != null) {
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}
					txt.setCursorVisible(false);
					return true;
				} else {
					txt.setCursorVisible(false);
					View view = getCurrentFocus();
					if (view != null) {
						InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}
					return true;
				}

			}
		});
		   swipe.setOnRefreshListener(new OnRefreshListener() {
			   @Override
			   public void onRefresh() {
				   swipe.setRefreshing(true);
				   new Handler().postDelayed(new Runnable() {

					   @Override
					   public void run() {
						   web.reload();
						   swipe.setRefreshing(false);
					   }
				   }, 1000);
				   swipe.setColorScheme(
						   android.R.color.holo_blue_light,
						   android.R.color.holo_green_light
				   );
			   }
		   });
           desktop.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				 @Override
				 public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
					 BeHeView behe = TabManager.getCurrentTab();
					 if(b){
						 behe.setDesktop();
					 }
				     else{
						 behe.setMobile();
					 }
				 }
			 });
		   privat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			   @Override
			   public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				   BeHeView behe = TabManager.getCurrentTab();
				   behe.setPrivate(b);
			   }
		   });
	}
	@Override
	public void onStart(){
		super.onStart();
		if ( data != null) {
		web.loadUrl(data.toString());
		}
		else {
		web.loadHomepage();
		}
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
					txt.setText(spokenText);

				}
			}
		}
		if (requestCode == 2) {
			if (resultCode == RESULT_OK) {
				if (data!=null) {
					Bundle extras = data.getExtras();
					final Bitmap selectedBitmap = extras.getParcelable("data");
					view.setImageBitmap(selectedBitmap);
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
	@Override
	public void onBackPressed() {
		if(!TabManager.getCurrentTab().canGoBack()) {
			if (_doubleBackToExitPressedOnce) {
				super.onBackPressed();
				this.finish();
			} else {
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
		else{
			TabManager.getCurrentTab().goBack();
		}
	}
    @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu,v,menuInfo);
		final WebView.HitTestResult result = web.getHitTestResult();
		final String url = result.getExtra();
		MenuItem.OnMenuItemClickListener handler = new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
					switch(item.getItemId()) {
					  case 1:
						String name = URLUtil.guessFileName(url, "", "");
						DownloadManager.Request request = new DownloadManager.Request(
								Uri.parse(url));
						request.allowScanningByMediaScanner();
						request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
						request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
						DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
						dm.enqueue(request);
					  break;
					  case 2:
						web.loadUrl(url);
					  break;
					  case 3:
						  ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
						  ClipData clip = ClipData.newPlainText("", url);
						  clipboard.setPrimaryClip(clip);
					  break;
					}
				return true;
			}
		};
		if (result.getType() == WebView.HitTestResult.IMAGE_TYPE ||
				result.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
			// Menu options for an image.
			//set the header title to the image url
			if(url.startsWith("data")){
			}
			else {
				menu.setHeaderTitle(result.getExtra());
				menu.add(0, 1, 0, "Save image").setOnMenuItemClickListener(handler);
				menu.add(0, 2, 0, "View Image").setOnMenuItemClickListener(handler);
			}
		} else if (result.getType() == WebView.HitTestResult.ANCHOR_TYPE ||
				result.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
			// Menu options for a hyperlink.
			//set the header title to the link url
			menu.setHeaderTitle(result.getExtra());
			menu.add(0,3 , 0, "Copy Link").setOnMenuItemClickListener(handler);
		}
	    menu.setHeaderTitle(result.getExtra());
	}

	@Override
	public void onDestroy(){
		super.onDestroy();

	}
	public static void trimCache(Context context) {
		try {
			File dir = context.getCacheDir();
			if (dir != null && dir.isDirectory()) {
				deleteDir(dir);
			}
		} catch (Exception e) {
		}
	}
	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}
	public void initializeBeHeView() {
		List<BeHeView> list = TabManager.getList();
		if(list.isEmpty()) {
			web = new BeHeView(getApplicationContext(), this, pBar, false, txt);
			TabManager.addTab(web);
		}
		else{
			web = TabManager.getCurrentTab();
			ViewGroup parent = (ViewGroup)web.getParent();
			if(parent != null){
				parent.removeAllViews();
			}
		}
		web.setLayoutParams(new SwipeRefreshLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		web.setIsCurrentTab(true);
		TabManager.setCurrentTab(web);
		swipe.addView(web);
		root.addView(swipe);
	}

    public void refreshTab() {
		web = TabManager.getCurrentTab();
		web.setLayoutParams(new SwipeRefreshLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		swipe = new SwipeRefreshLayout(this);
		ViewGroup group = (ViewGroup) web.getParent();
		if(group != null){
			group.removeAllViews();
		}
		swipe.addView(web);
		swipe.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipe.setRefreshing(true);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						BeHeView view = TabManager.getCurrentTab();
						view.reload();
						swipe.setRefreshing(false);
					}
				}, 1000);
				swipe.setColorScheme(
						android.R.color.holo_blue_light,
						android.R.color.holo_green_light
				);
			}
		});
	    for(int i = 0;i < root.getChildCount();i++){
			if(root.getChildAt(i) instanceof GridView){

			}
		    else{
				View view = root.getChildAt(i);
				root.removeView(view);
			}
		}
		root.addView(swipe);
	    txt.setText(web.getUrl());
	}
	private void clearBackStack() {
		FragmentManager manager = getSupportFragmentManager();
		if (manager.getBackStackEntryCount() > 0) {
			FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
            manager.popBackStack(first.getId(),FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
	}
	public void readBookmarks() {
		try {

			File toRead = new File(getApplicationContext().getFilesDir(), "bookmarks.oi");
			if (toRead.exists()) {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toRead));
				Object obj = ois.readObject();
				ois.close();
				ois.close();
				HashMap<String, String> mHash = (HashMap<String, String>) obj;
				for(String title : mHash.keySet()){
					if(!m.contains(title)){
						m.add(title);
					}
				}
				for(String url : mHash.values()){
					if(!u.contains(url)){
						u.add(url);
					}
				}
			}
		} catch (Exception ee) {

		}
	}
    public void showBookMarks(){
		mGrid = (GridView)findViewById(R.id.gridview);
		readBookmarks();
		final BookAdapter adt = new BookAdapter(mContext, m, u);
		mGrid.setAdapter(adt);
		adt.notifyDataSetChanged();
		mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				BeHeView behe = TabManager.getCurrentTab();
				behe.loadUrl(u.get(i));
			    hideBookMarks();
			}
		});
		mGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int i, long l) {

				Log.d("INT",String.valueOf(i));
				new android.app.AlertDialog.Builder(mContext)
						.setTitle("Delete bookmark?")
						.setMessage("Are you sure you want to delete this bookmark?")
						.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								File toRead = new File(mContext.getFilesDir(), "bookmarks.oi");
								HashMap<String, String> mHash = new HashMap<>();
								try {
									if (toRead.exists()) {
										ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toRead));
										Object obj = ois.readObject();
										ois.close();
										ois.close();
										mHash = (HashMap<String, String>) obj;
										String toRemove = m.get(i);
										mHash.remove(toRemove);
										m.clear();
										u.clear();
									    adt.notifyDataSetChanged();
									}
								} catch (Exception ee) {
								}
								try{
									if(toRead.exists()){
										ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(toRead));
										oos.writeObject(mHash);
										oos.flush();
										oos.close();
									}
									else{

									}
								}
								catch(Exception e){

								}

								showBookMarks();
							}
						})
						.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							}
						})
						.show();
				return true;
			}
		});
	    txt.setText(getResources().getText(R.string.boomarks));
		BeHeView view = TabManager.getCurrentTab();
		view.setIsCurrentTab(false);
		swipe.setVisibility(View.GONE);
		mGrid.setVisibility(View.VISIBLE);
	}
    public void hideBookMarks(){
		if(mGrid != null) {
			BeHeView view = TabManager.getCurrentTab();
			view.setIsCurrentTab(true);
			txt.setText(view.getUrl());
			mGrid.setVisibility(View.GONE);
			swipe.setVisibility(View.VISIBLE);
		}
	}
}

