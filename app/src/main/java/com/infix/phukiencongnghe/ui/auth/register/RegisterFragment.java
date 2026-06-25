package com.infix.phukiencongnghe.ui.auth.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.repository.auth.AuthRepositoryImpl;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.databinding.FragmentRegisterBinding;
import com.infix.phukiencongnghe.ui.auth.AuthViewModel;
import com.infix.phukiencongnghe.ui.auth.login.LoginFragment;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.utils.AppUtils;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.KeyboardUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;


public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private AuthViewModel authViewModel;
    private LoadingDialog loadingDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(
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
        //register
        binding.btnRegisterRegister.setOnClickListener(v -> handleRegister());

        //nav to login
        binding.btnNavLoginRegister.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fcv_auth, new LoginFragment())
                    .commit();
        });
    }

    private void handleRegister() {
        String email = binding.edtEmailRegister.getText().toString();
        String password = binding.edtPasswordRegister.getText().toString();
        String repeatPassword = binding.edtRepeatPasswordRegister.getText().toString();

        boolean dataReady = true;

        if (email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {
            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    getString(R.string.snackbar_data_must_not_empty),
                    Snackbar.LENGTH_SHORT);
            dataReady = false;
        }
        boolean illegalFormatPassword = false, illegalFormatRepeatPass = false;
        if (!AppUtils.isStrongPassword(password)) {
            illegalFormatPassword = true;
            binding.tilPasswordRegister.setError(getString(R.string.text_password_illegal));
        } else {
            binding.tilPasswordRegister.setError(null);
        }

        if (!AppUtils.isStrongPassword(repeatPassword)) {
            illegalFormatRepeatPass = true;
            binding.tilRepeatPasswordRegister.setError(getString(R.string.text_password_illegal));
        } else {
            binding.tilRepeatPasswordRegister.setError(null);
        }

        if (illegalFormatPassword || illegalFormatRepeatPass)
            dataReady = false;
        if (dataReady)
            if (!password.equals(repeatPassword)) {
                binding.tilRepeatPasswordRegister.setError(getString(R.string.text_repeat_password_not_match));
                binding.tilPasswordRegister.setError(getString(R.string.text_repeat_password_not_match));
                dataReady = false;
            } else {
                binding.tilRepeatPasswordRegister.setError(null);
                binding.tilPasswordRegister.setError(null);
            }

        if (!AppUtils.isFormatEmail(email)) {
            binding.tilEmailRegister.setError(getString(R.string.text_email_illegal));
            dataReady = false;
        } else
            binding.tilEmailRegister.setError(null);

        if (!dataReady) return;
        KeyboardUtils.hideKeyboardFromView(binding.getRoot());
        authViewModel.registerUser(email, password);
    }
}