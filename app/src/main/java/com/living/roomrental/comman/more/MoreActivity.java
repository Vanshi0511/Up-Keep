package com.living.roomrental.comman.more;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.living.roomrental.R;
import com.living.roomrental.databinding.ActivityMoreBinding;

public class MoreActivity extends AppCompatActivity {

    private ActivityMoreBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initListener();
    }

    private void initListener() {

        binding.header.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.header.headerTitle.setText("Coming Soon");
    }
}