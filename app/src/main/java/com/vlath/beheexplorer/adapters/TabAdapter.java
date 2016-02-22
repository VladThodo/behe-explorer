package com.vlath.beheexplorer.adapters;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vlath.beheexplorer.R;

import java.util.ArrayList;

public class TabAdapter extends BaseAdapter{
    View rowView;
    ArrayList<String> names;
    Context context;
    ArrayList<Bitmap> images;
    private static LayoutInflater inflater=null;
    public TabAdapter(Activity mainActivity, ArrayList<String> name, ArrayList<Bitmap> prgmImages,ListView list) {
        // TODO Auto-generated constructor stub
        names= name;
        context=mainActivity;
        images=prgmImages;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public class Holder
    {
        TextView tv;
        ImageView img;
        ImageView img_del;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();

        rowView = inflater.inflate(R.layout.tab_item,null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.img_del=(ImageView) rowView.findViewById(R.id.imageView2);
        holder.tv.setText(names.get(position));
        holder.img_del.setImageResource(R.drawable.del);
        holder.img.setImageResource(R.drawable.history);

        holder.img_del.setOnClickListener(new OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 names.remove(position);
                                                 notifyDataSetChanged();
                                             }
                                         }
       );
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + names.get(position), Toast.LENGTH_LONG).show();
            }
        });
        return rowView;
    }

}