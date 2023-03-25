package com.living.roomrental.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.living.roomrental.DialogListener;
import com.living.roomrental.R;

public class AppBoiler {

    public static void setInputLayoutErrorDisable(TextInputLayout layout){
        layout.setError(null);
        layout.setErrorEnabled(false);
    }

    public static Dialog setProgressDialog(Context context){
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_progress_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return dialog;
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static Dialog customDialogWithBtn(Context context , String message , int dialogImageResID , DialogListener dialogListener){
        ImageView dialogImage;
        TextView dialogMessage;

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_custom_dialog);
        dialogImage = dialog.findViewById(R.id.dialogImage);
        dialogMessage = dialog.findViewById(R.id.dialogMessage);

        dialogImage.setImageResource(dialogImageResID);
        dialogMessage.setText(message);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        LinearLayout dialogBtn = dialog.findViewById(R.id.dialogBtn);


        dialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              dialogListener.onClick();
            }
        });
        dialog.show();
        return dialog;
    }

    public static void navigateToActivity(Context context , Class nextActivity , Bundle bundle){
        Intent intent = new Intent(context,nextActivity);
        if(bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void navigateToActivityWithFinish(Context context , Class nextActivity , Bundle bundle){
        Intent intent = new Intent(context,nextActivity);
        if(bundle != null)
            intent.putExtras(bundle);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    public static void showSnackBarForInternet(Context context , View view){
       Snackbar.make(view,"No Internet Found",Snackbar.LENGTH_SHORT)
               .setBackgroundTint(context.getColor(R.color.red_400))
               .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
               .show();
       // snackbar.showAction -------> operations like undo
    }


}
