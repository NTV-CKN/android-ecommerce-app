package com.infix.phukiencongnghe;

import android.app.Application;

import com.google.android.libraries.places.api.Places;
import com.infix.phukiencongnghe.utils.ApiClient;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ApiClient.initAccessRefreshToken(getApplicationContext());
        if (!Places.isInitialized()) {
            Places.initialize(
                    getApplicationContext(),
                    BuildConfig.MAPS_KEY
            );
        }
    }
}
