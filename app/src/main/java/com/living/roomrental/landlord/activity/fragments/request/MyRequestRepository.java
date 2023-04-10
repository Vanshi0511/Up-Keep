package com.living.roomrental.landlord.activity.fragments.request;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.tenant.activity.view.PropertyRequestModel;
import com.living.roomrental.utilities.AppConstants;

import java.util.ArrayList;
import java.util.List;

public class MyRequestRepository {

    private MutableLiveData<List<MyRequestsModel>> rentModelMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<List<ProfileModel>> profileModelMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<String> deleteResponseMutableData = new MutableLiveData<>();


    public MutableLiveData<List<MyRequestsModel>> getRequestFromServer(){

        String uid = FirebaseController.getInstance().getUser().getUid();
        DatabaseReference reference = FirebaseController.getInstance().getDatabaseReference().child(AppConstants.LANDLORD_PROPERTY).child(uid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    List<MyRequestsModel> modelList = new ArrayList<>();

                    for(DataSnapshot propertyKeySnapshot : snapshot.getChildren()){

                        for(DataSnapshot requestSnapshot : propertyKeySnapshot.child("propertyRequests").getChildren()){

                            if(requestSnapshot.exists()) {

                                String propertyName = propertyKeySnapshot.child("propertyName").getValue().toString();
                                String description  = requestSnapshot.child("description").getValue().toString();
                                String uidOfTenant  = requestSnapshot.getKey();

                                MyRequestsModel myRequestsModel = new MyRequestsModel(description,uidOfTenant ,null,propertyName);
                                myRequestsModel.setPropertyKey(propertyKeySnapshot.getKey());
                                modelList.add(myRequestsModel);
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


    public MutableLiveData<List<ProfileModel>> getProfileDataFromServer(List<MyRequestsModel> myRequestsModelList) {

        DatabaseReference databaseReference = FirebaseController.getInstance().getDatabaseReference().child(AppConstants.USER_PROFILE);

        List<ProfileModel> profileModels = new ArrayList<>();
        for( int i=0 ; i< myRequestsModelList.size() ; i++) {

            databaseReference.child(myRequestsModelList.get(i).getUidOfTenant()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        ProfileModel model = snapshot.getValue(ProfileModel.class);
                        System.out.println("================== 112"+model.toString());
                        profileModels.add(model);

                        System.out.println("================== 112"+profileModels.size()+"   "+myRequestsModelList.size());
                        if(profileModels.size() == myRequestsModelList.size()){
                            System.out.println("==================hhhhhhhhhhhh");
                            profileModelMutableLiveData.setValue(profileModels);
                        }

                        System.out.println("model size"+profileModels.size());
                    } else {
                        System.out.println("================== 221");
                        profileModelMutableLiveData.setValue(null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    System.out.println("=========== Error ================" + error.getMessage());
                }
            });
        }

        return profileModelMutableLiveData;
    }

    public MutableLiveData<String> deleteRequestFromProperty(String uidOfTenant ,  String propertyKey){

        String uid = FirebaseController.getInstance().getUser().getUid();
        DatabaseReference databaseReference = FirebaseController.getInstance().getDatabaseReference().child(AppConstants.LANDLORD_PROPERTY)
                .child(uid)
                .child(propertyKey)
                .child("propertyRequests")
                .child(uidOfTenant);
        databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("======== Property deleted ==============");
                        deleteResponseMutableData.setValue(AppConstants.SUCCESS);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("======== Error ============== "+e.getMessage() );
                deleteResponseMutableData.setValue("Failed");
            }
        });
        return deleteResponseMutableData;
    }
}
