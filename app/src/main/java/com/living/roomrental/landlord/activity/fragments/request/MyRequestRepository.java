package com.living.roomrental.landlord.activity.fragments.request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.tenant.activity.view.PropertyRequestModel;
import com.living.roomrental.utilities.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class MyRequestRepository {

    private MutableLiveData<List<PropertyRequestModel>> rentModelMutableLiveData = new MutableLiveData<>();


    public MutableLiveData<List<PropertyRequestModel>> getRequestFromServer(){

        String uid = FirebaseController.getInstance().getUser().getUid();
        DatabaseReference reference = FirebaseController.getInstance().getDatabaseReference().child(AppConstants.LANDLORD_PROPERTY).child(uid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    List<PropertyRequestModel> modelList = new ArrayList<>();

                    for(DataSnapshot propertyKeySnapshot : snapshot.getChildren()){

                        for(DataSnapshot requestSnapshot : propertyKeySnapshot.child("propertyRequests").getChildren()){
                            if(requestSnapshot.exists()) {
                                modelList.add(requestSnapshot.getValue(PropertyRequestModel.class));
                            }
                        }
                    }
                    System.out.println("=========== Data found for request====="+modelList.size());
                    rentModelMutableLiveData.setValue(modelList);
                }else{
                    rentModelMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                rentModelMutableLiveData.setValue(null);
                System.out.println("========== ERROR =========== "+error.getMessage());
            }
        });

        return rentModelMutableLiveData;
    }
}
