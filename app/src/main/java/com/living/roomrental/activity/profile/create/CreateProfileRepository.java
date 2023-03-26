package com.living.roomrental.activity.profile.create;

import android.net.Uri;

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
import com.google.firebase.storage.UploadTask;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.utilities.AppConstants;

public class CreateProfileRepository {

    private DatabaseReference reference;
    private StorageReference storageReference;
    private FirebaseController controller;

    private String uid ;

    private MutableLiveData<String> responseMutableData = new MutableLiveData<>();

    private MutableLiveData<CreateProfileModel> profileModelMutableLiveData = new MutableLiveData<>();

    public CreateProfileRepository(){
        controller = FirebaseController.getInstance();
        reference = controller.getDatabaseReference();
        storageReference = controller.getStorageReference();

        uid = FirebaseAuth.getInstance().getUid();
    }
    public MutableLiveData<String> createProfileToServerWithImage(CreateProfileModel model , Uri uri){


       storageReference = storageReference.child(AppConstants.PROFILE_IMAGES).child(AppConstants.PROFILE_IMAGES).child(uid);
       storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       model.setImageUrl(uri.toString());

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
                                       responseMutableData.setValue(e.getMessage());
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

    public MutableLiveData<String> createProfileToServer(CreateProfileModel model){

        model.setImageUrl(null);

        reference.child(AppConstants.USER_PROFILE).child(uid).setValue(model)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        responseMutableData.setValue(AppConstants.SUCCESS);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("============ EXCEPTION ============"+e.getMessage());
                        responseMutableData.setValue(e.getMessage());
                    }
                });
        return responseMutableData;
    }

    public MutableLiveData<CreateProfileModel> getProfileDataFromServer(){
        reference.child(AppConstants.USER_PROFILE).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CreateProfileModel model = snapshot.getValue(CreateProfileModel.class);
                    profileModelMutableLiveData.setValue(model);
                }else{
                    profileModelMutableLiveData.setValue(null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("=========== Error ================"+error.getMessage());
            }
        });
        return profileModelMutableLiveData;
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
