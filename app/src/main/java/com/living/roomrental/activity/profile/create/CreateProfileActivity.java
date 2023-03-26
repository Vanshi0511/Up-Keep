package com.living.roomrental.activity.profile.create;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.living.roomrental.DialogListener;
import com.living.roomrental.R;
import com.living.roomrental.activity.auth.login.LoginActivity;
import com.living.roomrental.activity.auth.register.RegisterActivity;
import com.living.roomrental.activity.general.BottomSheetChoiceFragment;
import com.living.roomrental.databinding.ActivityCreateProfileBinding;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.Validation;

public class CreateProfileActivity extends AppCompatActivity {

    private ActivityCreateProfileBinding binding;
    private CreateProfileViewModel createProfileViewModel;
    private Dialog progressDialog , customDialog;
    private String name , email , contactNo , occupation , address , bio ,whoIsUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createProfileViewModel = ViewModelProviders.of(this).get(CreateProfileViewModel.class);


        getBundles();
        getDataFromViewModel();
        initListeners();
    }

    private void getBundles(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            if(bundle.getString("intentFor").equals("createProfile")){
                whoIsUser = bundle.getString("whoIsUser");
                binding.header.headerTitle.setText("Create profile");
            }else{
                binding.header.headerTitle.setText("Update profile");
            }
        }
    }

    private void getDataFromViewModel(){
        binding.nameEditText.setText(createProfileViewModel.getName());
        binding.contactNoEditText.setText(createProfileViewModel.getContactNo());
        binding.occupationEditText.setText(createProfileViewModel.getOccupation());
        binding.addressEditText.setText(createProfileViewModel.getAddress());
        binding.bioEditText.setText(createProfileViewModel.getBio());
    }
    private void initListeners(){

        binding.cameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = charSequence.toString();
               if(name.length()==0){
                   binding.nameTextInputLayout.setError("Enter name");
               }else{
                   AppBoiler.setInputLayoutErrorDisable(binding.nameTextInputLayout);
               }
            }

            @Override
            public void afterTextChanged(Editable editable) {
               createProfileViewModel.setName(name);
            }
        });

        binding.contactNoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                contactNo = charSequence.toString();
                if(contactNo.length()==0){
                    binding.contactNoTextInputLayout.setError("Enter contact no");
                }else if(contactNo.length()<10){
                    binding.contactNoTextInputLayout.setError("Invalid contact no");
                } else{
                    AppBoiler.setInputLayoutErrorDisable(binding.contactNoTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                createProfileViewModel.setContactNo(contactNo);
            }
        });

        binding.occupationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                occupation = charSequence.toString();
                if(occupation.length()==0){
                    binding.occupationTextInputLayout.setError("Enter occupation");
                }else{
                    AppBoiler.setInputLayoutErrorDisable(binding.occupationTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                createProfileViewModel.setOccupation(occupation);
            }
        });

        binding.addressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                address = charSequence.toString();
                if(address.length()==0){
                    binding.addressTextInputLayout.setError("Enter address");
                }else{
                    AppBoiler.setInputLayoutErrorDisable(binding.addressTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                createProfileViewModel.setAddress(address);
            }
        });

        binding.bioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                bio = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                createProfileViewModel.setBio(bio);
            }
        });

        binding.createOrEditProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.nameTextInputLayout.isErrorEnabled() || Validation.isStringEmpty(name)){
                    binding.nameTextInputLayout.setError("Enter name");
                }else if(binding.contactNoTextInputLayout.isErrorEnabled()){
                    binding.contactNoEditText.requestFocus();
                } else if(Validation.isStringEmpty(contactNo)){
                    binding.contactNoTextInputLayout.setError("Enter contact no");
                } else if(binding.occupationTextInputLayout.isErrorEnabled() || Validation.isStringEmpty(occupation)){
                    binding.occupationTextInputLayout.setError("Enter occupation");
                } else if(binding.addressTextInputLayout.isErrorEnabled() || Validation.isStringEmpty(address)){
                    binding.addressTextInputLayout.setError("Enter address");
                } else{

                    createProfileViewModel.setWhoIsUser(whoIsUser);
                    goForProfile();
                }
            }
        });
    }

    private void goForProfile(){
        if (AppBoiler.isInternetConnected(this)) {
            progressDialog = AppBoiler.setProgressDialog(CreateProfileActivity.this);
            observeResponse();
        } else {
            AppBoiler.showSnackBarForInternet(this,binding.rootLayoutOfCreateProfile);
        }
    }
    private void observeResponse(){
        LiveData<String> response  = createProfileViewModel.createOrEditUserProfile();

        response.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(s.equals("success")){
                        progressDialog.dismiss();
                        customDialog = AppBoiler.customDialogWithBtn(CreateProfileActivity.this, s, R.drawable.ic_done, new DialogListener() {
                            @Override
                            public void onClick() {
                                customDialog.dismiss();
                                //todo main activity
                            }
                        });

                }
                else {
                        progressDialog.dismiss();
                        customDialog = AppBoiler.customDialogWithBtn(CreateProfileActivity.this, s, R.drawable.ic_error, new DialogListener() {
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