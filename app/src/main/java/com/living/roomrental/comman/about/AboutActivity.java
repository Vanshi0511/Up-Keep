package com.living.roomrental.comman.about;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.living.roomrental.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {

    private ActivityAboutBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.header.headerTitle.setText("About");
        initListener();
    }

    private void initListener() {

        binding.header.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}