package com.living.roomrental.activity.auth.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.R;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;
import com.living.roomrental.utilities.AppConstants;

public class GoogleLogin {

    private Context context;
    private FirebaseAuth auth;

    public GoogleLogin(Context context) {
        this.context = context;
        auth = FirebaseController.getInstance().getAuth();
    }

    public void signIn() {

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account == null) {
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(context.getString(R.string.web_client_id))
                    .requestEmail()
                    .build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);

            Intent signInIntent = googleSignInClient.getSignInIntent();
            ((Activity)context).startActivityForResult(signInIntent, AppConstants.GOOGLE_REQ_CODE);
        } else
            Toast.makeText(context, "Already exist", Toast.LENGTH_SHORT).show();
    }

    public void activityResult(int requestCode, int resultCode, Intent data, int RESULT_OK) {

        if (requestCode == AppConstants.GOOGLE_REQ_CODE) {
            if (resultCode == RESULT_OK) {
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
            e.printStackTrace();
        }
    }

    private void firebaseAuthWithGoogle(AuthCredential credential) {
        auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                ((LoginActivity)context).progressDialog.dismiss();
                SharedPreferenceStorage.setUidOfUser(SharedPreferencesController.getInstance(context).getPreferences(),authResult.getUser().getUid());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ((LoginActivity)context).progressDialog.dismiss();
                e.printStackTrace();
            }
        });
    }
}

