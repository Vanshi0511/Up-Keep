package com.living.roomrental.activity.profile.edit;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.utilities.AppConstants;

public class EditProfileRepository {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String uid;

    private MutableLiveData<ProfileModel> modelMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> responseMutableLiveData = new MutableLiveData<>();

    public EditProfileRepository(){
        uid = FirebaseAuth.getInstance().getUid();
        databaseReference = FirebaseController.getInstance().getDatabaseReference().child(AppConstants.USER_PROFILE).child(uid);
    }

    public MutableLiveData<ProfileModel> getDataFromServer(){

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    ProfileModel model = snapshot.getValue(ProfileModel.class);
                    modelMutableLiveData.setValue(model);
                }else{
                    modelMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("=========== Error ================"+error.getMessage());
            }
        });
        return modelMutableLiveData;
    }

    public MutableLiveData<String> updateDataToServer(ProfileModel model){

        databaseReference.setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                responseMutableLiveData.setValue(AppConstants.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                responseMutableLiveData.setValue(e.getMessage());
                System.out.println("======ERROR======= "+e.getMessage());
            }
        });
        return responseMutableLiveData;
    }

    public void deleteImageFromServer(String url){

        storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(url);
        storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    System.out.println("============= SUCCESS =========");
                }else{
                    System.out.println("============= ERROR ========="+task.getException());
                }
            }
        });
    }
}
