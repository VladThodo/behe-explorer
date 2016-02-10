package com.vlath.beheexplorer;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;


public class TabActivity extends ActionBarActivity{
	ArrayAdapter adapter;
ArrayList<String> listText = new ArrayList<String>();
ArrayList<Bitmap> listImage = new ArrayList<Bitmap>();
HashMap<String,Bitmap> image = new HashMap<String,Bitmap>();
HashMap<String,Bundle> url = new HashMap<String,Bundle>();
@Override
protected void onCreate(Bundle savedInstanceState)
{
	super.onCreate(savedInstanceState);
	setContentView(R.layout.tab);
	GridView v = (GridView)findViewById(R.id.gridView1);
    adapter = new ArrayAdapter(this,android.R.layout.simple_gallery_item,listImage);
v.setAdapter(adapter);
	

	File file = new File("/sdcard/BeHeExploreR/tabs", "pictures.oi");
	  File fil = new File("/sdcard/BeHeExploreR/tabs", "links.oi");
try {
	ObjectInputStream obj = new ObjectInputStream(new FileInputStream(file));
    image =(HashMap<String,Bitmap>) obj.readObject();
    obj.close();
} catch (Exception e) {
	Toast.makeText(getApplicationContext(), e.toString(),Toast.LENGTH_LONG);
	

}

listImage.addAll(image.values());
adapter.notifyDataSetChanged();

}
}