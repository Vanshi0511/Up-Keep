package com.living.roomrental.activity.profile.create;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;
import com.living.roomrental.utilities.AppConstants;

public class CreateProfileRepository {

    private DatabaseReference reference;
    private StorageReference storageReference;
    private FirebaseController controller;

    private String uid ;
    private Context context;

    private MutableLiveData<String> responseMutableData = new MutableLiveData<>();

    private MutableLiveData<ProfileModel> profileModelMutableLiveData = new MutableLiveData<>();

    public CreateProfileRepository(Context context){
        this.context = context;

        controller = FirebaseController.getInstance();
        reference = controller.getDatabaseReference();
        storageReference = controller.getStorageReference();

        uid = FirebaseAuth.getInstance().getUid();
    }
    public MutableLiveData<String> createProfileToServerWithImage(ProfileModel model , Uri uri){


       storageReference = storageReference.child(AppConstants.PROFILE_IMAGES).child(uid);
       storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       model.setImageUrl(uri.toString());

                       SharedPreferenceStorage.setUserExtraData(SharedPreferencesController.getInstance(context).getPreferences(),model.getName(),uri.toString());

                       reference.child(AppConstants.USER_PROFILE).child(uid).setValue(model)
                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       responseMutableData.setValue(AppConstants.SUCCESS);
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       System.out.println("========== EXCEPTION 1 ========="+e.getMessage());
                                       responseMutableData.setValue("Failed to Update Profile");
                                   }
                               });
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       System.out.println("============ EXCEPTION 2 =============="+e.getMessage());
                       responseMutableData.setValue(e.getMessage());
                   }
               });
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               System.out.println("=============== EXCEPTION 3 =============="+e.getMessage());
               responseMutableData.setValue(e.getMessage());
           }
       });
       return responseMutableData;
    }

    public MutableLiveData<String> createProfileToServer(ProfileModel model){

        reference.child(AppConstants.USER_PROFILE).child(uid).setValue(model)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        SharedPreferenceStorage.setUserExtraData(SharedPreferencesController.getInstance(context).getPreferences(),model.getName(),model.getImageUrl());
                        responseMutableData.setValue(AppConstants.SUCCESS);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("============ EXCEPTION ============"+e.getMessage());
                        responseMutableData.setValue("Failed to Update Profile");
                    }
                });
        return responseMutableData;
    }

}
