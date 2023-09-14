package com.living.roomrental.activity.auth;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;

import com.living.roomrental.databinding.ActivityVerifyOtpBinding;


public class VerifyOtpActivity extends AppCompatActivity {

    private ActivityVerifyOtpBinding verifyOtpBinding;
    private final TextWatcher pinTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 1) {
                verifyOtpBinding.pinView.requestFocus();
                verifyOtpBinding.pinView.setSelection(1);
            } else if (editable.length() == 4) {
                hideKeyboard();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verifyOtpBinding = ActivityVerifyOtpBinding.inflate(getLayoutInflater());
        setContentView(verifyOtpBinding.getRoot());

        showKeyboard(verifyOtpBinding.pinView);
        initListeners();

    }


    private void initListeners() {
        verifyOtpBinding.pinView.addTextChangedListener(pinTextWatcher);

        verifyOtpBinding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            onBackPressed();
            }
        });
    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            verifyOtpBinding.pinView.setFocusable(true);
            verifyOtpBinding.pinView.setFocusableInTouchMode(true);
            verifyOtpBinding.pinView.requestFocus();
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view != null && imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }



}