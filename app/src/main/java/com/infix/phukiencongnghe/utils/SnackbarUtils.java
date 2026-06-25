package com.infix.phukiencongnghe.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.common.OnCallbackListener;

public class SnackbarUtils {
    public static void showBaseSnackbar(View view, String msg, int time) {
        Snackbar.make(
                view,
                msg,
                time
        ).show();
    }

    public static void showSnackbarWithAction(View view, String msg, int time, OnCallbackListener onCallbackListener) {
        Snackbar.make(
                view,
                msg,
                time
        )
                .setAction(view.getContext().getString(R.string.txt_confirm), v-> onCallbackListener.onRevoke())
                .show();
    }
}
