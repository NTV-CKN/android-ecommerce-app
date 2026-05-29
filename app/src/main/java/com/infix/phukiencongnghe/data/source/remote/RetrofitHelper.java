package com.infix.phukiencongnghe.data.source.remote;

import com.infix.phukiencongnghe.data.source.remote.auth.AuthService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private RetrofitHelper(){}

    public static AuthService getAuthService() {
        return generateRetrofit().create(AuthService.class);
    }

    private static Retrofit generateRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
