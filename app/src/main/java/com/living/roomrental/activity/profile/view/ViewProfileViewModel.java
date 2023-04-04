package com.living.roomrental.activity.profile.view;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.activity.profile.repository.ProfileRepository;

public class ViewProfileViewModel extends ViewModel {

    public MutableLiveData<ProfileModel> getProfileData(Context context){
        ProfileRepository repository = new ProfileRepository(context);
        return repository.getProfileDataFromServer();
    }
}
