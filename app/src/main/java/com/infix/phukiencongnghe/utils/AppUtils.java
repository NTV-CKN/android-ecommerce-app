package com.infix.phukiencongnghe.utils;

import android.content.Context;
import android.content.Intent;

public class AppUtils {

    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d|.*[@#$%^&+=!]).{8,}$";
    private static final String EMAIL_REGEX =
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static boolean isStrongPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        return password.matches(PASSWORD_REGEX);
    }

    public static boolean isFormatEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        return email.matches(EMAIL_REGEX);
    }

    public static void startNewTaskWithClearStack(Context context, Class<?> targetActivityClass) {
        Intent intent = new Intent(context, targetActivityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        context.startActivity(intent);
    }
}