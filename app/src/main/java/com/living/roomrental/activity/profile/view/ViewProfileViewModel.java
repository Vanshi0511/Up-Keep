package com.living.roomrental.activity.profile.view;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.activity.profile.create.CreateProfileModel;

public class ViewProfileViewModel extends ViewModel {

    public MutableLiveData<CreateProfileModel> getProfileData(){
        ViewProfileRepository repository = new ViewProfileRepository();
        return repository.getProfileDataFromServer();
    }
}
