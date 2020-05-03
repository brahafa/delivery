package com.bringit.orders;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.wang.avi.AVLoadingIndicatorView;

public class SplashActivity extends AppCompatActivity {
    private AVLoadingIndicatorView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loader=(AVLoadingIndicatorView)findViewById(R.id.LoadingIndicatorView);
        loader.smoothToShow();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                openMainActivity();

            }
        }, 3000);   //5 seconds

        //clearCart();
    }

    private void openMainActivity(){
        Intent i = new Intent(SplashActivity.this, MainActivity.class);

        startActivity(i);
        finish();
    }
}
