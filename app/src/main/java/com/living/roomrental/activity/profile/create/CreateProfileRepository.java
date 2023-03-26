package com.living.roomrental.activity.profile.create;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.living.roomrental.FirebaseController;

public class CreateProfileRepository {

    private DatabaseReference reference;
    private StorageReference storageReference;
    private FirebaseController controller;

    private MutableLiveData<String> response = new MutableLiveData<>();

    private MutableLiveData<CreateProfileModel> profileModelMutableLiveData = new MutableLiveData<>();

    public CreateProfileRepository(){
        controller = FirebaseController.getInstance();
    }
    public MutableLiveData<String> createProfileToServerWithImage(CreateProfileModel model , Uri uri){

       reference = controller.getDatabaseReference();
       storageReference = controller.getStorageReference();

       storageReference = storageReference.child("Images").child("ProfileImages").child(controller.getUser().getUid());
       storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       model.setImageUrl(uri.toString());

                       reference.child("UserProfile").child(controller.getUser().getUid()).setValue(model)
                               .addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       response.setValue("success");

                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                                   @Override
                                   public void onFailure(@NonNull Exception e) {
                                       System.out.println("============99999999999999999============");
                                       response.setValue(e.getMessage());
                                   }
                               });
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       System.out.println("============8888888888888888888==============");
                       response.setValue(e.getMessage());
                   }
               });
           }
       }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               System.out.println("==============================================");
               response.setValue(e.getMessage());
           }
       });
       return response;
    }

    public MutableLiveData<String> createProfileToServer(CreateProfileModel model){
        reference = controller.getDatabaseReference();
        model.setImageUrl(null);

        reference.child("UserProfile").child(controller.getUser().getUid()).setValue(model)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        response.setValue("success");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("============99999999999999999============");
                        response.setValue(e.getMessage());
                    }
                });
        return response;
    }

    public MutableLiveData<CreateProfileModel> getProfileDataFromServer(){
        reference.child("UserProfile").child(controller.getUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CreateProfileModel model = snapshot.getValue(CreateProfileModel.class);
                profileModelMutableLiveData.setValue(model);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Error================"+error.getMessage());
            }
        });
        return profileModelMutableLiveData;
    }

}
