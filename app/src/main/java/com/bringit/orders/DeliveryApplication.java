package com.bringit.orders;


import android.app.Application;
import android.os.Build;
import android.webkit.CookieSyncManager;

import com.bringit.orders.utils.SharedPrefs;

public class DeliveryApplication extends Application {
    private static final String SET_COOKIE_KEY = "Set-Cookie";
    private static final String COOKIE_KEY = "Cookie";
    private static final String SESSION_COOKIE = "APIKey";

    private static DeliveryApplication _instance;

    public static DeliveryApplication get() {
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
//        FirebaseApp.initializeApp(getApplicationContext());
//        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        SharedPrefs.loadPrefs(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(this);
        }
    }
}

