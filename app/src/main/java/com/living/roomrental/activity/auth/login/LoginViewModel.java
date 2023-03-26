package com.living.roomrental.activity.auth.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.activity.profile.create.CreateProfileModel;
import com.living.roomrental.activity.profile.create.CreateProfileRepository;
import com.living.roomrental.activity.profile.create.CreateProfileViewModel;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;

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

    public LiveData<CreateProfileModel> getProfileDataFromServer(){
       // repository = new LoginRepository();
        CreateProfileRepository createProfileRepository = new CreateProfileRepository();
        return createProfileRepository.getProfileDataFromServer();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        System.out.println("======================= Login view model destroyed");
    }
}
