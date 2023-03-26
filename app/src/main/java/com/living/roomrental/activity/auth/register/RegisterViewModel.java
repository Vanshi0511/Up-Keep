package com.living.roomrental.activity.auth.register;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.FirebaseController;

public class RegisterViewModel extends ViewModel {


    private String  email , password ,confirmPassword;


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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public MutableLiveData<String> register(){
        RegisterRepository repository = new RegisterRepository();
        return repository.registerUser(email,password);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        System.out.println("======================Register view model destroyed");
    }
}
