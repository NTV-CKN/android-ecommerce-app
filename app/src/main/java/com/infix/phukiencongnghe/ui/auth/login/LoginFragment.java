package com.infix.phukiencongnghe.ui.auth.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.repository.auth.AuthRepositoryImpl;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.databinding.FragmentLoginBinding;
import com.infix.phukiencongnghe.ui.auth.AuthViewModel;
import com.infix.phukiencongnghe.ui.auth.register.RegisterFragment;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.utils.AppUtils;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.KeyboardUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog();
        initAuthViewModel();
        setEvent();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        authViewModel.resetStates();
        binding = null;
        loadingDialog = null;
    }

    private void initAuthViewModel() {
        AuthViewModel.Factory factory = new AuthViewModel.Factory(InjectUtils.createAuthRepository(requireContext()));
        authViewModel = new ViewModelProvider(requireActivity(), factory).get(AuthViewModel.class);

        //observe notify msg
        authViewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg == null) return;
            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    msg,
                    Snackbar.LENGTH_SHORT
            );
        });

        //observe is loading
        authViewModel.isLoading.observe(getViewLifecycleOwner(), bool -> {
            if(bool == null) return;
            if(bool)
                loadingDialog.show(requireActivity().getSupportFragmentManager(), null);
            else
                loadingDialog.dismiss();
        });
    }

    private void setEvent() {
        //login
        binding.btnLoginLogin.setOnClickListener(v -> handleLogin());
        //forgot password
        binding.tvForgotPasswordLogin.setOnClickListener(v-> handleForgotPassword());
        //create new account
        binding.tvRegisterLinkLogin.setOnClickListener(v -> goToRegisterFragment());
    }

    private void handleLogin() {
        String email = binding.edtEmailLogin.getText().toString().trim();
        String password = binding.edtPasswordLogin.getText().toString();
        boolean dataReady = true;

        if (email.isEmpty() || password.isEmpty()) {
            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    getString(R.string.snackbar_data_must_not_empty),
                    Snackbar.LENGTH_SHORT);
            dataReady = false;
        }

        if (!AppUtils.isStrongPassword(password)) {
            binding.tilPasswordLogin.setError(getString(R.string.text_password_illegal));
            dataReady = false;
        } else {
            binding.tilPasswordLogin.setError(null);
        }

        if (!AppUtils.isFormatEmail(email)) {
            binding.tilEmailLogin.setError(getString(R.string.text_email_illegal));
            dataReady = false;
        } else {
            binding.tilEmailLogin.setError(null);
        }

        if (!dataReady) return;

        KeyboardUtils.hideKeyboardFromView(binding.getRoot());
        authViewModel.loginLocal(email, password);
    }

    private void goToRegisterFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_auth, new RegisterFragment())
                .commit();
    }

    private void handleForgotPassword() {

    }
}