package com.living.roomrental.repository.local;

import android.content.SharedPreferences;

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

    public static String getWhoIsUser(SharedPreferences preferences){
        return preferences.getString("whoIsUser",null);
    }
}
