package com.living.roomrental.activity.profile.edit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.living.roomrental.DialogListener;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.ImagePickerDialogListener;
import com.living.roomrental.R;
import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.databinding.ActivityEditProfileBinding;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;
import com.living.roomrental.utilities.ImplicitUtils;
import com.living.roomrental.utilities.Validation;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;

    private EditProfileViewModel editProfileViewModel;
    private Dialog progressDialog, responseDialog, imagePickerDialog;
    private ActivityResultLauncher<Intent> getImageLauncher;

    private ProfileModel model = new ProfileModel();

    public static boolean isImageRemoved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        binding.header.headerTitle.setText("Update Profile");
        System.out.println("==================== data "+FirebaseController.getInstance().getUser().getEmail());
        binding.emailEditText.setText(FirebaseController.getInstance().getUser().getEmail());
        initListeners();
        getBundleData();
        getDataFromViewModel();


        initLauncherForImage();
    }

    private void getBundleData(){
        Bundle bundle = getIntent().getExtras();

        ProfileModel model = bundle.getParcelable("data");

        binding.nameEditText.setText(model.getName());
        binding.contactNoEditText.setText(model.getContactNo());
        binding.occupationEditText.setText(model.getOccupation());
        binding.addressEditText.setText(model.getAddress());
        binding.bioEditText.setText(model.getBio());

        System.out.println("=================== IMAGE========= "+model.toString());

        if(!Validation.isStringEmpty(model.getImageUrl())){
            Glide.with(this).load(model.getImageUrl()).into(binding.profileImageView);
            this.model.setImageUrl(model.getImageUrl());
        }
    }

    private void getDataFromViewModel() {

        Uri imageUri = editProfileViewModel.getImageUri();
        if (imageUri != null)
            binding.profileImageView.setImageURI(imageUri);
    }

    private void initLauncherForImage() {

        getImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == Activity.RESULT_OK) {

                    isImageRemoved = false ;

                    assert result.getData() != null;
                    Uri imageUri = result.getData().getData();
                    binding.profileImageView.setImageURI(imageUri);
                    editProfileViewModel.setImageUri(imageUri);
                }
            }
        });
    }

    private void initListeners() {
        binding.header.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        binding.cameraImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromLocalStorage();
            }
        });

        binding.nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    binding.nameTextInputLayout.setError("Enter name");
                } else {
                    AppBoiler.setInputLayoutErrorDisable(binding.nameTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                model.setName(editable.toString());
            }
        });

        binding.contactNoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    binding.contactNoTextInputLayout.setError("Enter contact no");
                } else if (charSequence.toString().length() < 10) {
                    binding.contactNoTextInputLayout.setError("Invalid contact no");
                } else {
                    AppBoiler.setInputLayoutErrorDisable(binding.contactNoTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                model.setContactNo(editable.toString());
            }
        });

        binding.occupationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    binding.occupationTextInputLayout.setError("Enter occupation");
                } else {
                    AppBoiler.setInputLayoutErrorDisable(binding.occupationTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                model.setOccupation(editable.toString());
            }
        });

        binding.addressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().length() == 0) {
                    binding.addressTextInputLayout.setError("Enter address");
                } else {
                    AppBoiler.setInputLayoutErrorDisable(binding.addressTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                model.setAddress(editable.toString());
            }
        });

        binding.bioEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                model.setBio(editable.toString());
            }
        });

        binding.updaterProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Validation.isStringEmpty(model.getName())) {
                    binding.nameTextInputLayout.setError("Enter name");
                    binding.nameEditText.requestFocus();

                } else if (binding.contactNoTextInputLayout.isErrorEnabled()) {
                    binding.contactNoEditText.requestFocus();

                } else if (Validation.isStringEmpty(model.getContactNo())) {
                    binding.contactNoTextInputLayout.setError("Enter contact no");
                    binding.contactNoEditText.requestFocus();

                } else if (Validation.isStringEmpty(model.getOccupation())) {
                    binding.occupationTextInputLayout.setError("Enter occupation");
                    binding.occupationEditText.requestFocus();

                } else if (Validation.isStringEmpty(model.getAddress())) {
                    binding.addressTextInputLayout.setError("Enter address");
                    binding.addressEditText.requestFocus();

                } else {
                    editProfile();
                }
            }
        });
    }

    private void editProfile() {
        if (AppBoiler.isInternetConnected(this)) {
            progressDialog = AppBoiler.setProgressDialog(EditProfileActivity.this);
            observeResponseFoSetTheData();
        } else {
            AppBoiler.showSnackBarForInternet(this, binding.rootLayoutOfCreateProfile);
        }
    }

    private void observeResponseFoSetTheData() {

        LiveData<String> responseMutableLiveData = editProfileViewModel.setProfileData(this,model);

        responseMutableLiveData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                progressDialog.dismiss();

                if (s.equals(AppConstants.SUCCESS)) {
                    responseDialog = AppBoiler.customDialogWithBtn(EditProfileActivity.this, "Updated Successfully", R.drawable.ic_done, new DialogListener() {
                        @Override
                        public void onClick() {
                            responseDialog.dismiss();
                            onBackPressed();
                        }
                    });
                } else {
                    responseDialog = AppBoiler.customDialogWithBtn(EditProfileActivity.this, s, R.drawable.ic_error, new DialogListener() {
                        @Override
                        public void onClick() {
                            responseDialog.dismiss();
                        }
                    });

                }
            }
        });
    }
    public void getImageFromLocalStorage() {

        imagePickerDialog = AppBoiler.showImagePickerDialog(this, new ImagePickerDialogListener() {
            @Override
            public void onClickCamera() {
                imagePickerDialog.dismiss();
                Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getImageLauncher.launch(captureImageIntent);
            }

            @Override
            public void onClickGallery() {
                imagePickerDialog.dismiss();
                Intent implicitIntent = ImplicitUtils.getIntentForImagePickGallery();
                getImageLauncher.launch(implicitIntent);
            }

            @Override
            public void onClickRemove() {
                imagePickerDialog.dismiss();
                binding.profileImageView.setImageResource(R.drawable.ic_person);
                editProfileViewModel.setImageUri(null);
                isImageRemoved = true;
            }
        });
    }
}