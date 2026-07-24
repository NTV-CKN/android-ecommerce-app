package com.infix.phukiencongnghe.ui.auth.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.common.OnLoginGoogleListener;
import com.infix.phukiencongnghe.data.dto.request.UserLoginGoogleDTO;
import com.infix.phukiencongnghe.databinding.FragmentLoginBinding;
import com.infix.phukiencongnghe.ui.auth.AuthViewModel;
import com.infix.phukiencongnghe.ui.auth.register.RegisterFragment;
import com.infix.phukiencongnghe.ui.auth.reset_password.ForgotPasswordFragment;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.utils.AppUtils;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.KeyboardUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private AuthViewModel authViewModel;
    private LoadingDialog loadingDialog;
    private ActivityResultLauncher<Intent> googleSignInLauncher;
    private GoogleSignInClient googleSignInClient;

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
        initGoogleSignInLauncher();
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
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

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
            try {
                if(bool)
                    loadingDialog.show(requireActivity().getSupportFragmentManager(), null);
                else
                    loadingDialog.dismiss();
            } catch (Exception e) {
                Log.d("LoginFragment", e.getMessage());
            }
        });
    }

    private void setEvent() {
        //login
        binding.btnLoginLogin.setOnClickListener(v -> handleLogin());
        //forgot password
        binding.tvForgotPasswordLogin.setOnClickListener(v-> handleForgotPassword());
        //create new account
        binding.tvRegisterLinkLogin.setOnClickListener(v -> goToRegisterFragment());
        //login by google
        binding.btnLoginGoogleLogin.setOnClickListener(v -> handleGoogleLogin());
    }

    private void handleGoogleLogin() {
        //tiến hành tạo Intent chuyên dụng để mở bottom sheet cho người dùng chọn 1 trong các tài khoản đã
        //có (Trước đó phải sign out) để tiến hành lấy token id đi xác thực
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //tạo google đăng nhập client
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

        googleSignInClient.signOut().addOnCompleteListener(requireActivity(), task -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            googleSignInLauncher.launch(signInIntent);
        });
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
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_auth, new ForgotPasswordFragment())
                .commit();
    }

    //Khởi tạo Activity launcher để tiến hành gọi bottom sheet chọn tài khoản và lấy token id tạo chứng thực
    private void initGoogleSignInLauncher() {
        googleSignInLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            GoogleSignInAccount account = task.getResult(ApiException.class);

                            if (account != null) {
                                String idToken = account.getIdToken();
                                Log.d("GoogleAuth", "ID Token thu được thành công: " + idToken);

                                authViewModel.loginWithGoogle(idToken, new OnLoginGoogleListener() {
                                    @Override
                                    //nếu chứng thực thành công thì gọi API server để lưu hoặc đi đăng nhập
                                    public void onRevoke(UserLoginGoogleDTO userLoginGoogleDTO) {
                                        authViewModel.loginGoogle(userLoginGoogleDTO);
                                    }

                                    @Override
                                    public void onLoginFailure(String s) {
                                        SnackbarUtils.showBaseSnackbar(
                                                binding.getRoot(),
                                                s,
                                                Snackbar.LENGTH_SHORT
                                        );
                                    }
                                });
                            }
                        } catch (ApiException e) {
                            Log.e("GoogleAuth", "Google Sign In thất bại mã lỗi: " + e.getStatusCode());
                            SnackbarUtils.showBaseSnackbar(binding.getRoot(), "Đăng nhập Google thất bại.", Snackbar.LENGTH_SHORT);
                        }
                    }
                }
        );
    }
}