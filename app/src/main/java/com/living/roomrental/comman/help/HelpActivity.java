package com.living.roomrental.comman.help;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.living.roomrental.databinding.ActivityHelpBinding;
import com.living.roomrental.utilities.ImplicitUtils;
import com.living.roomrental.utilities.Validation;

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

        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = binding.queryMessage.getText().toString();
                if(!Validation.isStringEmpty(message)){

                    ImplicitUtils.intentForMail(HelpActivity.this , message);
                }
            }
        });
    }
}