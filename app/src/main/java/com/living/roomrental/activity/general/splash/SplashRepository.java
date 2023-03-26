package com.living.roomrental.activity.general.splash;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.activity.profile.create.CreateProfileModel;
import com.living.roomrental.utilities.AppConstants;

public class SplashRepository {

    private MutableLiveData<CreateProfileModel> modelMutableLiveData = new MutableLiveData<>();
    private FirebaseController controller;
    public SplashRepository(){
        controller = FirebaseController.getInstance();
    }

    public MutableLiveData<CreateProfileModel> getUserData(){

        DatabaseReference reference =  controller.getDatabaseReference();

        System.out.println("==============1");

        reference.child(AppConstants.USER_PROFILE).child(controller.getAuth().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CreateProfileModel model = snapshot.getValue(CreateProfileModel.class);
                    modelMutableLiveData.setValue(model);
                    System.out.println("==============2");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("=======ERROR===== "+error.getMessage());
            }
        });
        return modelMutableLiveData;
    }
}
