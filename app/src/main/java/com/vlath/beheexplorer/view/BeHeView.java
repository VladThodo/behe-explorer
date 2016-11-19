/*
 Copyright 2016 Vlad Todosin
*/


package com.vlath.beheexplorer.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.vlath.beheexplorer.activity.BeHeActivity;
import com.vlath.beheexplorer.activity.ReadingActivity;
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
	private int searchEngine;
	private ProgressBar P_BAR;
	private boolean ico ;
	ActionBarActivity WEB_ACTIVITY;
	EditText text;
	private String PAGE_TITLE;
	public static int GOOGLE_SEARCH = 1;
	public static int BING_SEARCH = 2;
	public static int YAHOO_SEARCH = 3;
	public static int DUCKDUCKGO_SEARCH = 4;
	public static int ASK_SEARCH = 5;
	public static int WOW_SEARCH = 6;
	/*
	* Public constructor of BeHeView
	 */
	public BeHeView(Context context, ActionBarActivity activity, ProgressBar pBar, boolean Private, final EditText txt)  {
		super(context);
		theme = new ThemeUtils(activity);
		isPrivate = Private;
		P_BAR = pBar;
		WEB_ACTIVITY = activity;
		text = txt;
		this.getSettings().setLoadWithOverviewMode(true);
		setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN && canGoBack()) {
					goBack();
					return true;
				}
				return false;
			}
		});
	setWebChromeClient(new BeHeChromeClient(P_BAR));
	//setWebViewClient(new BeHeWebClient(text));
	setDownloadListener(new CiobanDownloadListener(WEB_ACTIVITY, this));
	}
	/*
	* This method initializes the view settings and the view itself
	 */
	public void initializeSettings(){
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
		else {
			PreferenceUtils utils = new PreferenceUtils(WEB_ACTIVITY.getApplicationContext());
			getSettings().setJavaScriptEnabled(utils.getJavaEnabled());
			getSettings().setAppCacheEnabled(utils.getCacheEnabled());
			getSettings().setDefaultFontSize(utils.getTextSize());
			getSettings().setDefaultFixedFontSize(utils.getTextSize());
			setSearchEngine(utils.getSearchEngine());
			theme.setTheme();
			if (utils.getPluginsEnabled()) {
				getSettings().setPluginState(WebSettings.PluginState.ON);
			} else {
				getSettings().setPluginState(WebSettings.PluginState.OFF);
			}
		}
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

	/*
	* The app doesn't show page's icon instead of the actionbar toggle image anymore
	* I took this decision because that design looked ugly
	* Also,this method won't be avalabile in BeHeView 1.0.02
	* No substitution for it will be provided
	 */
	@Deprecated
	public void setIcon(boolean flag){
		ico = flag;
	}
	/*
	*  TODO use initializeSettings() instead
	 */
	@Deprecated
	public void initialize(boolean javaScript,boolean plugins,boolean cache,int textSize){

	}
	public void setSearchEngine(int search){
	  searchEngine = search; 
   }
    /*
	* This method has a very bad handling of white spaces and can cause bad errors
	* Also it won't be avalabile for use in BeHeView version 1.0.0.2
	* Please use searchWeb() instead
	*/
	@Deprecated
	public void search(String searchQuery) {
		if(searchEngine == 1) {
			String google = "https://www.google.com/search?q=" + searchQuery;
			loadUrl(google);
		}
		if (searchEngine == 2) {
			String bing = "http://www.bing.com/search?q=" + searchQuery;
			loadUrl(bing);
		}
		if (searchEngine == 3) {
			String yahoo = "https://search.yahoo.com/search?p=" + searchQuery;
			loadUrl(yahoo);
		}
		if (searchEngine == 4) {
			String duck = "https://duckduckgo.com/?q=" + searchQuery;
			loadUrl(duck);
		}
		if (searchEngine == 5) {
			String ask = "http://www.ask.com/web?q=" + searchQuery;
		}
		if (searchEngine == 6) {
			String wow = "http://www.wow.com/search?s_it=search-thp&v_t=na&q=" + searchQuery;
			loadUrl(wow);
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
	}
	class IJavascriptHandler {
		Context mContext;
		IJavascriptHandler(Context c) {
			mContext = c;
		}
		@JavascriptInterface
		public void processContent(String aContent,String title) {
			final String content = aContent;
			Intent mReading = new Intent(WEB_ACTIVITY, ReadingActivity.class);
		    mReading.putExtra("text",content);
			mReading.putExtra("title",title);
			WEB_ACTIVITY.startActivity(mReading);
		}
	}
}




