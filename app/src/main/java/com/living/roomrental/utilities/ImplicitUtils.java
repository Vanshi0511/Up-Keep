package com.living.roomrental.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

public class ImplicitUtils {

    public static Intent getIntentForImagePickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

        if (Build.VERSION.SDK_INT < 19) {
            intent = Intent.createChooser(intent, "Image From");
        } else {
            String[] mimeTypes = {"image/jpeg", "image/png"};
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        return intent;
    }

    public static Intent getMultipleImagesFromGallery(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        return intent;
    }

    public static void intentForCall(Context context , String number) {

        if(PermissionUtils.isPermissionGrantedForCall(context)){
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
            context.startActivity(intent);
        } else {
            PermissionUtils.requestForCall((Activity) context);
        }
    }

}
