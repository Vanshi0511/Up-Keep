package com.living.roomrental.activity.profile.create;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.activity.profile.repository.ProfileRepository;

public class CreateProfileViewModel extends ViewModel {

    private Uri imageUri;

    public Uri getImageUri(){
        return imageUri;
    }
    public void setImageUri(Uri imageUri){
    this.imageUri = imageUri;
    }

    public LiveData<String> createUserProfile(Context context, ProfileModel model){

        ProfileRepository repository =new ProfileRepository(context);
        if(imageUri==null){
            return  repository.createProfileToServer(model);
        } else {
            return repository.createProfileToServerWithImage(model , imageUri);
        }
    }
}
