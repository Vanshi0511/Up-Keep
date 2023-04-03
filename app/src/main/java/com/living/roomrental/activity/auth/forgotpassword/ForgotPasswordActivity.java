package com.living.roomrental.activity.auth.forgotpassword;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.living.roomrental.DialogListener;
import com.living.roomrental.R;
import com.living.roomrental.databinding.ActivityForgotPasswordBinding;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;
import com.living.roomrental.utilities.Validation;

public class ForgotPasswordActivity extends AppCompatActivity {

    private ActivityForgotPasswordBinding binding;
    private ForgotPasswordViewModel forgotPasswordViewModel;
    private String email;
    private Dialog progressDialog , customDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        forgotPasswordViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);

        binding.header.headerTitle.setText(R.string.forgot_password);
        initListeners();
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
                    binding.emailTextInputLayout.setError(getString(R.string.enter_email));
                } else if(isValid==2) {
                    binding.emailTextInputLayout.setError(getString(R.string.invalid_email_format));
                } else if(isValid==0){
                    AppBoiler.setInputLayoutErrorDisable(binding.emailTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                email = editable.toString().trim();
            }
        });

        binding.sendEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.emailTextInputLayout.isErrorEnabled()) {
                    binding.emailEditText.requestFocus();
                } else if(Validation.isStringEmpty(email)) {
                    binding.emailTextInputLayout.setError(getString(R.string.enter_email));
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

        LiveData<String> responseLiveData = forgotPasswordViewModel.sendResetPasswordEmail(email);

        responseLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                progressDialog.dismiss();
                if(s.equals(AppConstants.SUCCESS)){
                    customDialog = AppBoiler.customDialogWithBtn(ForgotPasswordActivity.this, getString(R.string.link_has_send_to_your_email_id), R.drawable.ic_done, new DialogListener() {
                        @Override
                        public void onClick() {
                            customDialog.dismiss();
                            onBackPressed();
                        }
                    });
                }
                else{
                    customDialog = AppBoiler.customDialogWithBtn(ForgotPasswordActivity.this, getString(R.string.failed_to_send_email), R.drawable.ic_error, new DialogListener() {
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