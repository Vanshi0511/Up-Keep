package com.living.roomrental.landlord.activity.create_property;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.utilities.AppConstants;

import java.util.ArrayList;

public class CreatePropertyRepository {

    private String uid ;
    private DatabaseReference databaseReference;

    private StorageReference storageReference;

    private MutableLiveData<String> responseMutableData = new MutableLiveData<>();
    public CreatePropertyRepository(){
        uid = FirebaseController.getInstance().getAuth().getUid();
        databaseReference = FirebaseController.getInstance().getDatabaseReference().child(AppConstants.LANDLORD_PROPERTY).child(uid);
        storageReference = FirebaseController.controller.getStorageReference().child(AppConstants.PROPERTY_IMAGES).child(uid);

    }

    public MutableLiveData<String> setDataToServer(CreatePropertyDataModel model , ArrayList<Uri> imagesUri){

        ArrayList<String> imagesUrl = new ArrayList<>();

        for(int i=0;i<imagesUri.size();i++)
        {
            Uri img = imagesUri.get(i);

            StorageReference storageReferenceInner = storageReference.child("image_"+i);
            storageReferenceInner.putFile(img).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReferenceInner.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imagesUrl.add(uri.toString());

                            if(imagesUrl.size() == imagesUri.size())
                            {
                                model.setPropertyImages(imagesUrl);
                                databaseReference.push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        responseMutableData.setValue(AppConstants.SUCCESS);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        responseMutableData.setValue(e.getMessage());
                                        System.out.println("=========== EXCEPTION 3 ============="+e.getMessage());
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            responseMutableData.setValue("Error : while creating property");
                            System.out.println("=========== EXCEPTION 2 ============="+e.getMessage());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    responseMutableData.setValue("Error : while creating property");
                    System.out.println("=========== EXCEPTION 1 ============="+e.getMessage());
                }
            });
        }
        return responseMutableData;
    }

    public MutableLiveData<String> setDataToServerWithoutImage(CreatePropertyDataModel model){

        databaseReference.push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                responseMutableData.setValue(AppConstants.SUCCESS);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                responseMutableData.setValue(e.getMessage());
                System.out.println("=========== EXCEPTION ============="+e.getMessage());
            }
        });

        return responseMutableData;
    }

}
