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
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.ui.auth.AuthActivity;
import com.infix.phukiencongnghe.ui.cart.CartActivity;
import com.infix.phukiencongnghe.ui.user_manage.UserManagerActivity;
import com.infix.phukiencongnghe.utils.SharePrefUtils;

public class HeaderFragment extends Fragment {
    private FragmentHeaderBinding binding;
    private ImageView imgView_user_header_fragment, imgView_search_header_fragment, imgView_cart_header_fragment;
    private TextView txtView_user_header_fragment;

    public HeaderFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        MainViewModel mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.cartBadgetCount.observe(getViewLifecycleOwner(),totalCount ->{
            if(totalCount==null){
                return;
            }
            if(binding.tvCartBadge != null){
                binding.tvCartBadge.setText(String.valueOf(totalCount));
                binding.tvCartBadge.setVisibility(View.VISIBLE);
            }
        });
        imgView_user_header_fragment = view.findViewById(R.id.imgAvatar);
        txtView_user_header_fragment = view.findViewById(R.id.txtWelcome);
        imgView_search_header_fragment = view.findViewById(R.id.btnSearch);
        imgView_cart_header_fragment = view.findViewById(R.id.btnCart);

        imgView_cart_header_fragment.setOnClickListener(view1 -> {
            String[] tokens = com.infix.phukiencongnghe.utils.SharePrefUtils.getAccessRefreshTokenFromPrefFile(
                    com.infix.phukiencongnghe.ui.auth.AuthActivity.USER_AUTH_FILE,
                    com.infix.phukiencongnghe.ui.auth.AuthActivity.KEY_ACCESS_TOKEN,
                    com.infix.phukiencongnghe.ui.auth.AuthActivity.KEY_REFRESH_TOKEN,
                    requireContext()
            );

            if (tokens[0] == null || tokens[0].isEmpty() && tokens[1] == null || tokens[1].isEmpty()) {
                com.infix.phukiencongnghe.utils.SnackbarUtils.showBaseSnackbar(
                        view,
                        "Vui lòng đăng nhập để xem giỏ hàng!",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                );

                Intent intent = new Intent(requireActivity(), com.infix.phukiencongnghe.ui.auth.AuthActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(requireActivity(), CartActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEvents();
    }

    private void setEvents() {
        //imgAvatar
        imgView_user_header_fragment.setOnClickListener(v -> handleImageAvatarClick());
    }

    private void handleImageAvatarClick() {
        boolean isLogin = SharePrefUtils.isLogin(
                AuthActivity.USER_AUTH_FILE,
                AuthActivity.KEY_ACCESS_TOKEN,
                AuthActivity.KEY_REFRESH_TOKEN,
                requireContext()
        );

        Intent intent;
        if(isLogin) {
            intent = new Intent(requireContext(), UserManagerActivity.class);
        }else {
            intent = new Intent(requireContext(), AuthActivity.class);
        }
        startActivity(intent);
    }
}
