package com.living.roomrental.repository.local;

import android.content.SharedPreferences;

import java.util.ArrayList;

public class SharedPreferenceStorage {

    public static void setUidOfUser(SharedPreferences preferences , String uid){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userId",uid);
        editor.apply();
    }

    public static String getUidOfUser(SharedPreferences preferences){
        return preferences.getString("userId",null);
    }

    public static void setProfileStatusOfUser(SharedPreferences preferences , String whoIsUser){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("whoIsUser",whoIsUser);
        editor.apply();
    }

    public static void setUserExtraData(SharedPreferences preferences , String userName , String userProfileImageUrl){

        System.out.println("======= User extra data stored ===========");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName",userName);
        editor.putString("userProfileUrl",userProfileImageUrl);
        editor.apply();
    }

    public static ArrayList<String> getUserExtraData(SharedPreferences preferences){
        ArrayList<String> list = new ArrayList<>();
        list.add(preferences.getString("userName","User"));
        list.add(preferences.getString("userProfileUrl",null));
        return list;
    }


    public static String getWhoIsUser(SharedPreferences preferences){
        return preferences.getString("whoIsUser",null);
    }
}
