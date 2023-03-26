package com.living.roomrental.activity.auth.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.activity.profile.create.CreateProfileModel;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;
import com.living.roomrental.utilities.AppConstants;


public class LoginRepository {

    private FirebaseController controller;

    private MutableLiveData<String> responseMutableData = new MutableLiveData<>();

    private MutableLiveData<CreateProfileModel> modelMutableLiveData = new MutableLiveData<>();

    public LoginRepository(){
        controller = FirebaseController.getInstance();
    }

    public MutableLiveData<String> loginUser(String email , String password){
        FirebaseAuth auth = controller.getAuth();

        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                responseMutableData.setValue(AppConstants.SUCCESS);
                System.out.println("======== USER ID======= "+authResult.getUser().getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                responseMutableData.setValue(e.getMessage());
                System.out.println("=================== Exception while Registration : "+e.getMessage());
            }
        });
        return responseMutableData;
    }

    public MutableLiveData<CreateProfileModel> getUserProfileData(){

        DatabaseReference reference =  controller.getDatabaseReference();

        System.out.println("==============1");

        reference.child(AppConstants.USER_PROFILE).child(controller.getAuth().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    CreateProfileModel model = snapshot.getValue(CreateProfileModel.class);
                    modelMutableLiveData.setValue(model);
                    System.out.println("==============2");
                }else{
                    modelMutableLiveData.setValue(null);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("=======ERROR===== "+error.getMessage());
            }
        });
        return modelMutableLiveData;
    }
}
