package com.living.roomrental.activity.profile.edit;


import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.activity.profile.create.CreateProfileRepository;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;

public class EditProfileViewModel extends ViewModel {

    private EditProfileRepository repository;
    private Uri imageUri;

    public EditProfileViewModel(){
        repository = new EditProfileRepository();
    }


    public Uri getImageUri(){
        return imageUri;
    }
    public void setImageUri(Uri imageUri){
        this.imageUri = imageUri;
    }

    public LiveData<String> setProfileData(Context context , ProfileModel model){

        String whoIsUser = SharedPreferenceStorage.getWhoIsUser(SharedPreferencesController.getInstance(context).getPreferences());
        model.setWhoIsUser(whoIsUser);

        CreateProfileRepository createProfileRepository = new CreateProfileRepository(context);

        if(model.getImageUrl()!=null && EditProfileActivity.isImageRemoved){
           repository.deleteImageFromServer(model.getImageUrl());
           model.setImageUrl(null);
        }
        if(imageUri!=null){
            return createProfileRepository.createProfileToServerWithImage(model,imageUri);
        } else {
            return createProfileRepository.createProfileToServer(model);
        }
    }

}
