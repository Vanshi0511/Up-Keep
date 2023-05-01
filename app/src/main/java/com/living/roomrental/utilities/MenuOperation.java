package com.living.roomrental.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.R;
import com.living.roomrental.activity.auth.login.LoginActivity;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;

public class MenuOperation {

    public static void logOutUser(Context context){

        new AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_warning)
                .setMessage("Are you sure you want to Log Out?")
                .setTitle("Log Out")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        SharedPreferenceStorage.clearAllDataFromLocal(SharedPreferencesController.getInstance(context).getPreferences());
                        AppBoiler.navigateToActivityWithFinish(context, LoginActivity.class,null);
                        FirebaseAuth.getInstance().signOut();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    public static void deactivateAccount(){

    }
}
