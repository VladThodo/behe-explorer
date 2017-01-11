/*
   Copyright 2016 Vlad Todosin
*/

package com.vlath.beheexplorer.controllers;



import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.vlath.beheexplorer.view.BeHeView;


import java.util.ArrayList;
import java.util.List;

public class TabManager {
   private static List<BeHeView> mViewsList = new ArrayList<BeHeView>();
   static BeHeView currentTab;
   private static NavigationView VIEW;
   public static void addTab(BeHeView view){
       mViewsList.add(view);
   }
   @Nullable
    public static List<BeHeView> getList(){
        return mViewsList;
    }

   private static void deleteTab(int position){
       if (position < 0 || position >= mViewsList.size()) {
       }
       else{
           BeHeView tab = mViewsList.get(position);
           mViewsList.remove(position);
       }
   }
    public static void removeTab(BeHeView view){
        int index = mViewsList.indexOf(view);
        if(index != 0){
          mViewsList.remove(view);
        }
        else {
            BeHeView behe = mViewsList.get(index + 1);
            mViewsList.set(0,behe);
            mViewsList.remove(index + 1);
            mViewsList.remove(view);
            setCurrentTab(behe);
        }
    }
    public static BeHeView getCurrentTab(){
        if(currentTab != null) {
            return currentTab;
        }
        else{
            return mViewsList.get(0);
        }
    }
    public static void setNavigationView(NavigationView view){
        VIEW = view;
    }
    public static void updateTabView(){
        VIEW.getMenu().clear();
        for(int i = 0;i < mViewsList.size();i++) {
            BeHeView  view = mViewsList.get(i);
            VIEW.getMenu().add(view.getTitle());
            if(view == TabManager.currentTab){
                VIEW.getMenu().getItem(i).setChecked(true);
            }
        }
        for(int i = 0;i< VIEW.getMenu().size();i++){
            ColorGenerator gen = ColorGenerator.MATERIAL;
            int col = gen.getRandomColor();
            TextDrawable drawable = TextDrawable.builder().buildRound(String.valueOf(i),col);
            VIEW.getMenu().getItem(i).setIcon(drawable);


        }
    }
    public static void setCurrentTab(BeHeView view){
        for(BeHeView behe : getList()){
            behe.setIsCurrentTab(false);
        }
        view.setIsCurrentTab(true);
        currentTab = view;
    }
    public static BeHeView getTabByTitle(String title){
        for(BeHeView view : getList()){
            String web = view.getTitle();
            if(web.matches(title)){
                return view;
            }
            else{
                return null;
            }
        }
        return null;
    }
    public static BeHeView getTabAtPosition(MenuItem menuItem){
        List<MenuItem> items = new ArrayList<>();
        Menu menu = VIEW.getMenu();
        for(int i = 0; i < menu.size();i++){
            MenuItem item = menu.getItem(i);
            items.add(item);
        }
        int index = items.indexOf(menuItem);
        BeHeView view = getList().get(index);
        return view;
    }
    public static void removeAllTabs(){
       mViewsList.clear();
    }

}

