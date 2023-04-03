package com.living.roomrental.activity.auth.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.activity.profile.view.ViewProfileRepository;

public class LoginViewModel extends ViewModel {


    private LoginRepository repository = new LoginRepository();
    private String  email , password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MutableLiveData<String> login(){
        repository = new LoginRepository();
       return repository.loginUser(email,password);
    }

    public LiveData<ProfileModel> getProfileDataFromServer(){
        ViewProfileRepository viewProfileRepository = new ViewProfileRepository();
        return viewProfileRepository.getProfileDataFromServer();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        System.out.println("======================= Login view model destroyed");
    }
}
