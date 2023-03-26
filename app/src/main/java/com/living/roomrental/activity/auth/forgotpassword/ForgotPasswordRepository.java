package com.living.roomrental.activity.auth.forgotpassword;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.utilities.AppConstants;

public class ForgotPasswordRepository {

    private FirebaseController controller ;

    private MutableLiveData<String> responseMutableData = new MutableLiveData<>();

    public ForgotPasswordRepository() {
        controller = FirebaseController.getInstance();
    }
    public MutableLiveData<String> sendResetPasswordEmailLink(String email){
        FirebaseAuth auth = controller.getAuth();

        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                responseMutableData.setValue(AppConstants.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                responseMutableData.setValue(e.getMessage());
                System.out.println("=========ERROR======== "+e.getMessage());
            }
        });
        return responseMutableData;
    }
}
