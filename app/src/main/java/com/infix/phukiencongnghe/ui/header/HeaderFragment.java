package com.infix.phukiencongnghe.ui.header;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.ui.cart.CartActivity;

public class HeaderFragment extends Fragment {

    private ImageView imgView_user_header_fragment, imgView_search_header_fragment, imgView_cart_header_fragment;
    private TextView txtView_user_header_fragment;

    public HeaderFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        imgView_user_header_fragment = view.findViewById(R.id.imgAvatar);
        txtView_user_header_fragment = view.findViewById(R.id.txtWelcome);
        imgView_search_header_fragment = view.findViewById(R.id.btnSearch);
        imgView_cart_header_fragment = view.findViewById(R.id.btnCart);

        imgView_cart_header_fragment.setOnClickListener(view1 -> {
            Intent intent = new Intent(requireActivity(), CartActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
