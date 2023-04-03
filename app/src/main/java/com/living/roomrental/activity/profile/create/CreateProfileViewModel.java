package com.living.roomrental.activity.profile.create;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.activity.profile.model.ProfileModel;

public class CreateProfileViewModel extends ViewModel {

    private Uri imageUri;

    public Uri getImageUri(){
        return imageUri;
    }
    public void setImageUri(Uri imageUri){
    this.imageUri = imageUri;
    }

    public LiveData<String> createUserProfile(Context context, ProfileModel model){

        CreateProfileRepository repository =new CreateProfileRepository(context);
        if(imageUri==null){
            return  repository.createProfileToServer(model);
        } else {
            return repository.createProfileToServerWithImage(model , imageUri);
        }
    }
}
