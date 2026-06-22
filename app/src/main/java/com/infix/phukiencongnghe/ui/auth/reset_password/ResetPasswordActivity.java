package com.infix.phukiencongnghe.ui.auth.reset_password;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.repository.auth.AuthRepositoryImpl;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.databinding.ActivityResetPasswordBinding;
import com.infix.phukiencongnghe.ui.auth.AuthViewModel;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.utils.AppUtils;
import com.infix.phukiencongnghe.utils.KeyboardUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

public class ResetPasswordActivity extends AppCompatActivity {
    private ActivityResetPasswordBinding binding;
    private AuthViewModel authViewModel;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadingDialog = new LoadingDialog();
        setEvent();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initAuthViewModel();
    }

    @Override
    public void onStop() {
        super.onStop();
        authViewModel.resetStates();
        binding = null;
        loadingDialog = null;
    }

    private void initAuthViewModel() {
        AuthViewModel.Factory factory = new AuthViewModel.Factory(
                new AuthRepositoryImpl(RetrofitHelper.getAuthService())
        );
        authViewModel = new ViewModelProvider(this, factory).get(AuthViewModel.class);

        //observe notify msg
        authViewModel.notifyMsg.observe(this, msg -> {
            if (msg == null) return;
            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    msg,
                    Snackbar.LENGTH_SHORT
            );
        });

        //is loading dialog
        authViewModel.isLoading.observe(this, isLoad ->{
            if(isLoad == null) return;
            if(isLoad)
                loadingDialog.show(getSupportFragmentManager(), null);
            else
                loadingDialog.dismiss();
        });
    }

    private void setEvent() {
        //cancel
        binding.btnCancelResetPassword.setOnClickListener(v -> finish());

        //reset password
        binding.btnChangePasswordResetPassword.setOnClickListener(v -> handleResetPassword());
    }

    private void handleResetPassword() {
        String password = binding.edtPasswordResetPassword.getText().toString();
        String repeatPassword = binding.edtRepeatPasswordResetPassword.getText().toString();

        if (password.isEmpty() || repeatPassword.isEmpty()) {
            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    getString(R.string.snackbar_data_must_not_empty),
                    Snackbar.LENGTH_SHORT
            );
            return;
        }

        if (!password.equals(repeatPassword)) {
            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    getString(R.string.text_repeat_password_not_match),
                    Snackbar.LENGTH_SHORT
            );
            return;
        }

        if (!AppUtils.isStrongPassword(repeatPassword)) {
            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    getString(R.string.text_password_illegal),
                    Snackbar.LENGTH_SHORT
            );
            return;
        }

        Uri uri = getIntent().getData();
        if (uri == null) {
            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    getString(R.string.text_data_null),
                    Snackbar.LENGTH_SHORT
            );
            return;
        }
        String email = uri.getQueryParameter("email");
        String token = uri.getQueryParameter("token");
        KeyboardUtils.hideKeyboardFromView(binding.getRoot());
        authViewModel.resetPassword(email, password, token);
    }
}