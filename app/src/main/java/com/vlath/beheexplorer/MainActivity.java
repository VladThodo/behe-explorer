package com.vlath.beheexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import  com.vlath.beheexplorer.Bookmark;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
	private String[] mPlanetTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	boolean ico;
	String READ = "";
	android.support.v4.app.ActionBarDrawerToggle mDrawerToggle;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();

		final TextView contentView = new TextView(this);



		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		boolean ic = settings.getBoolean("icon", true);
		ico = ic;

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
		mPlanetTitles = new String[]{"tree", "two"};
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

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		ActionBar actionBar = getSupportActionBar();

		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);


		try {
			bookmark = getIntent().getExtras().getString("webb");
		} catch (Exception e) {
		}


		if (delete == true) {
			Toast.makeText(getApplicationContext(), R.string.historytast, Toast.LENGTH_LONG).show();
		}
		web = (WebView) findViewById(R.id.web1);

		try {
			if (intent.getData() != null) {
				web.loadUrl(intent.getData().toString());
			}
		} catch (Exception er) {
		}
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
		try {
			File toWrite = new File(getApplicationContext().getFilesDir(),"recents.oi");
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toWrite));
			Object result = ois.readObject();
			ois.close();
			HashMap<String,String> map = (HashMap) result;
			names.addAll(map.keySet());
			adapter.notifyDataSetChanged();

		}
		catch(Exception e){
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}

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
				view.loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText);");
                READ = contentView.getText().toString();
			Toast.makeText(getApplicationContext(),READ,Toast.LENGTH_LONG).show();
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
				if (url.startsWith("vnd.youtube:")) {

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
		web.addJavascriptInterface(new BeHeInterface(contentView), "INTERFACE");
		list = new ArrayList<String>();


	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		setTitle("");
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem item = menu.findItem(R.id.action_tab);

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
			case R.id.action_read:

				break;
			case R.id.action_search:
				Intent intet = new Intent(this, Search.class);
				startActivity(intet);
				break;


			case R.id.action_about:
				Intent intent = new Intent(getApplicationContext(), Second.class);
				startActivity(intent);
				overridePendingTransition(R.layout.pull_in_left, R.layout.push_out_right);
				break;

			case R.id.action_tabs:
				if (item.isChecked() == true){
					item.setChecked(false);
				    web.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36");
				}
				if(!item.isChecked()){
					item.setChecked(true);
				    web.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36");
				}
				break;
			case R.id.action_pref:
				Intent ent = new Intent(this, Settings.class);
				startActivity(ent);

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
										try {
											result = userInput.getText().toString();
											Bookmark mark = new Bookmark(web.getTitle(), web.getFavicon());
											HashMap<String, HashMap<Bitmap, String>> map = new HashMap<String, HashMap<Bitmap, String>>();
											File file = new File(Environment.getExternalStorageDirectory() + "/BeHe ExploreR");
											if (!file.exists()) {
												file.mkdirs();
											}
											File toWrite = new File(file + "/bookmarks.oi");
											if (toWrite.exists()) {
												ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toWrite));
												Object obj = ois.readObject();
												ois.close();
												file.delete();
												HashMap<String, HashMap<Bitmap, String>> mm = new HashMap<String, HashMap<Bitmap, String>>();
												mm = (HashMap) obj;
												map.putAll(mm);
												HashMap<Bitmap, String> mi = new HashMap<Bitmap, String>();
												mi.put(web.getFavicon(), web.getUrl());
												map.put(result, mi);
											} else {
												HashMap<Bitmap, String> mi = new HashMap<Bitmap, String>();
												mi.put(web.getFavicon(), web.getUrl());
												map.put(result, mi);
											}
											ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(toWrite));
											outputStream.writeObject(map);
											outputStream.flush();
											outputStream.close();
											Toast.makeText(getApplicationContext(), "Bookmark saved", Toast.LENGTH_SHORT).show();
										} catch (Exception ie) {
											Toast.makeText(getApplicationContext(), ie.toString(), Toast.LENGTH_LONG).show();
										}
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
				Intent inten = new Intent(getApplicationContext(), Bookmarks.class);
				inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(inten);
				overridePendingTransition(R.layout.pull_in_left, R.layout.push_out_right);
				break;
			case R.id.action_hist:
				web.clearHistory();
				Toast.makeText(getApplicationContext(), R.string.historytast, Toast.LENGTH_LONG).show();
				break;
			case R.id.action_notes:
				Intent rc = new Intent(getApplicationContext(), Notes.class);
				startActivity(rc);
				overridePendingTransition(R.layout.pull_in_left, R.layout.push_out_right);
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

class BeHeInterface
{
	private TextView contentView;

	public BeHeInterface(TextView text)
	{
		contentView = text;
	}

	@SuppressWarnings("unused")
	@android.webkit.JavascriptInterface
	public void processContent(String aContent)
	{
		final String content = aContent;
		contentView.post(new Runnable()
		{
			public void run()
			{
				contentView.setText(content);
			}
		});

	}
}




	
