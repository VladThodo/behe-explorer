package com.vlath.beheexplorer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.format.Formatter;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.webkit.*;
import android.webkit.WebSettings.PluginState;
import android.support.v7.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Handler;
@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity {
	com.vlath.beheexplorer.MainActivity activity = this;
	WebView web;
	String result;
	ArrayList<String> list;
	ArrayList<Bitmap> images = new ArrayList<Bitmap>();
	ArrayList<String> names = new ArrayList<String>();
	TabAdapter adapter;
	HashMap<String,String> recent = new HashMap<String,String>();
	boolean delete;
	boolean java;
	MenuItem desktop;
	String bookmark;
	SwipeRefreshLayout swipeLayout;
	boolean ico;
	String READ = "";
	android.support.v4.app.ActionBarDrawerToggle mDrawerToggle;
	private String[] mPlanetTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		web = (WebView) findViewById(R.id.web1);
		final ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
		progressBar.setLayoutParams(new android.app.ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.MATCH_PARENT, 24));
		final FrameLayout decorView = (FrameLayout) getWindow().getDecorView();
		decorView.addView(progressBar);
		ViewTreeObserver observer = progressBar.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				View contentView = decorView.findViewById(android.R.id.content);
				progressBar.setY(contentView.getY() + 14);
				ViewTreeObserver observer = progressBar.getViewTreeObserver();
				observer.removeGlobalOnLayoutListener(this);
			}
		});
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
		swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		swipeLayout.setOnRefreshListener(new OnRefreshListener() {


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
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
		if (getIntent().getData() != null) {
			web.loadUrl(getIntent().getData().toString());
		}
		else{
			web.loadUrl(PreferenceManager.getDefaultSharedPreferences(this).getString("home_page","https://www.google.com"));

		}
		if (delete == true) {
			Toast.makeText(getApplicationContext(), R.string.historytast, Toast.LENGTH_LONG).show();
		}
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String color = settings.getString("color", "#FFFFFF");
		String size = settings.getString("size", "18");
		java = settings.getBoolean("java", true);
		boolean plugins = settings.getBoolean("plugins", true);
		boolean cache = settings.getBoolean("cache", false);
		int sz = Integer.parseInt(size);
		web.getSettings().setJavaScriptEnabled(java);
		if (plugins == true) {
			web.getSettings().setPluginState(PluginState.ON);
		}
		web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		web.getSettings().setAppCacheEnabled(cache);
		web.getSettings().setDefaultFontSize(sz);
		web.getSettings().setDefaultFixedFontSize(sz);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
		list = new ArrayList<String>();
		boolean ic = settings.getBoolean("icon", true);
		ico = ic;
		web.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && web.canGoBack()) {
					web.goBack();
					return true;
				}
				return true;

			}
		});
		 web.setDownloadListener(new DownloadListener() {
			@Override
			public void onDownloadStart(final String url, final String userAgent,
										final String contentDisposition, final String mimetype,
										final long contentLength) {
				final String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
				String downloadSize = null;
				if (contentLength > 0) {
					downloadSize = Formatter.formatFileSize(activity, contentLength);

				} else {
				}
				AlertDialog.Builder builder = new AlertDialog.Builder(activity);
				builder.setTitle(fileName)
						.setMessage("Doriti sa descarcati acest fisier de:" + downloadSize + " MB ?")

						.setPositiveButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
									request.allowScanningByMediaScanner();
									request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
								}
								DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
								request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
								manager.enqueue(request);
							}
						})
						.setNegativeButton("Anulati", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
						});
				builder.create();
				builder.show();
			}
		});
		names.add("Recente");
		adapter = new TabAdapter(activity, names, images,mDrawerList);
		mDrawerList.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		web.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				MainActivity.this.setTitle(view.getTitle());
			}
		});
		web.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onReceivedIcon(WebView view, Bitmap icon) {
				mDrawerList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				if (ico) {
					Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(icon, 22, 22, false));
					getSupportActionBar().setHomeAsUpIndicator(d);
				} else {
					getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
				}
			}


			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains("vnd.youtube:")) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
							(String.format("http://www.youtube.com/v/%s", url.substring("vnd.youtube:".length())))));
					return true;
				}
				return false;
			}

			@Override
			public void onProgressChanged(WebView view, int progress) {
				if (progress < 100 && progressBar.getVisibility() == ProgressBar.GONE) {
					progressBar.setVisibility(ProgressBar.VISIBLE);
				}
				progressBar.setProgress(progress);
				if (progress == 100) {
					progressBar.setVisibility(ProgressBar.GONE);
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setTitle("");
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem desktop = menu.findItem(R.id.action_tabs);
		desktop.setTitle("Versiune desktop");

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
			case R.id.action_search:
				Intent mSearch = new Intent(this, Search.class);
				startActivity(mSearch);
				break;

			case R.id.action_about:
				Intent mAbout = new Intent(getApplicationContext(), Second.class);
				startActivity(mAbout);
				break;

			case R.id.action_tabs:
				if (item.isChecked() == true) {
					item.setChecked(false);
					web.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
				}
				if (!item.isChecked()) {
					item.setChecked(true);
					web.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
				}
				break;
			case R.id.action_pref:
				Intent mSettings = new Intent(this, Settings.class);
				startActivity(mSettings);
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
						.setIcon(R.drawable.bookmark)
						.setCancelable(false)
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {

									}
								});
				alertDialogBuilder.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				break;

			case R.id.action_refresh:
				web.reload();
				break;

			case R.id.action_stop:
				web.stopLoading();
				break;

			case R.id.action_show:
				break;

			case R.id.action_hist:
				web.clearHistory();
				Toast.makeText(getApplicationContext(), R.string.historytast, Toast.LENGTH_LONG).show();
				break;

			case R.id.action_notes:
				Intent mNotes = new Intent(getApplicationContext(), Notes.class);
				startActivity(mNotes);
				break;
		}
		return super.onOptionsItemSelected(item);
	}
		@Override
		public void onResume() {
			super.onResume();
			SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
			String color = settings.getString("color", "#FFFFFF");
			String size = settings.getString("size", "18");
			java = settings.getBoolean("java", true);
			boolean plugins = settings.getBoolean("plugins", true);
			boolean cache = settings.getBoolean("cache", false);
			boolean ic = settings.getBoolean("icon", true);
			ico = ic;
			int sz = Integer.parseInt(size);
			web.getSettings().setJavaScriptEnabled(java);
			if (plugins == true) {
				web.getSettings().setPluginState(PluginState.ON);
			}
			web.getSettings().setAppCacheEnabled(cache);
			web.getSettings().setDefaultFontSize(sz);
			web.getSettings().setDefaultFixedFontSize(sz);
			getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
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
}
