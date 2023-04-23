package com.living.roomrental.utilities;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.activity.auth.login.LoginActivity;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;

public class MenuOperation {

    public static void logOutUser(Context context){

        SharedPreferenceStorage.clearAllDataFromLocal(SharedPreferencesController.getInstance(context).getPreferences());
        AppBoiler.navigateToActivityWithFinish(context, LoginActivity.class,null);
        FirebaseAuth.getInstance().signOut();
    }
}
