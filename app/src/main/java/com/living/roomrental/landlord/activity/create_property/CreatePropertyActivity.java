package com.living.roomrental.landlord.activity.create_property;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.living.roomrental.ImagePickerDialogListener;
import com.living.roomrental.R;
import com.living.roomrental.databinding.ActivityCreatePropertyBinding;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.ImplicitUtils;

import java.util.ArrayList;

public class CreatePropertyActivity extends AppCompatActivity {

    private ActivityCreatePropertyBinding binding;

    private CreatePropertyViewModel createPropertyViewModel;
    private Dialog imagePickerDialog;
    private ActivityResultLauncher<Intent> imageResultLauncher;

    private ArrayList<Uri> propertyImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreatePropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createPropertyViewModel = ViewModelProviders.of(this).get(CreatePropertyViewModel.class);

        binding.header.headerTitle.setText("Create Property");
        initListeners();
        setArrayAdaptersToSpinner();

        initLauncherForImage();
    }

    private void initLauncherForImage() {

        imageResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    // todo set data to view model and observe it and generate recycler view
                }
            }
        });
    }

    private void initListeners() {
        //binding.propertyImageRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL , false));


        binding.addImageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePickerDialog();
            }
        });

        binding.propertyNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()==0){
                    binding.propertyNameTextInputLayout.setError("Enter property name");
                }else{
                    AppBoiler.setInputLayoutErrorDisable(binding.propertyNameTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                createPropertyViewModel.setPropertyName(editable.toString());
            }
        });

        binding.propertyStreetAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()==0){
                    binding.propertyStreetAddressTextInputLayout.setError("Enter street address");
                }else{
                    AppBoiler.setInputLayoutErrorDisable(binding.propertyStreetAddressTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                createPropertyViewModel.setStreetAddress(editable.toString());
            }
        });

        binding.propertyPinCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()==0){
                    binding.propertyPinCodeTextInputLayout.setError("Enter pincode");
                }else{
                    AppBoiler.setInputLayoutErrorDisable(binding.propertyPinCodeTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                createPropertyViewModel.setPincodeAddress(editable.toString());
            }
        });

        binding.propertyLandmarkEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()==0){
                    binding.propertyLandmarkTextInputLayout.setError("Enter landmark");
                }else{
                    AppBoiler.setInputLayoutErrorDisable(binding.propertyLandmarkTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                createPropertyViewModel.setLandmarkAddress(editable.toString());
            }
        });

        binding.locationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo open map and get location
            }
        });

        binding.propertyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try{
                    TextView textView = (TextView) view;
                    textView.setTextColor(getColor(R.color.black_text_secondary));
                    textView.setTypeface(getTypeFace());
                    createPropertyViewModel.setType(adapterView.getSelectedItem().toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.propertyRentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()==0){
                    binding.propertyRentTextInputLayout.setError("Enter property rent");
                }else{
                    AppBoiler.setInputLayoutErrorDisable(binding.propertyRentTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                createPropertyViewModel.setRent(editable.toString());
            }
        });

        binding.propertyPeopleForSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                try{
                    TextView textView = (TextView) view;
                    textView.setTextColor(getColor(R.color.black_text_secondary));
                    textView.setTypeface(getTypeFace());
                    createPropertyViewModel.setPeopleFor(adapterView.getSelectedItem().toString());
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.propertySizeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()==0){
                    binding.propertySizeTextInputLayout.setError("Enter property size");
                }else{
                    AppBoiler.setInputLayoutErrorDisable(binding.propertySizeTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                createPropertyViewModel.setSize(editable.toString());
            }
        });

        binding.propertyAgreementEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()==0){
                    binding.propertyAgreementTextInputLayout.setError("Enter agreement status");
                }else{
                    AppBoiler.setInputLayoutErrorDisable(binding.propertyAgreementTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                createPropertyViewModel.setAgreement(editable.toString());
            }
        });



        binding.propertyDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                createPropertyViewModel.setDescription(editable.toString());
            }
        });

        binding.createPropertyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.furnishingRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.unFurnishedRadioButton: binding.propertyFurnishingTextInputLayout.setVisibility(View.GONE);
                        break;
                    case R.id.furnishedRadioButton:
                    case R.id.semiFurnishedRadioButton: binding.propertyFurnishingTextInputLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

//        binding.nestedScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                binding.nestedScrollView.post(new Runnable() {
//                    public void run() {
//                        binding.nestedScrollView.scrollTo(0, binding.nestedScrollView.getBottom() + binding.nestedScrollView.getScrollY());
//                    }
//                });
//            }
//        });
    }

    private void openImagePickerDialog() {
        imagePickerDialog = AppBoiler.showImagePickerDialog(this, new ImagePickerDialogListener() {
            @Override
            public void onClickCamera() {

            }

            @Override
            public void onClickGallery() {
                imagePickerDialog.dismiss();
                Intent implicitIntent = ImplicitUtils.getIntentForImagePickGallery();
                imageResultLauncher.launch(implicitIntent);
            }

            @Override
            public void onClickRemove() {

            }
        });
    }

    private void setArrayAdaptersToSpinner(){

        ArrayList<String> peopleForList = new ArrayList<String>();
        peopleForList.add("Select property tenant");
        peopleForList.add("Only boys");
        peopleForList.add("Only girls");
        peopleForList.add("Family");
        peopleForList.add("Only girls and family");
        peopleForList.add("Anyone");
        ArrayAdapter<String> peopleForAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,peopleForList){
            @Override
            public boolean isEnabled(int position) {
                return position!=0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if(position==0){
                    textView.setTextColor(getColor(R.color.black_text_disabled));
                }
                textView.setTypeface(getTypeFace());
                return view;
            }
        };
        binding.propertyPeopleForSpinner.setAdapter(peopleForAdapter);

        ArrayList<String> typeList = new ArrayList<>();
        typeList.add("Select property type");
        typeList.add("Single room");
        typeList.add("1 RK");
        typeList.add("1 BHK");
        typeList.add("2 BHK");
        typeList.add("3 BHK");
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,typeList){
            @Override
            public boolean isEnabled(int position) {
                return position!=0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if(position==0){
                    textView.setTextColor(getColor(R.color.black_text_disabled));
                }
                textView.setTypeface(getTypeFace());
                return view;
            }
        };
        binding.propertyTypeSpinner.setAdapter(typeAdapter);

    }

    private Typeface getTypeFace(){
        Typeface typeface = ResourcesCompat.getFont(this, R.font.montserrat_medium);
        return typeface;
    }
}