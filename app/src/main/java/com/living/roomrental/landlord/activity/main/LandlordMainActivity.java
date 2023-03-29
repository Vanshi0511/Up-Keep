package com.living.roomrental.landlord.activity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationBarView;
import com.living.roomrental.R;
import com.living.roomrental.databinding.ActivityLandlordMainBinding;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyActivity;
import com.living.roomrental.utilities.AppBoiler;

public class LandlordMainActivity extends AppCompatActivity {

    private ActivityLandlordMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLandlordMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        initListeners();
    }

    private void initListeners() {

        binding.fabCreateProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppBoiler.navigateToActivity(LandlordMainActivity.this, CreatePropertyActivity.class,null);
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.myProperty : loadFragment();
                        break;
                    case R.id.myBookings:  loadFragment();
                        break;
                    case R.id.myChats:  loadFragment();
                        break;
                    case R.id.myProfile:  loadFragment();
                        break;
                    default:
                }
                return true;
            }
        });
    }

    private void loadFragment() {

    }
}