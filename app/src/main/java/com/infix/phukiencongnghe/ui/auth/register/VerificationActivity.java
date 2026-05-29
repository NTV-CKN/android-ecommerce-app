package com.infix.phukiencongnghe.ui.auth.register;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.repository.auth.AuthRepositoryImpl;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.databinding.ActivityAuthBinding;
import com.infix.phukiencongnghe.databinding.ActivityVerificationBinding;
import com.infix.phukiencongnghe.ui.auth.AuthViewModel;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

public class VerificationActivity extends AppCompatActivity {
    private ActivityVerificationBinding binding;
    private AuthViewModel authViewModel;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        loadingDialog = new LoadingDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initAuthViewModel();
        setEvent();
    }

    private void setEvent() {
        //back
        binding.btnBackVerifyMail.setOnClickListener(v -> finish());

        //verify
        binding.btnVerifyMail.setOnClickListener(v -> {
            Uri uri = getIntent().getData();
            if (uri != null)
                authViewModel.verifyMail(uri.getQueryParameter("email"));
        });
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

        //observe is loading
        authViewModel.isLoading.observe(this, bool -> {
            if (bool == null) return;
            if (bool)
                loadingDialog.show(getSupportFragmentManager(), null);
            else
                loadingDialog.dismiss();
        });
    }

}