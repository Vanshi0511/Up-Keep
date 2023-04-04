package com.living.roomrental.landlord.activity.create_property;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.living.roomrental.DialogListener;
import com.living.roomrental.ImagePickerDialogListener;
import com.living.roomrental.R;
import com.living.roomrental.databinding.ActivityCreatePropertyBinding;
import com.living.roomrental.landlord.activity.PropertyMapActivity;
import com.living.roomrental.landlord.activity.edit_property.EditPropertyImageAdapter;
import com.living.roomrental.landlord.activity.view_property.ViewPropertyImageAdapter;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;
import com.living.roomrental.utilities.ImplicitUtils;
import com.living.roomrental.utilities.Validation;

import java.util.ArrayList;

public class CreatePropertyActivity extends AppCompatActivity {

    private ActivityCreatePropertyBinding binding;
    private CreatePropertyViewModel createPropertyViewModel;
    private Dialog imagePickerDialog , progressDialog , responseDialog ;
    private ActivityResultLauncher<Intent> imageResultLauncher , mapLocationLauncher;
    private PropertyImageAdapter propertyImageAdapter ;
    private EditPropertyImageAdapter editPropertyImageAdapter;

    private boolean isFromEdit ;
    private boolean isImageSelected ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreatePropertyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createPropertyViewModel = new ViewModelProvider(this).get(CreatePropertyViewModel.class);

        binding.header.headerTitle.setText("Create Property");


        if(getIntent().getExtras()!=null)
            isFromEdit=true;

        setAdapterForImage();
        initListeners();
        setArrayAdaptersToSpinner();

        getBundleData();

