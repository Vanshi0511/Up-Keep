package com.living.roomrental.activity.register;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.utilities.repository.FirebaseController;

public class RegisterViewModel extends ViewModel {

    private FirebaseAuth auth;

    private MutableLiveData<String> success = new MutableLiveData<>();

    private MutableLiveData<String> failure = new MutableLiveData<>();
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

    public LiveData<String> getSuccess() {
        return success;
    }

    public LiveData<String> getFailure() {
        return failure;
    }


    public void registerUser(String email , String password){
        auth = FirebaseController.getInstance().getAuth();

        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
               success.setValue("Registered Successfully");
               System.out.println("===================Auth Result : "+authResult.toString());
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
        System.out.println("======================Register view model destroyed");
    }
}
