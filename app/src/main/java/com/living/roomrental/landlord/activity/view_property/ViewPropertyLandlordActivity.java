package com.living.roomrental.landlord.activity.view_property;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.living.roomrental.AlertDialogListener;
import com.living.roomrental.DialogListener;
import com.living.roomrental.R;
import com.living.roomrental.ViewProfileListener;
import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.comman.chat.ChatActivity;
import com.living.roomrental.databinding.ActivityViewPropertyLandlordBinding;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyActivity;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyDataModel;
import com.living.roomrental.landlord.activity.create_property.CurrentBookingModel;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;
import com.living.roomrental.utilities.ImplicitUtils;

public class ViewPropertyLandlordActivity extends AppCompatActivity {

    private CreatePropertyDataModel model ;
    private LiveData<String> responseMutableData;

    private  Dialog progressDialog , responseDialog ;

    private ViewPropertyViewModel viewPropertyViewModel;

    private ProfileModel profileModel;


    private ActivityViewPropertyLandlordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewPropertyLandlordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewPropertyViewModel = new ViewModelProvider(this).get(ViewPropertyViewModel.class);
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
                Bundle bundle = new Bundle();
                bundle.putParcelable("data",model);
                AppBoiler.navigateToActivity(ViewPropertyLandlordActivity.this, CreatePropertyActivity.class,bundle);
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
            model = bundle.getParcelable("data");
        }
        if(model!=null){

            if(model.getPropertyImages().size()>0){
                ViewPropertyImageAdapter adapter = new ViewPropertyImageAdapter(this,model.getPropertyImages());

                binding.propertyImageRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
                binding.propertyImageRecyclerView.setAdapter(adapter);
            }

            System.out.println("============= Property name"+model.getPropertyName());
            System.out.println("============= Property name"+model.toString());

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

            System.out.println("============ "+model.toString());
            if(model.getCurrentBooking() == null){
                binding.propertyBookingStatusLayout.setVisibility(View.GONE);
            } else {
                binding.propertyBookingStatusLayout.setVisibility(View.VISIBLE);
                setBookingDetailsToViews(model.getCurrentBooking());
            }
        }
    }

    public void setBookingDetailsToViews(CurrentBookingModel model){

        binding.bookingDateTextView.setText("From : "+model.getBookingDate());
        binding.vacantRoomSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    setAlertDialogForVacant();
                }
            }
        });

        LiveData<ProfileModel> profileModelLiveData = viewPropertyViewModel.getProfileDataFromRepository(model.getTenantId());
        profileModelLiveData.observe(this, new Observer<ProfileModel>() {
            @Override
            public void onChanged(ProfileModel model) {
                if(model==null){
                    Toast.makeText(ViewPropertyLandlordActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                } else {
                    profileModel = model;
                }
            }
        });

        binding.viewProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppBoiler.showProfileDialog(ViewPropertyLandlordActivity.this, profileModel, new ViewProfileListener() {
                    @Override
                    public void onClickContact(String contact) {
                        ImplicitUtils.intentForCall(ViewPropertyLandlordActivity.this,contact);
                    }

                    @Override
                    public void onClickChat(String receiverKey) {
                        Bundle bundle = new Bundle();
                        bundle.putString("receiver_key",receiverKey);
                        AppBoiler.navigateToActivity(ViewPropertyLandlordActivity.this, ChatActivity.class,bundle);
                    }
                }, model.getTenantId());
            }
        });
    }

    private void setAlertDialogForVacant() {

        AppBoiler.showAlertDialog(this, R.drawable.ic_warning, "Vacant", "Are you sure you want to remove tenant booking?", new AlertDialogListener() {
            @Override
            public void onClickPositiveButton() {
                observeResponse();
            }

            @Override
            public void onClickNegativeButton() {
                binding.vacantRoomSwitch.setChecked(false);
            }
        });
    }

    private void observeResponse() {
        if(AppBoiler.isInternetConnected(this)){
            progressDialog = AppBoiler.setProgressDialog(ViewPropertyLandlordActivity.this);

            LiveData<String> responseLiveData = viewPropertyViewModel.updateBookingStatusToVacant(model.getKey());
            responseLiveData.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    progressDialog.dismiss();
                    if(s.equals(AppConstants.SUCCESS)){

                        responseDialog = AppBoiler.customDialogWithBtn(ViewPropertyLandlordActivity.this,"Tenant removed successfully", R.drawable.ic_done, new DialogListener() {
                            @Override
                            public void onClick() {
                                responseDialog.dismiss();
                                onBackPressed();
                            }
                        });

                    } else {
                        responseDialog = AppBoiler.customDialogWithBtn(ViewPropertyLandlordActivity.this, s, R.drawable.ic_error, new DialogListener() {
                            @Override
                            public void onClick() {
                                responseDialog.dismiss();
                            }
                        });
                    }
                }
            });

        } else {
            AppBoiler.showSnackBarForInternet(this,binding.getRoot());
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