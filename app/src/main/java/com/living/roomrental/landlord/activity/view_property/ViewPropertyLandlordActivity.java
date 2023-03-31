package com.living.roomrental.landlord.activity.view_property;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.living.roomrental.AlertDialogListener;
import com.living.roomrental.DialogListener;
import com.living.roomrental.R;
import com.living.roomrental.activity.auth.register.RegisterActivity;
import com.living.roomrental.databinding.ActivityViewPropertyLandlordBinding;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyDataModel;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;

public class ViewPropertyLandlordActivity extends AppCompatActivity {

    private CreatePropertyDataModel model ;
    private LiveData<String> responseMutableData;

    private  Dialog progressDialog , responseDialog ;


    private ActivityViewPropertyLandlordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewPropertyLandlordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.header.headerTitle.setText("Property Details");
        initData();
        initListeners();
    }

    private void initListeners() {

        binding.header.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo edit property
            }
        });

        binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo delete property
                deleteProperty();
            }
        });
    }

    private void initData() {

        Bundle bundle = getIntent().getExtras();

        if(model==null){
            model = (CreatePropertyDataModel) bundle.getSerializable("data");
        }
        if(model!=null){

            if(model.getPropertyImages().size()>0){
                ViewPropertyImageAdapter adapter = new ViewPropertyImageAdapter(this,model.getPropertyImages());

                binding.propertyImageRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
                binding.propertyImageRecyclerView.setAdapter(adapter);
            }

            binding.propertyNameData.setText(model.getPropertyName());
            binding.addressData.setText(model.getMapLocationAddress());
            binding.landmarkData.setText(model.getLandmarkAddress());
            binding.propertyTypeData.setText(model.getType());
            binding.propertyForData.setText(model.getPeopleFor());
            binding.propertySizeData.setText(model.getSize());
            binding.propertyAgreementData.setText(model.getAgreement());
            binding.propertyFacilitiesData.setText(model.getFacilities().toString());
            binding.propertyFurnishingData.setText(model.getFurnishing());
            binding.furnishingDescriptionData.setText(model.getFurnishingDescription());
            binding.propertyRentData.setText(model.getRent());
            binding.propertyDescriptionData.setText(model.getDescription());
        }
    }
    private void deleteProperty(){

        AppBoiler.showAlertDialog(this, R.drawable.ic_warning, "Delete", "Are you sure you want to delete property?", new AlertDialogListener() {
            @Override
            public void onClickPositiveButton() {
                deleteData();
            }

            @Override
            public void onClickNegativeButton() {

            }
        });
    }
    private void deleteData(){

        progressDialog = AppBoiler.setProgressDialog(this);

        ViewPropertyRepository repository = new ViewPropertyRepository(model);

        if(model.getPropertyImages().size()>0){
            responseMutableData = repository.deleteImageFromServer();
        } else {
            responseMutableData = repository.deleteDataFromServer();
        }
            responseMutableData.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    progressDialog.dismiss();

                    if(s.equals(AppConstants.SUCCESS)){
                        responseDialog = AppBoiler.customDialogWithBtn(ViewPropertyLandlordActivity.this,"Property Deleted Successfully", R.drawable.ic_done, new DialogListener() {
                            @Override
                            public void onClick() {
                                responseDialog.dismiss();
                                onBackPressed();
                            }
                        });
                    }
                    else{
                        responseDialog = AppBoiler.customDialogWithBtn(ViewPropertyLandlordActivity.this, s, R.drawable.ic_error, new DialogListener() {
                            @Override
                            public void onClick() {
                                responseDialog.dismiss();
                            }
                        });

                    }
                }
            });
    }
}