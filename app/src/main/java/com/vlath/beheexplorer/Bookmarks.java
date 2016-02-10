
       package com.vlath.beheexplorer;

         

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import com.vlath.beheexplorer.TabAdapter;
import com.vlath.beheexplorer.MainActivity;
	   public class Bookmarks extends ActionBarActivity  {
	File file = new File(Environment.getExternalStorageDirectory() + "/BeHe ExploreR");

	File toWrite = new File(file + "/bookmarks.oi");
	String k;
		   HashMap<String,HashMap<Bitmap,String>> map = new HashMap<String,HashMap<Bitmap, String>>();
		   ArrayList<Bitmap> images = new ArrayList<Bitmap>();
		   ArrayList<String> names = new ArrayList<String>();
		   ArrayList<String> url = new ArrayList<String>();
ListView list;
ArrayList<String> mylist = new ArrayList<String>(); 

	TabAdapter adapter;
@SuppressWarnings("unchecked")
@Override
protected void onCreate(Bundle savedInstanceState) {
    	        super.onCreate(savedInstanceState);
           setContentView(R.layout.bookmarks);

    	          setTitle("Bookmarks");
           list=(ListView)findViewById(R.id.listbook);
	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
	String color = settings.getString("color", "#FFFFFF");
	getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));

                	 
                try{
                	
                	 ObjectInputStream ois = new ObjectInputStream(new FileInputStream(toWrite));
                	Object result = ois.readObject();
                	map = (HashMap<String,HashMap<Bitmap,String>>)result;
                	 ois.close();
                
                }
                catch(Exception e)
                {
                	this.finish();
                }
	                names.addAll(map.keySet());
	          for(String s : names){
				  HashMap<Bitmap,String> mi = map.get(s);
				  images.addAll(mi.keySet());
			  }

	                list.setAdapter(adapter);
	                adapter.notifyDataSetChanged();
         	}         

@Override
public void onPostCreate(Bundle savedInstanceStat)
{
	  super.onPostCreate(savedInstanceStat);
	 /* list.setOnItemLongClickListener(new OnItemLongClickListener() {

		  @Override
		  public boolean onItemLongClick(AdapterView<?> parent, View view,
										 final int position, long id) {

			  CharSequence colors[] = new CharSequence[]{"Delete"};
			  AlertDialog.Builder builder = new AlertDialog.Builder(Bookmarks.this);
			  builder.setTitle("Pick an action");
			  builder.setItems(colors, new DialogInterface.OnClickListener() {

				  @Override
				  public void onClick(DialogInterface dialog, int which) {
					  try {

						  ObjectInputStream jj = new ObjectInputStream(new FileInputStream(file));
						  Object res = jj.readObject();
						  jj.close();
						  file.delete();
						  map = (HashMap) res;
						  String name = list.getItemAtPosition(position).toString();
						  map.remove(name);
						  ObjectOutputStream ous = new ObjectOutputStream(new FileOutputStream(file));
						  ous.writeObject(map);
						  ous.close();
						  Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT);
						  mylist.addAll(map.keySet());
						  Intent intent = getIntent();
						  finish();
						  startActivity(intent);
					  } catch (Exception e) {

					  }
				  }
			  });
			  builder.show();

			  return true;
		  }

	  }); */
	  list.setOnItemClickListener(new OnItemClickListener() {


		  @Override
		  public void onItemClick(AdapterView<?> parent, View view, int position,
								  long id) {
			  String hh = list.getItemAtPosition(position).toString();
			  String k = map.get(hh).toString();
			  Intent gg = new Intent(getApplicationContext(), MainActivity.class);
			  gg.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			  gg.putExtra("webb", k);
			  startActivity(gg);

		  }

	  });

}
@Override
 public void onResume(){
	super.onResume();
	SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
	String color = settings.getString("color", "#FFFFFF");
	getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
}
}     
 
 
 
 
	
	
	