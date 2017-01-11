package com.vlath.beheexplorer.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.IntegerRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.vlath.beheexplorer.R;
import com.vlath.beheexplorer.controllers.TabManager;
import com.vlath.beheexplorer.utils.PreferenceUtils;
import com.vlath.beheexplorer.view.BeHeView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class SettingsFragment extends PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        Preference pref = findPreference("change");
        pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra("crop", "true");
                photoPickerIntent.putExtra("return-data", true);
                photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
                startActivityForResult(photoPickerIntent, 1);
                return true;
            }
        });
        final  Preference prf = findPreference("home_page");
        prf.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                if (o.toString().equals("custom")) {
                    final Context context = getActivity();
                    LayoutInflater li = LayoutInflater.from(context);
                    View promptsView = li.inflate(R.layout.promt, null);
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    alertDialogBuilder.setView(promptsView);
                    final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton(R.string.ok,
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("home_page", userInput.getText().toString()).commit();
                                        }
                                    });
                    alertDialogBuilder.setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                   AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                else{
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("home_page", "default").commit();
                    prf.setDefaultValue("default");
                }
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    final Bitmap selectedBitmap = extras.getParcelable("data");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FileOutputStream writer = null;
                            File image = new File(getActivity().getFilesDir(), "drawer_image.png");
                            try {
                                writer = new FileOutputStream(image, false);
                                selectedBitmap.compress(Bitmap.CompressFormat.PNG, 100, writer);
                                writer.flush();
                                writer.close();
                            } catch (Exception e) {
                            }
                        }
                    }).start();
                }
            }
        }

    }

