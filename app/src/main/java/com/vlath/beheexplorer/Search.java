package com.vlath.beheexplorer;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import android.util.*;
/**
 * Created by Administrator on 25.01.2016.
 */
public class Search extends ActionBarActivity {
  String toString;
    SearchView search;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;
String text;
    ListView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        setTitle("");
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String color = settings.getString("color", "#FFFFFF");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
        view = (ListView) findViewById(R.id.listquery);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("");
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.search_main);
        search = (SearchView) item.getActionView();
        search.setIconified(false);
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, list);
   view.setAdapter(adapter);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


String ne = newText.replace(" ","%");
                text = " http://suggestqueries.google.com/complete/search?output=toolbar&hl=en&q=" + newText;


                new Thread(new Runnable() {

                    @Override
                    public void run() {


                        try {
                            URL url = new URL(text);
                            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                            DocumentBuilder db = dbf.newDocumentBuilder();
                            Document doc = db.parse(new InputSource(url.openStream()));
                            doc.getDocumentElement().normalize();

                            NodeList nodeList = doc.getElementsByTagName("CompleteSuggestion");





                            for (int i = 0; i < 4; i++) {

                                Node node = nodeList.item(i);




                                Element fstElmnt = (Element) node;
                                NodeList nameList = fstElmnt.getElementsByTagName("suggestion");
                                Element nameElement = (Element) nodeList.item(0);
                                String suggestion = nameElement.getAttribute("data").toString();
                                list.add(suggestion);






                            }

                        } catch (Exception e) {
                           Log.e("Parse error","XML Pasing Excpetion = " + e);
                        }

                    }

                }).start();
                adapter.notifyDataSetChanged();

                return true;

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {


        }


        return super.onOptionsItemSelected(item);
    }

    @Override
public void onResume(){
        super.onResume();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        String color = settings.getString("color", "#FFFFFF");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

    }
}
