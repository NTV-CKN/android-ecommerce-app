package com.infix.phukiencongnghe.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.infix.phukiencongnghe.R;

public class CartFragment extends Fragment {
    private ImageView imgView_back;

    public CartFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        imgView_back = view.findViewById(R.id.btnBack);
        imgView_back.setOnClickListener(view1 -> {
            requireActivity().finish();
        });
        return view;
    }
}
