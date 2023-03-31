package com.living.roomrental.landlord.activity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationBarView;
import com.living.roomrental.R;
import com.living.roomrental.databinding.ActivityLandlordMainBinding;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyActivity;
import com.living.roomrental.landlord.activity.fragments.FragmentController;
import com.living.roomrental.landlord.activity.fragments.chat.ChatFragment;
import com.living.roomrental.landlord.activity.fragments.home.MyPropertyFragment;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.TypeConverters;

import java.util.ArrayList;

public class LandlordMainActivity extends AppCompatActivity {

    private ActivityLandlordMainBinding binding;

    private FragmentController controller  = new FragmentController(this);;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLandlordMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        getDataFromLocal();
        initListeners();
    }

    private void getDataFromLocal(){
        ArrayList<String> list ;
        list = SharedPreferenceStorage.getUserExtraData(SharedPreferencesController.getInstance(this).getPreferences());

        Glide.with(this).load(list.get(1)).into(binding.header.headerProfileImageView);
        binding.header.headerUserName.setText(list.get(0));
    }

    private void initListeners() {

        binding.fabCreateProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppBoiler.navigateToActivity(LandlordMainActivity.this, CreatePropertyActivity.class,null);
            }
        });

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(fragment==null)
          controller.addFragment(new MyPropertyFragment(),R.id.fragmentContainer);

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.myProperty :
                         controller.replaceFragment(new MyPropertyFragment(),R.id.fragmentContainer);
                         break;
                    case R.id.myBookings:

                        break;
                    case R.id.myChats:
                        controller.replaceFragment(new ChatFragment(),R.id.fragmentContainer);
                        break;
                    case R.id.myProfile:
                        break;
                    default:
                }
                return true;
            }
        });

        binding.bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });
    }
}