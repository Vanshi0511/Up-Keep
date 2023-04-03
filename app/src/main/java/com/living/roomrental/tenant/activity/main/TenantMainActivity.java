package com.living.roomrental.tenant.activity.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.living.roomrental.R;
import com.living.roomrental.activity.profile.view.ViewProfileBottomSheet;
import com.living.roomrental.databinding.ActivityTenantMainBinding;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyActivity;
import com.living.roomrental.landlord.activity.main.LandlordMainActivity;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.Validation;

import java.util.ArrayList;

public class TenantMainActivity extends AppCompatActivity {

    private ActivityTenantMainBinding binding;
    private ViewProfileBottomSheet bottomSheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTenantMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initListeners();
    }

    private void getDataFromLocal(){
        ArrayList<String> list ;
        list = SharedPreferenceStorage.getUserExtraData(SharedPreferencesController.getInstance(this).getPreferences());

        if(!Validation.isStringEmpty(list.get(1)))
            Glide.with(this).load(list.get(1)).into(binding.header.headerProfileImageView);
        else{
            binding.header.headerProfileImageView.setImageResource(R.drawable.ic_person);
        }
        binding.header.headerUserName.setText(list.get(0));
    }

    private void initListeners() {

        binding.header.headerProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet = new ViewProfileBottomSheet();
                bottomSheet.show(getSupportFragmentManager(), "ViewProfileBottomSheet");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromLocal();
        if(bottomSheet!=null){
            bottomSheet.dismiss();
        }
    }
}