package com.infix.phukiencongnghe.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.List;

public class SharePrefUtils {
    public static void saveStringToPrefFile(String nameFile, String keyName, String value, Context context) {
        if(context == null) return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameFile, Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(keyName, value)
                .apply();
    }

    public static void saveAccessTokenAndRefreshTokenToPrefFile(String nameFile, String accessKey, String refreshKey
            , String accessToken, String refreshToken, Context context) {
        if(context == null) return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(nameFile, Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(accessKey, accessToken)
                .putString(refreshKey, refreshToken)
                .apply();
    }

    //Trả về mảng có size = 2
    //index0: access token
    //index1: refresh token
    public static String[] getAccessRefreshTokenFromPrefFile(String nameFile, String accessKey, String refreshKey, Context context) {
        if(context == null) return null;
       SharedPreferences sharedPreferences = context.getSharedPreferences(nameFile, Context.MODE_PRIVATE);
        String[] strs = new String[2];
        strs[0] = sharedPreferences.getString(accessKey, null);
        strs[1] = sharedPreferences.getString(refreshKey, null);

        return strs;
    }

    //Hàm này kiểm tra liệu access/refresh token đã có trong shared pref chưa, nếu có thì tức người dùng đã login
    public static boolean isLogin(String nameFile, String accessKey, String refreshKey, Context context) {
        String[] strs =  getAccessRefreshTokenFromPrefFile(nameFile, accessKey, refreshKey, context);
        return (strs[0] != null && !strs[0].isEmpty()) && (strs[1] != null && !strs[1].isEmpty());
    }
}