        setDataFromViewModel();
        initLaunchers();
    }

    private void getBundleData(){
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            CreatePropertyDataModel model = bundle.getParcelable("data");
            binding.propertyNameEditText.setText(model.getPropertyName());
            binding.propertyLandmarkEditText.setText(model.getLandmarkAddress());
            binding.mapLocationTextView.setText(model.getMapLocationAddress());

            binding.mapLocationTextView.setText(model.getMapLocationAddress());
            createPropertyViewModel.setLatitude(model.getLatitude());
            createPropertyViewModel.setLongitude(model.getLongitude());


            binding.propertyRentEditText.setText(model.getRent());
            binding.propertySizeEditText.setText(model.getSize());
            binding.propertyAgreementEditText.setText(model.getAgreement());
            binding.propertyDescriptionEditText.setText(model.getDescription());


            if(model.getFurnishing().equals("UnFurnished")){
                binding.unFurnishedRadioButton.setChecked(true);
            } else if(model.getFurnishing().equals("Semi-Furnished")){
                binding.semiFurnishedRadioButton.setChecked(true);
            } else {
                binding.furnishedRadioButton.setChecked(true);
            }
            binding.propertyFurnishingEditText.setText(model.getFurnishingDescription());


            if(model.getFacilities().size()>0){
               for(String facility : model.getFacilities()){
                   if(facility.equals("RO Water"))
                       binding.waterFacilityCheckBox.setChecked(true);
                   else if(facility.equals("Electricity"))
                       binding.lightFacilityCheckBox.setChecked(true);
                   else
                       binding.parkingFacilityCheckBox.setChecked(true);
               }
            }

            setSpinnerItemByValue(binding.propertyTypeSpinner,model.getType());
            setSpinnerItemByValue(binding.propertyPeopleForSpinner,model.getPeopleFor());

            if(model.getPropertyImages().size()>0) {
                createPropertyViewModel.setImagesUrlArrayList(model.getPropertyImages());
            }
            isFromEdit = true;
        }
    }

    public void setDataFromViewModel(){
        binding.mapLocationTextView.setText(createPropertyViewModel.getMapLocationAddress());
        notifyAdapter();
    }

    private void setAdapterForImage(){

            if(propertyImageAdapter==null)
                propertyImageAdapter = new PropertyImageAdapter(this);

            binding.propertyImageRecyclerView.setAdapter(propertyImageAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapterForImage();
    }

    private void initLaunchers() {

        imageResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    // todo set image uri to view model and then notify adapter
                    Intent response = result.getData();
                    if(response!=null)
                    {
                        if(response.getClipData()!=null) //if images is more than one (multiple)
                        {
                            for(int i=0;i<response.getClipData().getItemCount();i++)
                                createPropertyViewModel.setPropertyImages(response.getClipData().getItemAt(i).getUri());
                        }
                        else
                            createPropertyViewModel.setPropertyImages(response.getData()); //single image

                        notifyAdapter();
                    }
                }
                else{
                    AppBoiler.showCustomSnackBar(binding.rootLayoutOfCreateProperty,"Failed to get Image");
                }
            }
        });

        mapLocationLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK){

                   if(result.getData()!=null){
                       Bundle bundle = result.getData().getExtras();

                       binding.mapLocationTextView.setText(bundle.getString("address"));
                       createPropertyViewModel.setLatitude(bundle.getDouble("latitude"));
                       createPropertyViewModel.setLongitude(bundle.getDouble("longitude"));
                   }
                }
                else{
                    AppBoiler.showCustomSnackBar(binding.rootLayoutOfCreateProperty,"Failed to get Location");
                }
            }
        });
    }
    private void notifyAdapter(){
        propertyImageAdapter.setImagesList(createPropertyViewModel.getPropertyImages());
    }

    private void initListeners() {

        binding.propertyImageRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL , false));

                propertyImageAdapter.setListener(new PropertyImageAdapter.ItemListener() {
                    @Override
                    public void onClickCancel(int position) {
                        propertyImageAdapter.removeImage(position);
                    }
                });

        binding.addImageCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImagePickerDialog();
            }
        });

        binding.header.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
            }
        });

        binding.mapLocationTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // todo remove error
                    binding.mapLocationLinaerLayout.setBackgroundResource(R.drawable.layout_border);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                createPropertyViewModel.setMapLocationAddress(editable.toString());
            }
        });

        binding.locationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo open map and get location
                AppBoiler.navigateToActivityForResult(CreatePropertyActivity.this, PropertyMapActivity.class,null,mapLocationLauncher);
            }
        });

        binding.propertyTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                try{
                    TextView textView = (TextView) adapterView.getSelectedView();
                    textView.setTextColor(getColor(R.color.black_text_secondary));
                    textView.setTypeface(getTypeFace());
                    if(position!=0){
                        createPropertyViewModel.setType(adapterView.getSelectedItem().toString());
                        binding.propertyTypeSpinnerLayout.setBackgroundResource(R.drawable.layout_border);
                    }

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
            }
        });

        binding.propertyPeopleForSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                try{
                    TextView textView = (TextView) adapterView.getSelectedView();
                    textView.setTextColor(getColor(R.color.black_text_secondary));
                    textView.setTypeface(getTypeFace());
                    if(position!=0){
                        createPropertyViewModel.setPeopleFor(adapterView.getSelectedItem().toString());
                        binding.propertyPeopleForSpinnerLayout.setBackgroundResource(R.drawable.layout_border);
                    }

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
            }
        });

        binding.furnishingRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton button = (RadioButton)findViewById(id);
                createPropertyViewModel.setFurnishing(button.getText().toString());

                binding.furnishingTextView.setTextColor(getColor(R.color.app_default_color));
                switch (id){
                    case R.id.unFurnishedRadioButton: binding.propertyFurnishingTextInputLayout.setVisibility(View.GONE);
                        break;
                    case R.id.furnishedRadioButton:
                    case R.id.semiFurnishedRadioButton: binding.propertyFurnishingTextInputLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        binding.propertyFurnishingEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length()==0){
                    binding.propertyFurnishingTextInputLayout.setError("Enter Furnished items");
                }else{
                    AppBoiler.setInputLayoutErrorDisable(binding.propertyFurnishingTextInputLayout);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        binding.lightFacilityCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String facility = binding.lightFacilityCheckBox.getText().toString();
                if(binding.lightFacilityCheckBox.isChecked()){
                    createPropertyViewModel.setFacilitiesArrayList(facility);
                }
                else{
                    createPropertyViewModel.setFacilitiesArrayList(facility);
                }
            }
        });

        binding.waterFacilityCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String facility = binding.waterFacilityCheckBox.getText().toString();
                if(binding.waterFacilityCheckBox.isChecked()){
                    createPropertyViewModel.setFacilitiesArrayList(facility);
                }
                else{
                    createPropertyViewModel.setFacilitiesArrayList(facility);
                }
            }
        });

        binding.parkingFacilityCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String facility = binding.parkingFacilityCheckBox.getText().toString();
                if(binding.parkingFacilityCheckBox.isChecked()){
                    createPropertyViewModel.setFacilitiesArrayList(facility);
                }
                else{
                    createPropertyViewModel.setFacilitiesArrayList(facility);
                }
            }
        });

        binding.createPropertyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("============  "+createPropertyViewModel.toString());


                String propertyName = binding.propertyNameEditText.getText().toString().trim();
                String propertyLandmark = binding.propertyLandmarkEditText.getText().toString().trim();
                String propertyRent = binding.propertyRentEditText.getText().toString().trim();
                String propertySize  = binding.propertySizeEditText.getText().toString().trim();
                String propertyAgreement = binding.propertyAgreementEditText.getText().toString().trim();
                String propertyFurnishingDescription = binding.propertyFurnishingEditText.getText().toString().trim();
                String description = binding.propertyDescriptionEditText.getText().toString().trim();


                if(isValidationSuccess(propertyName,propertyLandmark,propertyRent,propertySize,propertyAgreement,propertyFurnishingDescription)){

                    CreatePropertyDataModel model = new CreatePropertyDataModel(propertyName,propertyLandmark, createPropertyViewModel.getMapLocationAddress(),propertyRent,propertySize,propertyAgreement,description, createPropertyViewModel.getPeopleFor(), createPropertyViewModel.getType(), createPropertyViewModel.getFurnishing(), propertyFurnishingDescription, createPropertyViewModel.getLatitude(), createPropertyViewModel.getLongitude(),null,createPropertyViewModel.getFacilitiesArrayList() , createPropertyViewModel.getBookingStatus() , null);
                    setDataToServer(model);
                }
            }
        });
    }

    public boolean isValidationSuccess(String propertyName , String propertyLandmark,String propertyRent,String propertySize,String propertyAgreement,String propertyFurnishingDescription){

        boolean isValid;

        if(Validation.isStringEmpty(propertyName)) {
            binding.propertyNameTextInputLayout.setError("Enter name");
            binding.propertyNameEditText.requestFocus();
            isValid =  false;
        }
        else if(Validation.isStringEmpty(createPropertyViewModel.getMapLocationAddress())){
            binding.mapLocationLinaerLayout.setBackgroundResource(R.drawable.error_border_drawable);
            binding.mapLocationTextView.setFocusable(true);
            binding.mapLocationTextView.requestFocus();
            isValid = false;
        }
        else if(Validation.isStringEmpty(propertyLandmark)){
            binding.propertyLandmarkTextInputLayout.setError("Enter landmark");
            binding.propertyLandmarkTextInputLayout.requestFocus();
            isValid = false;
        }
        else if(Validation.isStringEmpty(createPropertyViewModel.getType())){
            binding.propertyTypeSpinnerLayout.setBackgroundResource(R.drawable.error_border_drawable);
            binding.selectPropertyTypeRequestFocus.setFocusable(true);
            binding.selectPropertyTypeRequestFocus.requestFocus();
            isValid = false;
        }
        else if(Validation.isStringEmpty(propertyRent)){
            binding.propertyRentTextInputLayout.setError("Enter property rent");
            binding.propertyRentEditText.requestFocus();
            isValid = false;
        }
        else if(Validation.isStringEmpty(createPropertyViewModel.getPeopleFor())){
            binding.propertyPeopleForSpinnerLayout.setBackgroundResource(R.drawable.error_border_drawable);
            binding.selectPropertyForRequestFocus.setFocusable(true);
            binding.selectPropertyForRequestFocus.requestFocus();
            isValid = false;
        }
        else if(Validation.isStringEmpty(propertySize)){
            binding.propertySizeTextInputLayout.setError("Enter property size");
            binding.propertySizeEditText.requestFocus();
            isValid = false;
        }
        else if(Validation.isStringEmpty(propertyAgreement)){
            binding.propertyAgreementTextInputLayout.setError("Enter agreement status");
            binding.propertyAgreementEditText.requestFocus();
            isValid = false;
        }
        else if(Validation.isStringEmpty(createPropertyViewModel.getFurnishing())){
            Toast.makeText(CreatePropertyActivity.this, "Select furnishing", Toast.LENGTH_SHORT).show();
            binding.furnishingTextView.setTextColor(getColor(R.color.error_color));
            isValid = false;
        }
        else if((createPropertyViewModel.getFurnishing().equals("Semi-Furnished") || createPropertyViewModel.getFurnishing().equals("Full Furnished") ) && Validation.isStringEmpty(propertyFurnishingDescription)) {
            binding.propertyFurnishingTextInputLayout.setError("Enter furnishing items");
            binding.propertyFurnishingEditText.requestFocus();
            isValid = false;
        }
        else {
            isValid = true;
        }
        return isValid;
    }


    private void setDataToServer(CreatePropertyDataModel model){
        if (AppBoiler.isInternetConnected(this)) {
            progressDialog = AppBoiler.setProgressDialog(CreatePropertyActivity.this);
            observeDataForSetTheData(model);
        } else {
            AppBoiler.showSnackBarForInternet(this, binding.rootLayoutOfCreateProperty);
        }
    }
    private void observeDataForSetTheData(CreatePropertyDataModel model){
        LiveData<String> responseData  = createPropertyViewModel.setDataOfPropertyToServer(model);

        responseData.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressDialog.dismiss();

                if (s.equals(AppConstants.SUCCESS)) {
                    responseDialog = AppBoiler.customDialogWithBtn(CreatePropertyActivity.this,"Property Created Successfully", R.drawable.ic_done, new DialogListener() {
                        @Override
                        public void onClick() {
                            responseDialog.dismiss();
                            onBackPressed();
                        }
                    });

                } else {
                    responseDialog = AppBoiler.customDialogWithBtn(CreatePropertyActivity.this, s, R.drawable.ic_error, new DialogListener() {
                        @Override
                        public void onClick() {
                            responseDialog.dismiss();
                        }
                    });
                }
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

    private void openImagePickerDialog() {
        imagePickerDialog = AppBoiler.showImagePickerDialog(this, new ImagePickerDialogListener() {
            @Override
            public void onClickCamera() {
                // todo get image from camera
                imagePickerDialog.dismiss();
            }

            @Override
            public void onClickGallery() {
                imagePickerDialog.dismiss();
                imageResultLauncher.launch(ImplicitUtils.getMultipleImagesFromGallery());
            }

            @Override
            public void onClickRemove() {
                // no need to remove image
            }
        });
    }

    public void setSpinnerItemByValue(Spinner spinner, String value) {

        SpinnerAdapter adapter = (SpinnerAdapter) spinner.getAdapter();

        for (int position = 0; position < adapter.getCount(); position++) {
            if(adapter.getItem(position).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(position);
                return;
            }
        }
    }
}