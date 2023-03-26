package com.living.roomrental.landlord.activity.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.living.roomrental.R;

public class LandlordMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landlord_main);

        System.out.println("Landlord");
    }
}