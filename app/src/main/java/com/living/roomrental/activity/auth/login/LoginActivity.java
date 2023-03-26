package com.living.roomrental.activity.auth.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.living.roomrental.DialogListener;
import com.living.roomrental.R;
import com.living.roomrental.activity.auth.forgotpassword.ForgotPasswordActivity;
import com.living.roomrental.activity.auth.register.RegisterActivity;
import com.living.roomrental.activity.general.BottomSheetChoiceFragment;
import com.living.roomrental.databinding.ActivityLoginBinding;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;
import com.living.roomrental.utilities.Validation;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;
    private GoogleLogin googleLogin;
    private String email="" , password="" ;
    public Dialog progressDialog , customDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        observeActivityComponents();
        initListeners();
    }

    private void observeActivityComponents() {
        binding.emailEditText.setText(loginViewModel.getEmail());
        binding.passwordEditText.setText(loginViewModel.getPassword());

        LiveData<String> success = loginViewModel.getSuccess();
        LiveData<String> failure = loginViewModel.getFailure();

        success.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String uid) {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "kkkkk", Toast.LENGTH_SHORT).show();
                    SharedPreferenceStorage.setUidOfUser(SharedPreferencesController.getInstance(LoginActivity.this).getPreferences(),uid);

                    BottomSheetChoiceFragment bottomSheet = new BottomSheetChoiceFragment();
                    bottomSheet.show(getSupportFragmentManager(),
                            "ModalBottomSheet");
                    //todo bottom sheet
                }

            }
        });

        failure.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(progressDialog!=null){
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


    private void initListeners(){
        binding.registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppBoiler.navigateToActivity(LoginActivity.this, RegisterActivity.class,null);
            }
        });

        binding.googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppBoiler.isInternetConnected(LoginActivity.this)) {
                    googleLogin = new GoogleLogin(LoginActivity.this);
                    googleLogin.signIn();
                } else {
                    AppBoiler.showSnackBarForInternet(LoginActivity.this,binding.rootLayoutOfLogin);
                }

            }
        });

        binding.forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppBoiler.navigateToActivity(LoginActivity.this, ForgotPasswordActivity.class,null);
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

                if (isValid==1) {
                    binding.emailTextInputLayout.setError("Enter email");
                } else if(isValid==2) {
                    binding.emailTextInputLayout.setError("Invalid email format");
                } else if(isValid==0){
                    AppBoiler.setInputLayoutErrorDisable(binding.emailTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginViewModel.setEmail(binding.emailEditText.getText().toString().trim());
            }
        });

        binding.passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = charSequence.toString();
                if(Validation.isStringEmpty(password)){
                    binding.passwordTextInputLayout.setError("Enter password");
                }else{
                    AppBoiler.setInputLayoutErrorDisable(binding.passwordTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                loginViewModel.setPassword(binding.passwordEditText.getText().toString().trim());
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.emailTextInputLayout.isErrorEnabled()) {
                    binding.emailEditText.requestFocus();
                } else if(Validation.isStringEmpty(email)) {
                    binding.emailTextInputLayout.setError("Enter email");
                    binding.emailEditText.requestFocus();
                } else if (binding.passwordTextInputLayout.isErrorEnabled() || Validation.isStringEmpty(password)) {
                    binding.passwordEditText.requestFocus();
                    binding.passwordTextInputLayout.setError("Enter password");
                }else{
                    loginUser();
                }
            }
        });
    }

    private void loginUser(){
        if (AppBoiler.isInternetConnected(this)) {
            progressDialog = AppBoiler.setProgressDialog(LoginActivity.this);
            loginViewModel.login(email, password);
        } else {
            AppBoiler.showSnackBarForInternet(this,binding.rootLayoutOfLogin);
        }
    }


    // for google login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode== AppConstants.GOOGLE_REQ_CODE){
            progressDialog = AppBoiler.setProgressDialog(LoginActivity.this);
            googleLogin.activityResult(requestCode,resultCode,data , Activity.RESULT_OK);}
    }
}