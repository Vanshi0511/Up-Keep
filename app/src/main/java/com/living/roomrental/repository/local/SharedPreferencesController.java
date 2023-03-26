package com.living.roomrental.repository.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.living.roomrental.FirebaseController;
import com.living.roomrental.utilities.AppConstants;

public class SharedPreferencesController {

    private SharedPreferences preferences;
    public static SharedPreferencesController controller;

    private SharedPreferencesController(Context context){
        preferences = context.getSharedPreferences(AppConstants.SHARED_PREFERENCES_FILE_NAME,context.MODE_PRIVATE);
    }

    public static SharedPreferencesController getInstance(Context context){
        if(controller==null){
            controller = new SharedPreferencesController(context);
        }
        return controller;
    }

    public SharedPreferences getPreferences() {
        return preferences;
    }
}
