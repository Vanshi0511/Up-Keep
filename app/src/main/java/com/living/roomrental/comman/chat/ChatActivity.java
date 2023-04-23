package com.living.roomrental.comman.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.living.roomrental.databinding.ActivityChatBinding;
import com.living.roomrental.utilities.Validation;


public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.header.headerTitle.setText("Chat");
        initListener();
    }

    private void initListener(){

        binding.header.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = binding.messageEditText.getText().toString().trim();
                if(Validation.isStringEmpty(message)){
                    return;
                } else {

                }
            }
        });
    }
}