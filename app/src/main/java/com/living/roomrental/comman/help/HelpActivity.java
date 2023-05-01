package com.living.roomrental.comman.help;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.living.roomrental.databinding.ActivityHelpBinding;

public class HelpActivity extends AppCompatActivity {

    private ActivityHelpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.header.headerTitle.setText("Help");
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