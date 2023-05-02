package com.living.roomrental.utilities;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class PermissionUtils {

    public static final int MAP_REQUEST_CODE = 102;
    public static final int CALL_REQUEST_CODE = 103;


    public static boolean isPermissionGranted(String[] grantPermissions, int[] grantResults){
        int permissionSize = grantPermissions.length;
        for (int i = 0; i < permissionSize; i++) {
            if (Manifest.permission.ACCESS_FINE_LOCATION.equals(grantPermissions[i])) {
                return grantResults[i] == PackageManager.PERMISSION_GRANTED;
            }
        }
        return false;
    }

    public static boolean isPermissionProvided(Activity context , Context application) {

        if (ContextCompat.checkSelfPermission(application, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(context,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MAP_REQUEST_CODE);

            return false;
        } else{
            //permission given
            return true;
        }
    }

    public static boolean isPermissionGrantedForCall(Context context){

        if (ContextCompat.checkSelfPermission(context,android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
             // else block means user has already accepted.And make your phone call here.
            return true;
        }
    }

    public static void requestForCall(Activity context){
        ActivityCompat.requestPermissions(context,
                new String[]{android.Manifest.permission.CALL_PHONE},
                CALL_REQUEST_CODE);
    }
}
