package com.living.roomrental.activity.auth.register;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.living.roomrental.DialogListener;
import com.living.roomrental.R;
import com.living.roomrental.databinding.ActivityRegisterBinding;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;
import com.living.roomrental.utilities.Validation;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel registerViewModel;

    private String email = "", password = "", confirmPassword = "";

    private Dialog progressDialog , customDialog;

    private LiveData<String> responseLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        binding.header.headerTitle.setText("Register");

        getDataFromViewModel();
        initListeners();
    }

    private void getDataFromViewModel() {

        binding.emailEditText.setText(registerViewModel.getEmail());
        binding.passwordEditText.setText(registerViewModel.getPassword());
        binding.confirmPasswordEditText.setText(registerViewModel.getConfirmPassword());

    }

    private void initListeners() {
        binding.header.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
                registerViewModel.setEmail(editable.toString().trim());
            }
        });

        binding.passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                password = charSequence.toString();
                int isValid = Validation.isValidPassword(password);

                if(isValid==1)
                    binding.passwordTextInputLayout.setError("Enter password");
                else if(isValid==2)
                    binding.passwordTextInputLayout.setError("Password contains at least 8 characters");
                else if(isValid==3)
                    binding.passwordTextInputLayout.setError("Password contains at most 30 characters");
                else if(isValid==4)
                    binding.passwordTextInputLayout.setError("Password contains at least one upper case alphabet");
                else if(isValid==5)
                    binding.passwordTextInputLayout.setError("Password contains at least one lower case alphabet");
                else if(isValid==6)
                    binding.passwordTextInputLayout.setError("Password contains at least one digit");
                else if(isValid==7)
                    binding.passwordTextInputLayout.setError("Password contains at least one special character");
                else if(isValid==8)
                    binding.passwordTextInputLayout.setError("Password does not contain any white space");
                else if(isValid==0){
                    AppBoiler.setInputLayoutErrorDisable(binding.passwordTextInputLayout);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerViewModel.setPassword(editable.toString().trim());
            }
        });

        binding.confirmPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmPassword = charSequence.toString();
                int isValid = Validation.isValidConfirmPassword(password,confirmPassword);

                if(isValid==1){
                    binding.passwordTextInputLayout.setError("Enter password");
                    binding.passwordEditText.requestFocus();
                }else if(isValid==2)
                    binding.confirmPasswordTextInputLayout.setError("Enter confirm password");
                else if(isValid==3)
                    binding.confirmPasswordTextInputLayout.setError("Password contains at least 8 characters");
                else if(isValid==4)
                    binding.confirmPasswordTextInputLayout.setError("Password not matched");
                else{
                    AppBoiler.setInputLayoutErrorDisable(binding.confirmPasswordTextInputLayout);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                registerViewModel.setConfirmPassword(editable.toString().trim());
            }
        });

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (binding.emailTextInputLayout.isErrorEnabled()) {
                    binding.emailEditText.requestFocus();
                } else if(Validation.isStringEmpty(email)) {
                    binding.emailTextInputLayout.setError("Enter email");
                    binding.emailEditText.requestFocus();
                } else if (binding.passwordTextInputLayout.isErrorEnabled()) {
                    binding.passwordEditText.requestFocus();
                } else if (Validation.isStringEmpty(password)) {
                    binding.passwordTextInputLayout.setError("Enter password");
                    binding.passwordEditText.requestFocus();
                }else if (binding.confirmPasswordTextInputLayout.isErrorEnabled()) {
                    binding.confirmPasswordEditText.requestFocus();
                }else if (Validation.isStringEmpty(confirmPassword)) {
                    binding.confirmPasswordTextInputLayout.setError("Enter confirm password");
                    binding.confirmPasswordEditText.requestFocus();
                } else {
                    createUser();
                }
            }
        });
    }
    private void createUser() {
        if (AppBoiler.isInternetConnected(this)) {
            progressDialog = AppBoiler.setProgressDialog(RegisterActivity.this);
            observeResponseOfRegister();
        } else {
            AppBoiler.showSnackBarForInternet(this,binding.rootLayoutOfRegister);
        }

    }

    private void observeResponseOfRegister() {

        responseLiveData = registerViewModel.register();
        responseLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressDialog.dismiss();

                if(s.equals(AppConstants.SUCCESS)){
                    customDialog = AppBoiler.customDialogWithBtn(RegisterActivity.this,"Registered Successfully", R.drawable.ic_done, new DialogListener() {
                        @Override
                        public void onClick() {
                            customDialog.dismiss();
                            onBackPressed();
                        }
                    });
                }
                else{
                    customDialog = AppBoiler.customDialogWithBtn(RegisterActivity.this, s, R.drawable.ic_error, new DialogListener() {
                        @Override
                        public void onClick() {
                            customDialog.dismiss();
                        }
                    });

                }
            }
        });
    }

}