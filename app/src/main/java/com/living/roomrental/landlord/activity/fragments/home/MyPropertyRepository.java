package com.living.roomrental.landlord.activity.fragments.home;

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

public class MyPropertyRepository {

    private String uid;
    private DatabaseReference reference;

    private MutableLiveData<List<CreatePropertyDataModel>> propertyMutableData = new MutableLiveData<>();

    public MyPropertyRepository(){
        uid = FirebaseController.getInstance().getAuth().getUid();
        reference = FirebaseController.getInstance().getDatabaseReference().child(AppConstants.LANDLORD_PROPERTY).child(uid);

    }

    public MutableLiveData<List<CreatePropertyDataModel>> getPropertyFromServer(){

        List<CreatePropertyDataModel> modelList = new ArrayList<>();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    CreatePropertyDataModel model;
                    for(DataSnapshot dataSingleModel : snapshot.getChildren()){

                        model = dataSingleModel.getValue(CreatePropertyDataModel.class);
                        model.setKey(dataSingleModel.getKey());
                        modelList.add(model);
                    }
                    propertyMutableData.setValue(modelList);
                }
                else{
                    propertyMutableData.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("=============== ERROR ========== "+error.getMessage());
            }
        });
        return propertyMutableData;
    }
}
