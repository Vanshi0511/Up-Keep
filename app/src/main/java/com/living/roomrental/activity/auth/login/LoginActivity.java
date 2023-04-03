package com.living.roomrental.activity.auth.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.BottomChoiceListener;
import com.living.roomrental.DialogListener;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.R;
import com.living.roomrental.activity.auth.forgotpassword.ForgotPasswordActivity;
import com.living.roomrental.activity.auth.register.RegisterActivity;
import com.living.roomrental.activity.general.UserChoiceBottomSheet;
import com.living.roomrental.activity.profile.create.CreateProfileActivity;
import com.living.roomrental.activity.profile.create.CreateProfileModel;
import com.living.roomrental.databinding.ActivityLoginBinding;
import com.living.roomrental.landlord.activity.main.LandlordMainActivity;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;
import com.living.roomrental.tenant.activity.main.TenantMainActivity;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;
import com.living.roomrental.utilities.Validation;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;
    private GoogleLogin googleLogin;
    private String email = "", password = "";
    public Dialog progressDialog, customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        getDataFromViewModel();
        initListeners();
    }

    private void getDataFromViewModel() {
        binding.emailEditText.setText(loginViewModel.getEmail());
        binding.passwordEditText.setText(loginViewModel.getPassword());
    }

    private void initListeners() {
        binding.registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppBoiler.navigateToActivity(LoginActivity.this, RegisterActivity.class, null);
            }
        });

        binding.googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppBoiler.isInternetConnected(LoginActivity.this)) {
                    googleLogin = new GoogleLogin(LoginActivity.this);
                    googleLogin.signIn();
                } else {
                    AppBoiler.showSnackBarForInternet(LoginActivity.this, binding.rootLayoutOfLogin);
                }

            }
        });

        binding.forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppBoiler.navigateToActivity(LoginActivity.this, ForgotPasswordActivity.class, null);
            }
        });


        binding.emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email = charSequence.toString();
                int isValid = Validation.isValidEmail(email);

                if (isValid == 1) {
                    binding.emailTextInputLayout.setError("Enter email");
                } else if (isValid == 2) {
                    binding.emailTextInputLayout.setError("Invalid email format");
                } else if (isValid == 0) {
                    AppBoiler.setInputLayoutErrorDisable(binding.emailTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginViewModel.setEmail(editable.toString().trim());
            }
        });

        binding.passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = charSequence.toString();
                if (Validation.isStringEmpty(password)) {
                    binding.passwordTextInputLayout.setError("Enter password");
                } else {
                    AppBoiler.setInputLayoutErrorDisable(binding.passwordTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginViewModel.setPassword(editable.toString().trim());
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.emailTextInputLayout.isErrorEnabled()) {
                    binding.emailEditText.requestFocus();
                } else if (Validation.isStringEmpty(email)) {
                    binding.emailTextInputLayout.setError("Enter email");
                    binding.emailEditText.requestFocus();
                } else if (binding.passwordTextInputLayout.isErrorEnabled() || Validation.isStringEmpty(password)) {
                    binding.passwordEditText.requestFocus();
                    binding.passwordTextInputLayout.setError("Enter password");
                } else {
                    loginUser();
                }
            }
        });
    }

    private void loginUser() {
        if (AppBoiler.isInternetConnected(this)) {
            progressDialog = AppBoiler.setProgressDialog(LoginActivity.this);
            observeResponseOfLogin();
        } else {
            AppBoiler.showSnackBarForInternet(this, binding.rootLayoutOfLogin);
        }
    }

    private void observeResponseOfLogin() {

        MutableLiveData<String> responseLiveData = loginViewModel.login();

        responseLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {


                if (s.equals(AppConstants.SUCCESS)) {

                    SharedPreferenceStorage.setUidOfUser(SharedPreferencesController.getInstance(LoginActivity.this).getPreferences(), FirebaseAuth.getInstance().getUid());
                    getUserProfile();

                } else {

                    progressDialog.dismiss();
                    customDialog = AppBoiler.customDialogWithBtn(LoginActivity.this, s, R.drawable.ic_error, new DialogListener() {
                        @Override
                        public void onClick() {
                            customDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void getUserProfile() {
        LiveData<CreateProfileModel> modelLiveData = loginViewModel.getProfileDataFromServer();

        modelLiveData.observe(this, new Observer<CreateProfileModel>() {
            @Override
            public void onChanged(CreateProfileModel model) {
                progressDialog.dismiss();
                if (model != null) {

                    SharedPreferenceStorage.setProfileStatusOfUser(SharedPreferencesController.getInstance(LoginActivity.this).getPreferences(), model.getWhoIsUser());
                    SharedPreferenceStorage.setUserExtraData(SharedPreferencesController.getInstance(LoginActivity.this).getPreferences(),model.getName(),model.getImageUrl());

                    if (model.getWhoIsUser().equals(AppConstants.LANDLORD))
                        AppBoiler.navigateToActivityWithFinish(LoginActivity.this, LandlordMainActivity.class, null);
                    else
                        AppBoiler.navigateToActivityWithFinish(LoginActivity.this, TenantMainActivity.class, null);
                } else {

                    UserChoiceBottomSheet bottomSheet = new UserChoiceBottomSheet();
                    bottomSheet.show(getSupportFragmentManager(), "ChoiceBottomSheet");
//                    bottomSheet.initListeners(new BottomChoiceListener() {
//                        @Override
//                        public void onClickLandlord() {
//                            Bundle bundle = new Bundle();
//                            bundle.putString(AppConstants.WHO_IS_USER, AppConstants.LANDLORD);
//                            AppBoiler.navigateToActivity(LoginActivity.this, CreateProfileActivity.class, bundle);
//                        }
//
//                        @Override
//                        public void onClickTenant() {
//                            Bundle bundle = new Bundle();
//                            bundle.putString(AppConstants.WHO_IS_USER, AppConstants.TENANT);
//                            AppBoiler.navigateToActivity(LoginActivity.this, CreateProfileActivity.class, bundle);
//                        }
//                    });
                }
            }
        });
    }


    // for google login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.GOOGLE_REQ_CODE) {
            googleLogin.activityResult(requestCode, resultCode, data, Activity.RESULT_OK);
        }
    }
}