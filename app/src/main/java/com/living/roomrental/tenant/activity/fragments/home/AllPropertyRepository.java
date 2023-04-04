package com.living.roomrental.tenant.activity.fragments.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyDataModel;
import com.living.roomrental.utilities.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class AllPropertyRepository {
    private MutableLiveData<List<CreatePropertyDataModel>> modelMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<List<CreatePropertyDataModel>> getAllPropertyFromServer() {

        DatabaseReference reference = FirebaseController.getInstance().getDatabaseReference().child(AppConstants.LANDLORD_PROPERTY);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    List<CreatePropertyDataModel> modelList = new ArrayList<>();

                    CreatePropertyDataModel model;
                    for(DataSnapshot uidSnapshot : snapshot.getChildren()){

                        for(DataSnapshot propertyKeySnapshot : uidSnapshot.getChildren()){
                            model = propertyKeySnapshot.getValue(CreatePropertyDataModel.class);
                            model.setKey(propertyKeySnapshot.getKey());
                            model.setUid(uidSnapshot.getKey());
                            modelList.add(model);
                        }
                    }
                    modelMutableLiveData.setValue(modelList);
                } else {
                    modelMutableLiveData.setValue(null);
                    System.out.println("====================== No Data Found =================");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                modelMutableLiveData.setValue(null);
                System.out.println("====================== ERROR ================= " + error.getMessage());
            }
        });
        return modelMutableLiveData;
    }
}
