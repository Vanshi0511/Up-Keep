package com.living.roomrental.activity.forgotpassword;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.FirebaseController;

public class ForgotPasswordViewModel extends ViewModel {

    private FirebaseAuth auth;
    private String email;
    private MutableLiveData<String> success = new MutableLiveData<>();

    private MutableLiveData<String> failure = new MutableLiveData<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LiveData<String> getSuccess() {
        return success;
    }

    public LiveData<String> getFailure() {
        return failure;
    }

    public void sendResetPasswordEmail(){
        auth = FirebaseController.getInstance().getAuth();

        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                success.setValue("Email sent to registered email id.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                failure.setValue(e.getMessage());
            }
        });
    }

}
