package com.living.roomrental.activity.profile.edit;


import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.activity.profile.repository.ProfileRepository;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;

public class EditProfileViewModel extends ViewModel {

    private Uri imageUri;

    public Uri getImageUri(){
        return imageUri;
    }
    public void setImageUri(Uri imageUri){
        this.imageUri = imageUri;
    }

    public LiveData<String> setProfileData(Context context , ProfileModel model){

        String whoIsUser = SharedPreferenceStorage.getWhoIsUser(SharedPreferencesController.getInstance(context).getPreferences());
        model.setWhoIsUser(whoIsUser);

        ProfileRepository profileRepository = new ProfileRepository(context);

        if(model.getImageUrl()!=null && EditProfileActivity.isImageRemoved){
           profileRepository.deleteImageFromServer(model.getImageUrl());
           model.setImageUrl(null);
        }
        if(imageUri!=null){
            return profileRepository.createProfileToServerWithImage(model,imageUri);
        } else {
            return profileRepository.createProfileToServer(model);
        }
    }

}
