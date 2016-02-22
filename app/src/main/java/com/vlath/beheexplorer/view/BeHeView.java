package com.vlath.beheexplorer.view;



import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

import com.vlath.beheexplorer.activity.BeHeActivity;
import com.vlath.beheexplorer.activity.ReadingActivity;
import com.vlath.beheexplorer.utils.PreferenceUtils;
import com.vlath.beheexplorer.utils.ThemeUtils;


@SuppressWarnings("unused")


public class BeHeView extends WebView{
	ThemeUtils theme;
	private String TEXT = "1";
	private boolean PRIVATE;
	private int searchEngine;
	private ProgressBar P_BAR;
	private boolean ico ;
	BeHeActivity WEB_ACTIVITY;
	AutoCompleteTextView text;
	private String PAGE_TITLE;
	public int GOOGLE_SEARCH = 1;
	public int BING_SEARCH = 2;
	public int YAHOO_SEARCH = 3;
	public int DUCKDUCKGO_SEARCH = 4;
	public int ASK_SEARCH = 5;
	public int WOW_SEARCH = 6;
	public BeHeView(Context context,BeHeActivity activity,ProgressBar pBar,boolean isPrivate,final AutoCompleteTextView txt)  {
		super(context);
		theme = new ThemeUtils(activity);
		PRIVATE = isPrivate;
		P_BAR = pBar;
		WEB_ACTIVITY = activity;
		text = txt;
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
	setWebChromeClient(new BeHeChromeClient(WEB_ACTIVITY, P_BAR));
	setWebViewClient(new BeHeWebClient(WEB_ACTIVITY, text));
	setDownloadListener(new CiobanDownloadListener(WEB_ACTIVITY,this));
	}
	public void initializeSettings(){
      PreferenceUtils utils = new PreferenceUtils(WEB_ACTIVITY.getApplicationContext());
	  getSettings().setJavaScriptEnabled(utils.getJavaEnabled());
	  getSettings().setAppCacheEnabled(utils.getCacheEnabled());
	  getSettings().setDefaultFontSize(utils.getTextSize());
	  getSettings().setDefaultFixedFontSize(utils.getTextSize());
	  setSearchEngine(utils.getSearchEngine());
	  if(utils.getPluginsEnabled()){
		  getSettings().setPluginState(WebSettings.PluginState.ON);
	  }
	  else{
		  getSettings().setPluginState(WebSettings.PluginState.OFF);
	  }
	}
	public void setPrivate(boolean isPrivate) {
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

	public void setIcon(boolean flag){
		ico = flag;
	}
	public void initialize(boolean javaScript,boolean plugins,boolean cache,int textSize){

	}
	public void setSearchEngine(int search){
	  searchEngine = search; 
   }
	public void search(String searchQuery){
		if(searchEngine == 1)
		{
			String google = "https://www.google.ro/search?q=" + searchQuery;
			loadUrl(google);
		}
		if(searchEngine == 2)
		{
			String bing = "http://www.bing.com/search?q=" + searchQuery;
			loadUrl(bing);
		}
		if(searchEngine == 3)
		{
			String yahoo = "MxMARvcmlnaW4Dchttps://search.yahoo.com/search;_ylt=A0LEVzFsgOlVd3wAL7dXNyoA;_ylc=X1MDMjc2NjY3OQRfcgMyBGZyA3NmcARncHJpZANESnhIOUtPdFJfMnE2SklwaVliWWxBBG5fcnNsdAMwBG5fc3VnZw2VhcmNoLnlhaG9vLmNvbQRwb3MDMARwcXN0cgMEcHFzdHJsAwRxc3RybAM2BHF1ZXJ5A25lIGF0YQR0X3N0bXADMTQ0MTM2NjE0MQ--?p=" + searchQuery;
		    loadUrl(yahoo);
		}
		if(searchEngine == 4)
		{
			String duck ="https://duckduckgo.com/?q=" + searchQuery;
			loadUrl(duck);
		}
		if(searchEngine == 5)
		{
			String ask = "http://www.ask.com/web?q=" + searchQuery;
		}
		if(searchEngine == 6)
		{
			String wow = "http://www.wow.com/search?s_it=search-thp&v_t=na&q=" +searchQuery;
			loadUrl(wow);
		}
	    
	
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




