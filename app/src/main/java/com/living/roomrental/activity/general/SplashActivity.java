package com.living.roomrental.activity.general;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.R;
import com.living.roomrental.activity.auth.login.LoginActivity;
import com.living.roomrental.databinding.ActivitySplashBinding;
import com.living.roomrental.utilities.AppBoiler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(FirebaseController.getInstance().getAuth().getCurrentUser()==null){
                    AppBoiler.navigateToActivityWithFinish(SplashActivity.this, LoginActivity.class,null);
                }else{
                    AppBoiler.navigateToActivityWithFinish(SplashActivity.this, LoginActivity.class,null);
                }

            }
        },2000);
    }
}