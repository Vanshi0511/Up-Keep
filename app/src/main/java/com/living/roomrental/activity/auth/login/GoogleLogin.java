package com.living.roomrental.activity.auth.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.living.roomrental.R;
import com.living.roomrental.activity.general.UserChoiceBottomSheet;
import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.activity.profile.view.ViewProfileRepository;
import com.living.roomrental.landlord.activity.main.LandlordMainActivity;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;
import com.living.roomrental.tenant.activity.main.TenantMainActivity;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;

import java.util.Objects;

public class GoogleLogin {

    private Context context;
    private FirebaseAuth auth;

    public GoogleLogin(Context context) {
        this.context = context;
        auth = FirebaseAuth.getInstance();
    }

    public void signIn() {

//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
//        if (account == null) {
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.web_client_id))
                    .requestEmail()
                    .build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);

            Intent signInIntent = googleSignInClient.getSignInIntent();
            ((Activity)context).startActivityForResult(signInIntent, AppConstants.GOOGLE_REQ_CODE);
//        } else
//            Toast.makeText(context, "Already exist", Toast.LENGTH_SHORT).show();
    }

    public void activityResult(int requestCode, int resultCode, Intent data, int RESULT_OK) {

        if (requestCode == AppConstants.GOOGLE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                ((LoginActivity)context).progressDialog = AppBoiler.setProgressDialog(context);
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }
    }

    private  void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String token = account.getIdToken();
            /*
            ====== User credentials from google ========

            String username = account.getDisplayName();
            String email = account.getEmail();
            Uri profileImage = account.getPhotoUrl();
            */

            // authenticate from firebase for google
            AuthCredential credential = GoogleAuthProvider.getCredential(token, null);
            firebaseAuthWithGoogle(credential);

        } catch (ApiException e) {
            ((LoginActivity)context).progressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void firebaseAuthWithGoogle(AuthCredential credential) {
        auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                SharedPreferenceStorage.setUidOfUser(SharedPreferencesController.getInstance(context).getPreferences(), Objects.requireNonNull(authResult.getUser()).getUid());
                getProfileData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ((LoginActivity)context).progressDialog.dismiss();
                Toast.makeText(context, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });
    }

    private void getProfileData(){
        ViewProfileRepository viewProfileRepository = new ViewProfileRepository();
        LiveData<ProfileModel> modelLiveData = viewProfileRepository.getProfileDataFromServer();

        modelLiveData.observe((LoginActivity)context, new Observer<ProfileModel>() {
            @Override
            public void onChanged(ProfileModel model) {
                ((LoginActivity)context).progressDialog.dismiss();

                if(model!=null){
                    SharedPreferenceStorage.setProfileStatusOfUser(SharedPreferencesController.getInstance(context).getPreferences(), model.getWhoIsUser());
                    SharedPreferenceStorage.setUserExtraData(SharedPreferencesController.getInstance(context).getPreferences(),model.getName(),model.getImageUrl());

                    if (model.getWhoIsUser().equals(AppConstants.LANDLORD))
                        AppBoiler.navigateToActivityWithFinish(context, LandlordMainActivity.class, null);
                    else
                        AppBoiler.navigateToActivityWithFinish(context, TenantMainActivity.class, null);
                }
                else{
                    openChoiceBottomSheet();
                }

            }
        });
    }
    private void openChoiceBottomSheet(){
        UserChoiceBottomSheet bottomSheet = new UserChoiceBottomSheet();
        bottomSheet.show(((LoginActivity)context).getSupportFragmentManager(), "ChoiceBottomSheet");
//        bottomSheet.initListeners(new BottomChoiceListener() {
//            @Override
//            public void onClickLandlord() {
//                Bundle bundle = new Bundle();
//                bundle.putString(AppConstants.WHO_IS_USER, AppConstants.LANDLORD);
//                AppBoiler.navigateToActivity(context, CreateProfileActivity.class, bundle);
//            }
//
//            @Override
//            public void onClickTenant() {
//                Bundle bundle = new Bundle();
//                bundle.putString(AppConstants.WHO_IS_USER, AppConstants.TENANT);
//                AppBoiler.navigateToActivity(context, CreateProfileActivity.class, bundle);
//            }
//        });
    }
}

