package com.infix.phukiencongnghe.ui.infoshop;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infix.phukiencongnghe.R;

public class InfoShopFragment extends Fragment {

    public InfoShopFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(
                R.layout.fragment_info_shop,
                container,
                false
        );
    }
}