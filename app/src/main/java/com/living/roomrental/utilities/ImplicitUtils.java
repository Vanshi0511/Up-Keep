package com.living.roomrental.utilities;

import android.content.Intent;
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
}
