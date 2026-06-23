package com.infix.phukiencongnghe.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class SharePrefUtils {
    public static void saveStringToPrefFile(String nameFile, String keyName, String value, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameFile, Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(keyName, value)
                .apply();
    }

    public static void saveAccessTokenAndRefreshTokenToPrefFile(String nameFile, String accessKey, String refreshKey
            , String accessToken, String refreshToken, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameFile, Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(accessKey, accessToken)
                .putString(refreshKey, refreshToken)
                .apply();
    }
}
