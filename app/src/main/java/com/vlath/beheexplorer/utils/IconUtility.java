package com.vlath.beheexplorer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.vlath.beheexplorer.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Copyright 2016 Vlad Todosin
 */
public class IconUtility extends AsyncTask<Void,Void,Bitmap>{
    public String mUrl;
    public Bitmap mDefaultBitmap;
    public Context mContext;
    public ImageView mImageView;
    public IconUtility(@NonNull ImageView view ,@NonNull Context context,@NonNull String url,@NonNull Bitmap defaultImage){
        mUrl = url;
        mDefaultBitmap = defaultImage;
        mContext = context;
        mImageView = view;
    }
    @Override
    protected Bitmap doInBackground(Void... params) {
        Bitmap mIcon = null;
        if (mUrl == null) {
            return mDefaultBitmap;
        }
        Context context = mContext;
        if (context == null) {
            return mDefaultBitmap;
        }
        File cache = context.getCacheDir();
        final Uri uri = Uri.parse(mUrl);
        if (uri.getHost() == null || uri.getScheme() == null) {
            return mDefaultBitmap;
        }
        final String hash = String.valueOf(uri.getHost().hashCode());
        final File image = new File(cache, hash + ".png");
        final String urlDisplay = uri.getScheme() + "://" + uri.getHost() + "/favicon.ico";
        // checks to see if the image exists
        if (!image.exists()) {
            FileOutputStream fos = null;
            InputStream in = null;
            try {
                // if not, download it...
                final URL urlDownload = new URL(urlDisplay);
                final HttpURLConnection connection = (HttpURLConnection) urlDownload.openConnection();
                connection.setDoInput(true);
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                connection.connect();
                in = connection.getInputStream();

                if (in != null) {
                    mIcon = BitmapFactory.decodeStream(in);
                }
                // ...and cache it
                if (mIcon != null) {
                    fos = new FileOutputStream(image);
                    mIcon.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                }

            } catch (Exception ignored) {
            } finally {
                 try {
                     fos.close();
                     in.close();
                 }
                 catch(Exception r){

                 }
            }
        } else {
            mIcon = BitmapFactory.decodeFile(image.getPath());
        }
        if (mIcon == null) {
            InputStream in = null;
            FileOutputStream fos = null;
            try {
                final URL urlDownload = new URL("https://www.google.com/s2/favicons?domain_url=" + uri.toString());
                final HttpURLConnection connection = (HttpURLConnection) urlDownload.openConnection();
                connection.setDoInput(true);
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                connection.connect();
                in = connection.getInputStream();

                if (in != null) {
                    mIcon = BitmapFactory.decodeStream(in);
                }

                if (mIcon != null) {
                    fos = new FileOutputStream(image);
                    mIcon.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                }

            } catch (Exception e) {

            } finally {
                   try{
                       in.close();
                       fos.close();
                   }
                   catch(Exception i){

                   }
           }
        }
        if (mIcon == null) {
            return mDefaultBitmap;
        } else {
            return mIcon;
        }
    }
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        Bitmap img = bitmap;
        ImageView view = mImageView;
        if (view != null) {
            view.setImageBitmap(img);
        }
    }
}
