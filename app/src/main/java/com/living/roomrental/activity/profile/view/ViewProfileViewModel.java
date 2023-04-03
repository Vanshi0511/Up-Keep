package com.living.roomrental.activity.profile.view;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.activity.profile.model.ProfileModel;

public class ViewProfileViewModel extends ViewModel {

    public MutableLiveData<ProfileModel> getProfileData(){
        ViewProfileRepository repository = new ViewProfileRepository();
        return repository.getProfileDataFromServer();
    }
}
