package com.living.roomrental.activity.forgotpassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.living.roomrental.DialogListener;
import com.living.roomrental.R;
import com.living.roomrental.activity.register.RegisterActivity;
import com.living.roomrental.databinding.ActivityForgotPasswordBinding;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.Validation;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private ForgotPasswordViewModel forgotPasswordViewModel;
    private String email="";
    private Dialog progressDialog , customDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        forgotPasswordViewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel.class);
        progressDialog = AppBoiler.setProgressDialog(ForgotPasswordActivity.this);
        binding.header.headerTitle.setText("Forgot Password");

        observeActivityComponents();
        initListeners();
    }

    private void observeActivityComponents() {

        binding.emailEditText.setText(forgotPasswordViewModel.getEmail());

        LiveData<String> success = forgotPasswordViewModel.getSuccess();
        LiveData<String> failure = forgotPasswordViewModel.getFailure();

        success.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if(progressDialog!=null){
                    progressDialog.dismiss();
                    customDialog = AppBoiler.customDialogWithBtn(ForgotPasswordActivity.this, s, R.drawable.ic_done, new DialogListener() {
                        @Override
                        public void onClick() {
                            customDialog.dismiss();
                            onBackPressed();
                        }
                    });
                }
            }
        });

        failure.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if(progressDialog!=null){
                    progressDialog.dismiss();
                    customDialog = AppBoiler.customDialogWithBtn(ForgotPasswordActivity.this, s, R.drawable.ic_error, new DialogListener() {
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

        binding.header.backImageView.setOnClickListener(new View.OnClickListener() {
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
                forgotPasswordViewModel.setEmail(binding.emailEditText.getText().toString().trim());
            }
        });

        binding.sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.emailTextInputLayout.isErrorEnabled()) {
                    binding.emailEditText.requestFocus();
                } else if(Validation.isStringEmpty(email)) {
                    binding.emailTextInputLayout.setError("Enter email");
                    binding.emailEditText.requestFocus();
                }else{
                  sendEmail();
                }
            }
        });
    }

    private void sendEmail(){
        if (AppBoiler.isInternetConnected(this)) {
            progressDialog.show();
            forgotPasswordViewModel.sendResetPasswordEmail();
        } else {
            AppBoiler.showSnackBarForInternet(this,binding.rootLayoutOfForgotPassword);
        }
    }
}