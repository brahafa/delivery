package com.bringit.orders.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bringit.orders.R;
import com.wang.avi.AVLoadingIndicatorView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    private AVLoadingIndicatorView loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loader=(AVLoadingIndicatorView)findViewById(R.id.LoadingIndicatorView);
        loader.smoothToShow();
        Handler handler = new Handler();
        handler.postDelayed(() -> openMainActivity(), 3000);   //5 seconds

        //clearCart();
    }

    private void openMainActivity(){
        Intent i = new Intent(SplashActivity.this, MainActivity.class);

        startActivity(i);
        finish();
    }
}
