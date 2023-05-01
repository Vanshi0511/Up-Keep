package com.living.roomrental.landlord.activity.view_property;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyDataModel;
import com.living.roomrental.utilities.AppConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewPropertyRepository {

    private CreatePropertyDataModel model;
    private String uid;
    int position = 1;

    private MutableLiveData<String> responseMutableData = new MutableLiveData<>();

    private MutableLiveData<ProfileModel> profileModelMutableLiveData = new MutableLiveData<>();

    public ViewPropertyRepository(CreatePropertyDataModel model){
        this.model = model;
        uid = FirebaseController.getInstance().getAuth().getUid();
    }

    public ViewPropertyRepository(){
        uid = FirebaseController.getInstance().getAuth().getUid();
    }
    public MutableLiveData<String> deleteImageFromServer(){

        System.out.println("=========== 1++++++++");
        FirebaseStorage storage = FirebaseStorage.getInstance();

        for(String url : model.getPropertyImages()){

            System.out.println("=========== 2 ++++++++"+url);
            storage.getReferenceFromUrl(url).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    System.out.println("=========== 3++++++++" +position+"          "+model.getPropertyImages().size());
                    if(position == model.getPropertyImages().size()){
                        System.out.println("=========== 4++++++++");
                        deleteDataFromServer();
                    }
                    position++;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    responseMutableData.setValue("Failed");
                    System.out.println("============= ERROR ======= "+e.getMessage());
                }
            });
        }
        return responseMutableData;
    }

    public MutableLiveData<String> deleteDataFromServer(){

        System.out.println("=======key====== "+model.getKey());

        DatabaseReference reference = FirebaseController.getInstance().getDatabaseReference().child(AppConstants.LANDLORD_PROPERTY).child(uid).child(model.getKey());
        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                responseMutableData.setValue(AppConstants.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                responseMutableData.setValue("Failed");
                System.out.println("=============== ERROR DATABASE ======== "+e.getMessage());
            }
        });
        return responseMutableData;
    }

    public MutableLiveData<String> removeTenantFromBooking(String propertyKey){

        Map<String,Object> map = new HashMap<>();
        map.put("bookingStatus","vacant");
        map.put("currentBooking",null);

        DatabaseReference databaseReference = FirebaseController.getInstance().getDatabaseReference().child(AppConstants.LANDLORD_PROPERTY).child(uid);
        databaseReference.child(propertyKey).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                responseMutableData.setValue(AppConstants.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("============ EXCEPTION ============ "+e.getMessage());
                responseMutableData.setValue("Failed");
            }
        });
        return responseMutableData;
    }

    public MutableLiveData<ProfileModel> getProfileData(String tenantId){

        DatabaseReference databaseReference = FirebaseController.getInstance().getDatabaseReference().child(AppConstants.USER_PROFILE).child(tenantId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ProfileModel profileModel = snapshot.getValue(ProfileModel.class);
                    profileModelMutableLiveData.setValue(profileModel);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("=========== FAILED =========== "+error.getMessage());
                profileModelMutableLiveData.setValue(null);
            }
        });
        return profileModelMutableLiveData;
    }
}
