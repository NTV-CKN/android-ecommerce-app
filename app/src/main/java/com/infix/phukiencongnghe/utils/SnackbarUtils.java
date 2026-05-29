package com.infix.phukiencongnghe.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class SnackbarUtils {
    public static void showBaseSnackbar(View view, String msg, int time) {
        Snackbar.make(
                view,
                msg,
                time
        ).show();
    }
}
