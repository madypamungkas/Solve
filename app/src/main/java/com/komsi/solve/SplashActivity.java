package com.komsi.solve;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    private Handler mHandler;

    ImageView splashFirst;
    ImageView splashTwo;
    ImageView splashThree;
    ImageView splashFour;

    int loadPosition = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        splashFirst = findViewById(R.id.custom_loading_logo_one);
        splashTwo = findViewById(R.id.custom_loading_logo_two);
        splashThree = findViewById(R.id.custom_loading_logo_three);
        splashFour = findViewById(R.id.custom_loading_logo_four);

        mHandler = new Handler();
        mStatusChecker.run();

        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));

                finish();
            }
        }, 3000L);
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {

          //  displayLoadingPosition(loadPosition);

            loadPosition++;

            mHandler.postDelayed(mStatusChecker, 270);
        }
    };

   /* private void displayLoadingPosition(int loadPosition) {
        int emphasizedViewPos = loadPosition % 4;

        splashFirst.setImageResource(R.drawable.splash_logo_one_light);
        splashTwo.setImageResource(R.drawable.splash_logo_two_light);
        splashThree.setImageResource(R.drawable.splash_logo_three_light);
        splashFour.setImageResource(R.drawable.splash_logo_four_light);

        switch (emphasizedViewPos) {
            case 0:
                splashFirst.setImageResource(R.drawable.splash_logo_one);
                break;

            case 1:
                splashTwo.setImageResource(R.drawable.splash_logo_two);
                break;

            case 2:
                splashThree.setImageResource(R.drawable.splash_logo_three);
                break;

            case 3:
                splashFour.setImageResource(R.drawable.splash_logo_four);
                break;
        }
    }
*/
}