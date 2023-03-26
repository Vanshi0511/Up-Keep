package com.living.roomrental.activity.auth.forgotpassword;

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
import com.living.roomrental.databinding.ActivityForgotPasswordBinding;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;
import com.living.roomrental.utilities.Validation;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private ForgotPasswordViewModel forgotPasswordViewModel;
    private String email="";
    private LiveData<String> responseLiveData ;
    private Dialog progressDialog , customDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        forgotPasswordViewModel = ViewModelProviders.of(this).get(ForgotPasswordViewModel.class);

        binding.header.headerTitle.setText("Forgot Password");

        getDataFromViewModel();
        initListeners();
    }

    private void getDataFromViewModel() {
        binding.emailEditText.setText(forgotPasswordViewModel.getEmail());
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
            progressDialog = AppBoiler.setProgressDialog(ForgotPasswordActivity.this);
            observeResponseForResetPassword();
        } else {
            AppBoiler.showSnackBarForInternet(this,binding.rootLayoutOfForgotPassword);
        }
    }

    private void observeResponseForResetPassword() {

        responseLiveData = forgotPasswordViewModel.sendResetPasswordEmail();

        responseLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                progressDialog.dismiss();
                if(s.equals(AppConstants.SUCCESS)){
                    customDialog = AppBoiler.customDialogWithBtn(ForgotPasswordActivity.this, "Link has send to your email id.", R.drawable.ic_done, new DialogListener() {
                        @Override
                        public void onClick() {
                            customDialog.dismiss();
                            onBackPressed();
                        }
                    });
                }
                else{
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


}