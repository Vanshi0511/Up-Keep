package com.living.roomrental.activity.auth.forgotpassword;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.FirebaseController;

public class ForgotPasswordViewModel extends ViewModel {

    public MutableLiveData<String> sendResetPasswordEmail(String email){
        ForgotPasswordRepository repository = new ForgotPasswordRepository();
        return repository.sendResetPasswordEmailLink(email);
    }

}
