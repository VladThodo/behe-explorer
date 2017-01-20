/*
 Copyright 2016 Vlad Todosin
*/


package com.vlath.beheexplorer.view;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.URLUtil;
import android.webkit.WebSettings;;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.utils.HomePage;
import com.vlath.beheexplorer.utils.HystoryTask;
import com.vlath.beheexplorer.utils.PreferenceUtils;
import com.vlath.beheexplorer.utils.ThemeUtils;


@SuppressWarnings("unused")
 /*
 * This view acts as the engine of BeHe ExploreR
 */


public class BeHeView extends WebView{
	ThemeUtils theme;
	private boolean isPrivate;
	private String TEXT = "1";
	private int searchEngine = 1;
	private ProgressBar P_BAR;
	private boolean ico ;
	private boolean FOCUS;
	ActionBarActivity WEB_ACTIVITY;
	EditText text;
	private String PAGE_TITLE;
	public String found = "";
	public static int GOOGLE_SEARCH = 1;
	public static int BING_SEARCH = 2;
	public static int YAHOO_SEARCH = 3;
	public static int DUCKDUCKGO_SEARCH = 4;
	public static int ASK_SEARCH = 5;
	public static int WOW_SEARCH = 6;
	/*
	* Public constructors of BeHeView
	 */
	public BeHeView(Context c,Activity activity){
		super(c);
		WEB_ACTIVITY = (ActionBarActivity) activity;
	}
	public BeHeView(Context context, ActionBarActivity activity, ProgressBar pBar, boolean Private, final EditText txt)  {
		super(context);
		theme = new ThemeUtils(activity);
		isPrivate = Private;
		P_BAR = pBar;
		WEB_ACTIVITY = activity;
		text = txt;
	    WEB_ACTIVITY.registerForContextMenu(this);
		setWebChromeClient(new BeHeChromeClient(P_BAR,this));
	    setWebViewClient(new BeHeWebClient(text,WEB_ACTIVITY,false,this));
	    setDownloadListener(new CiobanDownloadListener(WEB_ACTIVITY, this));
	    initializeSettings();
	}
	public void initializeSettings(){
		PreferenceUtils utils = new PreferenceUtils(WEB_ACTIVITY);
		WebSettings settings = getSettings();
		settings.setDisplayZoomControls(false);
		settings.setBuiltInZoomControls(true);
		settings.setSupportMultipleWindows(false);
		settings.setEnableSmoothTransition(true);
		if (isPrivate){
			CookieManager.getInstance().setAcceptCookie(false);
			settings.setCacheMode(getSettings().LOAD_NO_CACHE);
			settings.setAppCacheEnabled(false);
			clearHistory();
			clearCache(true);
			clearFormData();
			settings.setSavePassword(false);
			settings.setSaveFormData(false);
			settings.setSupportZoom(true);
			settings.setGeolocationEnabled(false);
			settings.setAppCacheEnabled(false);
			settings.setDatabaseEnabled(false);
			settings.setDomStorageEnabled(false);
			theme.setIncognitoTheme();
		}
		else {
			settings.setJavaScriptEnabled(utils.getJavaEnabled());
			settings.setDefaultFontSize(utils.getTextSize());
			settings.setDefaultFixedFontSize(utils.getTextSize());
			settings.setAppCacheEnabled(false);
			settings.setDatabaseEnabled(false);
			settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
			theme.setTheme();
			if (utils.getPluginsEnabled()) {
			    settings.setPluginState(WebSettings.PluginState.ON);
			}
			else
				settings.setPluginState(WebSettings.PluginState.OFF);
		}
		setLayerType(View.LAYER_TYPE_HARDWARE, null);
        searchEngine = utils.getSearchEngine();
		if(!utils.getCacheEnabled()){
			settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		}
		else {
			settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		}
		settings.setAppCacheEnabled(false);
	}
	/*
	* This method sets the current instance of the BeHeView to go private or not
	 */
	public void setPrivate(boolean Private) {
		isPrivate = Private;
		if (isPrivate){
		CookieManager.getInstance().setAcceptCookie(false);
		getSettings().setCacheMode(getSettings().LOAD_NO_CACHE);
		getSettings().setAppCacheEnabled(false);
		clearHistory();
		clearCache(true);
		clearFormData();
		getSettings().setSavePassword(false);
		getSettings().setSaveFormData(false);
		theme.setIncognitoTheme();
	}
	else{
		theme.setTheme();
	}
	}
	public void searchWeb(String query){
		switch (searchEngine){
			case 1:
				String google = "https://www.google.com/search?q=" + query.replace(" ","+");
				loadUrl(google);
			break;
			case 2:
				String bing = "http://www.bing.com/search?q=" + query.replace(" ","+");
				loadUrl(bing);
			break;
			case 3:
				String yahoo = "https://search.yahoo.com/search?p=" + query.replace(" ","+");
				loadUrl(yahoo);
			break;
			case 4:
				String duck = "https://duckduckgo.com/?q=" + query.replace(" ","+");
				loadUrl(duck);
			break;
			case 5:
				String ask = "http://www.ask.com/web?q=" + query.replace(" ","+");
			    loadUrl(ask);
			break;
			case 6:
			String wow = "http://www.wow.com/search?s_it=search-thp&v_t=na&q=" + query.replace(" ","+");
			loadUrl(wow);
			break;
		}
	}
	public boolean isPrivate(){
		return isPrivate;
	}
	public void findInPage(String searchText){
		found = searchText;
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
			findAllAsync(searchText);
		}
	    else{
			findAll(searchText);
		}
	}
    public void startReaderMode(){
		addJavascriptInterface(new IJavascriptHandler(WEB_ACTIVITY), "INTERFACE");
		loadUrl("javascript:window.INTERFACE.processContent(document.getElementsByTagName('body')[0].innerText,document.title);");
	}
	public void destroy(){
	  ViewGroup parent = (ViewGroup) this.getParent();
	  if( parent != null){
		  parent.removeView(this);
	  }
	  this.stopLoading();
	  this.onPause();
	  this.clearHistory();
	  this.clearCache(true);
	  this.setVisibility(View.GONE);
	  this.removeAllViews();
	  this.destroyDrawingCache();
	  super.destroy();
	}
	public void loadHomepage(){
		PreferenceUtils utils = new PreferenceUtils(WEB_ACTIVITY);
		if(utils.getHomePage().equals("default")) {
			HomePage page = new HomePage(this, WEB_ACTIVITY.getApplication());
			Void[] b = null;
			page.execute(b);
			text.setText(Html.fromHtml("<font color='#228B22'>" + getResources().getString(R.string.home) + "</font>"), TextView.BufferType.SPANNABLE);
		}
	    else{
			loadUrl(utils.getHomePage());
		}
	}
	public void setDesktop(){
		getSettings().setDisplayZoomControls(true);
		getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.111 Safari/537.36");
		setInitialScale(-10);
		getSettings().setBuiltInZoomControls(true);
		reload();
	}
	public void setMobile(){
		getSettings().setDisplayZoomControls(false);
		getSettings().setUserAgentString("Mozilla/5.0 (Linux; <Android Version>; <Build Tag etc.>) AppleWebKit/<WebKit Rev> (KHTML, like Gecko) Chrome/<Chrome Rev> Mobile Safari/<WebKit Rev> ");
		setInitialScale(0);
		reload();
	}
	public void loadHistory(){
		HystoryTask task = new HystoryTask(WEB_ACTIVITY,this);
		Void[] va = null;
		task.execute(va);
	}
	class IJavascriptHandler {
		Context mContext;
		IJavascriptHandler(Context c) {
			mContext = c;
		}
		@JavascriptInterface
		public void processContent(String aContent,String title) {
			final String content = aContent;
			//Intent mReading = new Intent(WEB_ACTIVITY, ReadingActivity.class);
		 //   mReading.putExtra("text",content);
		//	mReading.putExtra("title",title);
		//	WEB_ACTIVITY.startActivity(mReading);
		}
	}
    public void loadHistoty(){
		HystoryTask task = new HystoryTask(this.getContext(),this);
		Void[] d = null;
		task.execute(d);
	}
    public void setTitle(){
		if(getUrl().contains("file")){
			text.setText(Html.fromHtml("<font color='#228B22'>" + getResources().getString(R.string.home) + "</font>"), TextView.BufferType.SPANNABLE);
		}
		else{
			if(getUrl() != null){
				text.setText(getUrl());
			}
		}

	}
    public Drawable getScreenshot(){
		Picture pic = capturePicture();
		PictureDrawable draw = new PictureDrawable(pic);
		return draw;
	}
    public boolean isCurrentTab(){
		return FOCUS;
	}
    public void setIsCurrentTab(boolean focus){
		FOCUS = focus;
	}
    public void setSearchEngine(int engine){
		searchEngine = engine;
	}
    public Activity getActivity(){
		return WEB_ACTIVITY;
	}
    public void setNewParams(EditText txt, ProgressBar pBar, ActionBarActivity activity,boolean pvt){
		text = txt;
		P_BAR = pBar;
		WEB_ACTIVITY = activity;
		isPrivate = pvt;
		setWebChromeClient(new BeHeChromeClient(P_BAR,this));
		setWebViewClient(new BeHeWebClient(text,WEB_ACTIVITY,false,this));
		setDownloadListener(new CiobanDownloadListener(WEB_ACTIVITY, this));
		initializeSettings();
	}
    public void setMAtch(String t){
		found = t;
	}
    public String hasAnyMatches(){
		return found;
	}

}





