package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.komsi.solve.Storage.SharedPrefManager;

public class SplashActivity extends AppCompatActivity {
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mHandler = new Handler();
        mStatusChecker.run();

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                if (SharedPrefManager.getInstance(SplashActivity.this).isLoggedIn()) {
                    Intent intent = new Intent(SplashActivity.this, Main2Activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this, LoginRegisterActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                finish();
            }
        }, 2500L);
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

            mHandler.postDelayed(mStatusChecker, 270);
        }
    };
}