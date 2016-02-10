package com.vlath.beheexplorer;



import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

@SuppressWarnings("unused")
public class BeHe {
public static class webkit{
public  enum SearchEngine{
	
}
public class BeHeView extends WebView{
    private Activity WEB_ACTIVITY;
    private TabAdapter TAB_ADAPTER;
	private Bitmap[] TAB_ICONS = new Bitmap[]{};
	private String PAGE_TITLE;
	public BeHeView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {


			}

			@Override
			public void onPageFinished(WebView view, String url) {
                 setActivityTitle(WEB_ACTIVITY);
			}

		});


	}
	public int GOOGLE_SEARCH = 1;
	public int BING_SEARCH = 2;
	public int YAHOO_SEARCH = 3;
	public int DUCKDUCKGO_SEARCH = 4;
	public int ASK_SEARCH = 5;
	public int WOW_SEARCH = 6;
	public void changeFavicon(){

	}
	public void setActivityTitle(Activity activity){
		WEB_ACTIVITY = activity;
		WEB_ACTIVITY.setTitle(getTitle());
}
  
   
   
	
  private int searchEngine;
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
			String yahoo = "https://search.yahoo.com/search;_ylt=A0LEVzFsgOlVd3wAL7dXNyoA;_ylc=X1MDMjc2NjY3OQRfcgMyBGZyA3NmcARncHJpZANESnhIOUtPdFJfMnE2SklwaVliWWxBBG5fcnNsdAMwBG5fc3VnZwMxMARvcmlnaW4Dc2VhcmNoLnlhaG9vLmNvbQRwb3MDMARwcXN0cgMEcHFzdHJsAwRxc3RybAM2BHF1ZXJ5A25lIGF0YQR0X3N0bXADMTQ0MTM2NjE0MQ--?p=" + searchQuery;
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
	   
	
	   
}  

}

}
