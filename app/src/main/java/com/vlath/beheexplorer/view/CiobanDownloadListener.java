/*
 Copyright 2016 Vlad Todosin
*/

package com.vlath.beheexplorer.view;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebView;

import com.vlath.beheexplorer.R;


public class CiobanDownloadListener implements DownloadListener {
   private WebView view;
    private ActionBarActivity mActivity;
    public CiobanDownloadListener(ActionBarActivity activity,WebView web){
       super();
       mActivity = activity;
       view = web;
   }
    @Override
    public void onDownloadStart(final String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        final String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                            String downloadSize = null;
                                if (contentLength > 0) {
                                     downloadSize = Formatter.formatFileSize(mActivity, contentLength);
                                   } else {
                                       downloadSize = "";
                                    }
                        String message = mActivity.getResources().getString(R.string.download);
                        builder.setTitle(fileName)
                       .setMessage(message + downloadSize + " ?")
                       .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               DownloadManager.Request request = new DownloadManager.Request(
                                       Uri.parse(url));
                               request.allowScanningByMediaScanner();
                               request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                               request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
                               DownloadManager dm = (DownloadManager) mActivity.getSystemService(Activity.DOWNLOAD_SERVICE);
                               dm.enqueue(request);
                           }
                       })

                      .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {

                          }
                      });
                builder.create();
                builder.show();

    }
}
