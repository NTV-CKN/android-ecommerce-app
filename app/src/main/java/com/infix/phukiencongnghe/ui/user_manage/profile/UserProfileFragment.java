package com.infix.phukiencongnghe.ui.user_manage.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.utils.SharePrefUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

public class UserProfileFragment extends Fragment {
    private UserProfileViewModel viewModel;
    private EditText edtFullname, edtEmail, edtAccountType;
    private Button btnSaveProfile;
    private String userToken;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        edtFullname = view.findViewById(R.id.edtFullname);
        edtEmail = view.findViewById(R.id.edtEmail);
        edtAccountType = view.findViewById(R.id.edtAccountType);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);

        viewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);
        String[] tokens = SharePrefUtils.getAccessRefreshTokenFromPrefFile("AppPrefs",
                "ACCESS_TOKEN",
                "REFRESH_TOKEN",
                requireContext());

        if (tokens != null && tokens[0] != null) {
            userToken = "Bearer " + tokens[0];
            viewModel.loadUserProfile(userToken);
        } else {
            Intent intent = new Intent(requireActivity(), com.infix.phukiencongnghe.ui.auth.AuthActivity.class);
            startActivity(intent);
        }

        observeViewModel();

        viewModel.loadUserProfile(userToken);

        btnSaveProfile.setOnClickListener(v -> {
            String newName = edtFullname.getText().toString().trim();
            if (!newName.isEmpty()) {
                viewModel.updateFullName(userToken, newName);
            } else {
                SnackbarUtils.showBaseSnackbar(
                        requireView(),
                        "Vui lòng nhập họ và tên của bạn",
                        Snackbar.LENGTH_SHORT
                );
            }
        });
    }

    private void observeViewModel() {
        viewModel.userProfile.observe(getViewLifecycleOwner(), profile -> {
            if (profile != null) {
                edtFullname.setText(profile.getFullName());
                edtEmail.setText(profile.getEmail());
                edtAccountType.setText(profile.getTypeAccount());
            }
        });
        viewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                SnackbarUtils.showBaseSnackbar(requireView(), msg, Snackbar.LENGTH_SHORT);
            }
        });
        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            btnSaveProfile.setEnabled(!isLoading);
            if (isLoading) {
                btnSaveProfile.setText("Đang lưu...");
            } else {
                btnSaveProfile.setText("Lưu thay đổi");
            }
        });
    }
}
