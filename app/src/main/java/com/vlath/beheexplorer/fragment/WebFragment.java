package com.vlath.beheexplorer.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.utils.HystoryTask;
import com.vlath.beheexplorer.utils.PreferenceUtils;
import com.vlath.beheexplorer.utils.ThemeUtils;
import com.vlath.beheexplorer.view.AnimatedProgressBar;
import com.vlath.beheexplorer.view.BeHeChromeClient;
import com.vlath.beheexplorer.view.BeHeView;
import com.vlath.beheexplorer.view.BeHeWebClient;

public class WebFragment extends Fragment {
    SwipeRefreshLayout swipeLayout;
    WebView web;
    ProgressBar BAR;
    Activity act;
    EditText TXT;
    public int searchEngine;
    boolean history = false;
    public void setArgs(ProgressBar pBar, EditText ext, final Activity activity) {
        BAR = pBar;
        TXT = ext;
        act = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.web_fragment, container, false);
        web = new WebView(v.getContext());
        initializeWebSettings(BAR, TXT);
        web.setLayoutParams(new SwipeRefreshLayout.LayoutParams(SwipeRefreshLayout.LayoutParams.MATCH_PARENT, SwipeRefreshLayout.LayoutParams.MATCH_PARENT));
        PreferenceUtils utils = new PreferenceUtils(v.getContext());
        setSearchEngine(utils.getSearchEngine());
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeLayout.addView(web);
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {


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
        TXT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TXT.setCursorVisible(true);
            }
        });
        TXT.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               TXT.setCursorVisible(true);
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String toSearch;
                    toSearch = TXT.getText().toString();

                    if (toSearch.contains("http://") || toSearch.contains("https://") || toSearch.contains("www")) {
                          web.loadUrl(toSearch);
                    } else {
                          searchWeb(toSearch);
                    }
                    View view = act.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    TXT.setCursorVisible(false);
                    return true;
                } else {
                    TXT.setCursorVisible(false);
                    View view = act.getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }

                    return true;
                }

            }
        });
        if(history){
            HystoryTask task = new HystoryTask(act,web);
            Void[] va = null;
            task.execute(va);
        }
        return v;
    }

    public void initializeWebSettings(ProgressBar pBar, EditText txt) {
        web.setWebChromeClient(new BeHeChromeClient(pBar));
        web.setWebViewClient(new BeHeWebClient(txt,act));
        PreferenceUtils utils = new PreferenceUtils(getContext());
        web.getSettings().setJavaScriptEnabled(utils.getJavaEnabled());
        web.getSettings().setAppCacheEnabled(utils.getCacheEnabled());
        web.getSettings().setDefaultFontSize(utils.getTextSize());
        web.getSettings().setDefaultFixedFontSize(utils.getTextSize());
        ThemeUtils theme = new ThemeUtils((ActionBarActivity) getActivity());
        theme.setTheme();
        if (utils.getPluginsEnabled()) {
            web.getSettings().setPluginState(WebSettings.PluginState.ON);
        } else {
            web.getSettings().setPluginState(WebSettings.PluginState.OFF);
        }
        web.loadUrl(utils.getHomePage());
    }

    public void searchWeb(String query) {
        switch (searchEngine) {
            case 1:
                String google = "https://www.google.com/search?q=" + query.replace(" ", "+");
                web.loadUrl(google);
                break;
            case 2:
                String bing = "http://www.bing.com/search?q=" + query.replace(" ", "+");
                web.loadUrl(bing);
                break;
            case 3:
                String yahoo = "https://search.yahoo.com/search?p=" + query.replace(" ", "+");
                web.loadUrl(yahoo);
                break;
            case 4:
                String duck = "https://duckduckgo.com/?q=" + query.replace(" ", "+");
                web.loadUrl(duck);
                break;
            case 5:
                String ask = "http://www.ask.com/web?q=" + query.replace(" ", "+");
                web.loadUrl(ask);
                break;
            case 6:
                String wow = "http://www.wow.com/search?s_it=search-thp&v_t=na&q=" + query.replace(" ", "+");
                web.loadUrl(wow);
                break;
        }

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        web.destroy();
    }
    public void setSearchEngine(int search){
        searchEngine = search;
    }
    public void showHistory(boolean bol){
        history = bol;
    }
}