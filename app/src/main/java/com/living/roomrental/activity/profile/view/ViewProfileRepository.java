package com.living.roomrental.activity.profile.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.utilities.AppConstants;

public class ViewProfileRepository {

    private MutableLiveData<ProfileModel> modelMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<ProfileModel> getProfileDataFromServer() {

        String uid = FirebaseController.getInstance().getAuth().getUid();
        DatabaseReference databaseReference = FirebaseController.getInstance().getDatabaseReference().child(AppConstants.USER_PROFILE).child(uid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ProfileModel model = snapshot.getValue(ProfileModel.class);
                    modelMutableLiveData.setValue(model);
                } else {
                    modelMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("=========== Error ================" + error.getMessage());
            }
        });
        return modelMutableLiveData;
    }
}

