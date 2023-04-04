package com.living.roomrental.tenant.activity.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.utilities.AppConstants;

public class ViewPropertyTenantRepository {

    private MutableLiveData<String> responseMutableDataForStore = new MutableLiveData<>();

    private MutableLiveData<String> responseMutableDataForSendRequest = new MutableLiveData<>();

    private String uidOfCurrentUser;
    private DatabaseReference databaseReference;

    public ViewPropertyTenantRepository(){
        uidOfCurrentUser = FirebaseController.getInstance().getUser().getUid();
        databaseReference = FirebaseController.getInstance().getDatabaseReference();
    }

    public MutableLiveData<String> sendRequest(PropertyRequestModel model, String propertyKey){

        model.setUidOfUser(uidOfCurrentUser);
        databaseReference.child(AppConstants.LANDLORD_PROPERTY).child(model.getUidOfUser()).child(propertyKey).child("propertyRequests")
                .push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                responseMutableDataForSendRequest.setValue(AppConstants.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                responseMutableDataForSendRequest.setValue(null);
                System.out.println("================ EXCEPTION ============ "+e.getMessage());
            }
        });
        return responseMutableDataForSendRequest;
    }

    public MutableLiveData<String> setTenantRequestData(String propertyKey){

        databaseReference.child("UserRequests").child(uidOfCurrentUser).push().setValue(propertyKey).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                responseMutableDataForStore.setValue(AppConstants.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                responseMutableDataForStore.setValue(null);
                System.out.println("================ EXCEPTION 2 ============ "+e.getMessage());
            }
        });
        return responseMutableDataForStore;
    }
}
