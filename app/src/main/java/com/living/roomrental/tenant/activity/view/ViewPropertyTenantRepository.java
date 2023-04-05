package com.living.roomrental.tenant.activity.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.utilities.AppConstants;

public class ViewPropertyTenantRepository {

    private MutableLiveData<String> responseMutableDataForStore = new MutableLiveData<>();

    private MutableLiveData<String> responseMutableDataForSendRequest = new MutableLiveData<>();

    private MutableLiveData<Boolean> isRequestedMutableData = new MutableLiveData<>();


    private String uidOfCurrentUser;
    private DatabaseReference databaseReference;

    private String propertyKey , uidOfLandlord;

    public ViewPropertyTenantRepository(String propertyKey , String uidOfLandlord){

        this.propertyKey = propertyKey;
        this.uidOfLandlord = uidOfLandlord;

        uidOfCurrentUser = FirebaseController.getInstance().getUser().getUid();
        databaseReference = FirebaseController.getInstance().getDatabaseReference();
    }

    public MutableLiveData<String> sendRequest(PropertyRequestModel model){

        model.setUidOfUser(uidOfCurrentUser);
        databaseReference.child(AppConstants.LANDLORD_PROPERTY).child(uidOfLandlord).child(propertyKey).child("propertyRequests")
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

    public MutableLiveData<String> setTenantRequestData(){

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

    public MutableLiveData<Boolean> isUserRequested(){
        databaseReference.child(AppConstants.LANDLORD_PROPERTY).child(uidOfLandlord).child(propertyKey).child("propertyRequests")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){

                            System.out.println("========== found 1");
                            PropertyRequestModel model;
                            String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            for(DataSnapshot keySnapshot : snapshot.getChildren()){
                                System.out.println("========== found 2");
                                model = keySnapshot.getValue(PropertyRequestModel.class);

                                if(model.getUidOfUser().equals(currentUid)){
                                    System.out.println("========== found 3");
                                   isRequestedMutableData.setValue(true);
                                   break;
                                }
                            }
                        } else{
                            System.out.println("=========== No DATA ======== ");
                            isRequestedMutableData.setValue(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println("=========== Error ======== ");
                        isRequestedMutableData.setValue(false);
                    }
                });
        return isRequestedMutableData;
    }
}
