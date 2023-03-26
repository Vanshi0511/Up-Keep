package com.living.roomrental.activity.profile.create;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.living.roomrental.FirebaseController;

public class CreateProfileRepository {

    private DatabaseReference reference;
    private FirebaseController controller;

    private MutableLiveData<String> response = new MutableLiveData<>();

    public CreateProfileRepository(){
        controller = FirebaseController.getInstance();
    }
    public MutableLiveData<String> createProfileToServer(CreateProfileModel model){

       reference = controller.getDatabaseReference();
       reference.child("UserProfile").child(controller.getUser().getUid()).setValue(model)
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void unused) {
                     response.setValue("success");

                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                      response.setValue(e.getMessage());
                   }
               });
       return response;
    }
}
