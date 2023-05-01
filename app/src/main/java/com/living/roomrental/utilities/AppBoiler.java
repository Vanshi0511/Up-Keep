package com.living.roomrental.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.AlertDialogListener;
import com.living.roomrental.ViewProfileListener;
import com.living.roomrental.DialogListener;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.ImagePickerDialogListener;
import com.living.roomrental.PopupWindowsMenuListener;
import com.living.roomrental.R;
import com.living.roomrental.activity.profile.create.CreateProfileActivity;
import com.living.roomrental.activity.profile.edit.EditProfileActivity;
import com.living.roomrental.activity.profile.model.ProfileModel;

import de.hdodenhof.circleimageview.CircleImageView;

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
        dialog.show();
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

    public static void navigateToActivityForResult(Context context , Class nextActivity , Bundle bundle , ActivityResultLauncher<Intent> mapLocationLauncher){
        Intent intent = new Intent(context,nextActivity);
        if(bundle!=null)
            intent.putExtras(bundle);
       mapLocationLauncher.launch(intent);
    }

    public static void showSnackBarForInternet(Context context , View view){
       Snackbar.make(view,"No Internet Found",Snackbar.LENGTH_SHORT)
               .setBackgroundTint(context.getColor(R.color.red_400))
               .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
               .show();
       // snackbar.showAction -------> operations like undo
    }

    public static Dialog showImagePickerDialog(Context context , ImagePickerDialogListener listener){
        Dialog dialog =new Dialog(context);
        dialog.setContentView(R.layout.layout_image_picker_chooser_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        LinearLayout imageFromCameraLayout, imageFromGalleryLayout , removeImageLayout;
        imageFromCameraLayout = dialog.findViewById(R.id.imageFromCameraLayout);
        imageFromGalleryLayout = dialog.findViewById(R.id.imageFromGalleryLayout);
        removeImageLayout = dialog.findViewById(R.id.removeImageLayout);

        if(context instanceof CreateProfileActivity || context instanceof EditProfileActivity){
            removeImageLayout.setVisibility(View.VISIBLE);
        }

        imageFromCameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickCamera();
            }
        });

        imageFromGalleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickGallery();
            }
        });
        removeImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickRemove();
            }
        });

        dialog.show();
        return dialog;
    }

    public static void showProfileDialog(Context context , ProfileModel model , ViewProfileListener listener , String receiverKey){

        TextView name, address , about , contact , email;
        CircleImageView profileImageView;
        MaterialCardView contactCardView , chatCardView;

        Dialog dialog =new Dialog(context);
        dialog.setContentView(R.layout.layout_view_profile_dialog);


        name = dialog.findViewById(R.id.nameTextView);
        address = dialog.findViewById(R.id.addressTextView);
        about = dialog.findViewById(R.id.aboutTextView);
        contact = dialog.findViewById(R.id.contactTextView);
        email = dialog.findViewById(R.id.emailTextView);
        profileImageView = dialog.findViewById(R.id.profileImage);
        contactCardView = dialog.findViewById(R.id.contactCardView);
        chatCardView = dialog.findViewById(R.id.chatCardView);

        name.setText(model.getName()+" ("+model.getOccupation()+")");
        address.setText(model.getAddress());
        about.setText(model.getBio());
        contact.setText(model.getContactNo());
        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        if(!Validation.isStringEmpty(model.getImageUrl()))
                Glide.with(context).load(model.getImageUrl()).into(profileImageView);

        contactCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickContact(model.getContactNo());
            }
        });

        chatCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickChat(receiverKey);
            }
        });


        Window window = dialog.getWindow();
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        InsetDrawable inset = new InsetDrawable(back, 20);
        window.setBackgroundDrawable(inset);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    public static void showCustomSnackBar( View view , String msg){
        Snackbar.make(view,msg,Snackbar.LENGTH_SHORT)
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show();
        // snackbar.showAction -------> operations like undo
    }

    public static void showAlertDialog(Context context , int iconId , String text , String message , AlertDialogListener listener){

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setIcon(iconId);
                dialog.setTitle(text);
                dialog.setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onClickPositiveButton();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onClickNegativeButton();
                    }
                })
                .show();
    }

    public static void showMenuPopupWindow(Context context ,View container, PopupWindowsMenuListener listener){


        View popupView = LayoutInflater.from(context).inflate(R.layout.layout_menu_popup_window,null,false);

        popupView.findViewById(R.id.logOutCardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickLogOut();
            }
        });
        popupView.findViewById(R.id.deactivateCardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickDeactivate();
            }
        });
        popupView.findViewById(R.id.helpCardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickHelp();
            }
        });
        popupView.findViewById(R.id.aboutCardView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickAbout();
            }
        });

        PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);

        System.out.println("============ popup=======");
        popupWindow.showAsDropDown(container,0,0);
        //popupWindow.showAtLocation(container , Gravity.CENTER, 0, 0);
        //return popupWindow;
    }

}
