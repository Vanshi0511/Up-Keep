package com.living.roomrental.activity.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.FirebaseController;

public class LoginViewModel extends ViewModel {

    private FirebaseAuth auth;
    private String  email , password;
    private MutableLiveData<String> success = new MutableLiveData<>();

    private MutableLiveData<String> failure = new MutableLiveData<>();

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
    public LiveData<String> getSuccess() {
        return success;
    }

    public LiveData<String> getFailure() {
        return failure;
    }

    public void login(String email , String password){
        auth = FirebaseController.getInstance().getAuth();

        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                success.setValue(authResult.getUser().getDisplayName());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failure.setValue(e.getMessage());
                System.out.println("===================Exception while Registration : "+e.getMessage());
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        System.out.println("======================= Login view model destroyed");
    }
}
