package com.living.roomrental.activity.profile.create;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.DialogListener;
import com.living.roomrental.ImagePickerDialogListener;
import com.living.roomrental.R;
import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.databinding.ActivityCreateProfileBinding;
import com.living.roomrental.landlord.activity.main.LandlordMainActivity;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;
import com.living.roomrental.tenant.activity.main.TenantMainActivity;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;
import com.living.roomrental.utilities.ImplicitUtils;
import com.living.roomrental.utilities.Validation;


public class CreateProfileActivity extends AppCompatActivity {

    private ActivityCreateProfileBinding binding;
    private CreateProfileViewModel createProfileViewModel;
    private Dialog progressDialog, responseDialog, imagePickerDialog;
    private ActivityResultLauncher<Intent> getImageLauncher;
    private String  whoIsUser;

    private ProfileModel model = new ProfileModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createProfileViewModel = new ViewModelProvider(this).get(CreateProfileViewModel.class);

        binding.header.headerTitle.setText("Create Profile");
        binding.emailEditText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        getBundles();
        getDataFromViewModel();
        initLauncherForImage();
        initListeners();

    }

    private void getBundles() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            whoIsUser = bundle.getString(AppConstants.WHO_IS_USER);
            model.setWhoIsUser(whoIsUser);
        }
    }

    private void getDataFromViewModel() {

        Uri imageUri = createProfileViewModel.getImageUri();
        if (imageUri != null)
            binding.profileImageView.setImageURI(imageUri);
    }

    private void initLauncherForImage() {

        getImageLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == Activity.RESULT_OK) {

                    assert result.getData() != null;
                    Uri imageUri = result.getData().getData();
                    binding.profileImageView.setImageURI(imageUri);
                    createProfileViewModel.setImageUri(imageUri);

                } else {
                    AppBoiler.showCustomSnackBar(binding.rootLayoutOfCreateProfile,"Failed to get image");
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

        binding.createProfileBtn.setOnClickListener(new View.OnClickListener() {
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
                    goForProfile();
                }
            }
        });
    }

    private void goForProfile() {
        if (AppBoiler.isInternetConnected(this)) {
            progressDialog = AppBoiler.setProgressDialog(CreateProfileActivity.this);
            observeResponseForSetTheData();
        } else {
            AppBoiler.showSnackBarForInternet(this, binding.rootLayoutOfCreateProfile);
        }
    }

    private void observeResponseForSetTheData() {

        LiveData<String> response = createProfileViewModel.createUserProfile(this,model);

        response.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                progressDialog.dismiss();
                if (s.equals(AppConstants.SUCCESS)) {
                    responseDialog = AppBoiler.customDialogWithBtn(CreateProfileActivity.this, "Profile Created Successfully", R.drawable.ic_done, new DialogListener() {
                        @Override
                        public void onClick() {
                            responseDialog.dismiss();
                            SharedPreferenceStorage.setProfileStatusOfUser(SharedPreferencesController.getInstance(CreateProfileActivity.this).getPreferences(), whoIsUser);

                            if (whoIsUser.equals(AppConstants.LANDLORD)) {

                                AppBoiler.navigateToActivityWithFinish(CreateProfileActivity.this, LandlordMainActivity.class, null);
                                finishAffinity();
                            } else {
                                AppBoiler.navigateToActivityWithFinish(CreateProfileActivity.this, TenantMainActivity.class, null);
                                finishAffinity();
                            }
                        }
                    });

                } else {

                    responseDialog = AppBoiler.customDialogWithBtn(CreateProfileActivity.this, s, R.drawable.ic_error, new DialogListener() {
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
                createProfileViewModel.setImageUri(null);
            }
        });
    }

}