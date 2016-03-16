/*
   Copyright 2016 Vlad Todosin
*/

package com.vlath.beheexplorer.controllers;

import android.support.annotation.Nullable;


import com.vlath.beheexplorer.view.BeHeView;

import java.util.ArrayList;

public class TabManager {
   private static ArrayList<BeHeView> mViewsList = new ArrayList<BeHeView>();

   public static void addTab(BeHeView view){
       mViewsList.add(view);
   }
   @Nullable
   private static BeHeView getTabAtPosition(final int position){
       if (position < 0 || position >= mViewsList.size()) {
           return null;
       }
      else {
           return mViewsList.get(position);
       }
    }

   private static void freeAllData(){
       for (BeHeView view : mViewsList){
           view.freeMemory();
           view.clearCache(true);
           view.clearHistory();
           view.clearFormData();
       }
   }
   private static void deleteTab(int position){
       if (position < 0 || position >= mViewsList.size()) {
       }
       else{
           BeHeView tab = mViewsList.get(position);
           mViewsList.remove(position);
           tab.destroy();
       }
   }

}
