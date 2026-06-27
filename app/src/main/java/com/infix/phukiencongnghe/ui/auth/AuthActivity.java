package com.infix.phukiencongnghe.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.common.OnCallbackListener;
import com.infix.phukiencongnghe.data.repository.auth.AuthRepositoryImpl;
import com.infix.phukiencongnghe.data.source.local.AppDatabase;
import com.infix.phukiencongnghe.data.source.local.entity.UserEntity;
import com.infix.phukiencongnghe.data.source.local.source.user.UserLocalDataSourceImpl;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.databinding.ActivityAuthBinding;
import com.infix.phukiencongnghe.ui.main.MainActivity;
import com.infix.phukiencongnghe.ui.share_viewmodel.UserEntityViewModel;
import com.infix.phukiencongnghe.utils.ApiClient;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.SharePrefUtils;

public class AuthActivity extends AppCompatActivity {
    private ActivityAuthBinding binding;
    private AuthViewModel authViewModel;
    private UserEntityViewModel userEntityViewModel;

    public static final String USER_AUTH_FILE = "com.infix.phukiencongnghe.ui.auth.USER_AUTH_FILE";
    public static final String KEY_ACCESS_TOKEN = "com.infix.phukiencongnghe.ui.auth.KEY_ACCESS_TOKEN";
    public static final String KEY_REFRESH_TOKEN = "com.infix.phukiencongnghe.ui.auth.KEY_REFRESH_TOKEN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initUserEntityViewModel();
        initAndObserveAuthViewModel();
    }

    private void initUserEntityViewModel() {
        UserEntityViewModel.Factory factory =
                new UserEntityViewModel.Factory(InjectUtils.createAuthRepository(this));

        userEntityViewModel = new ViewModelProvider(this, factory).get(UserEntityViewModel.class);
    }

    private void initAndObserveAuthViewModel() {
        AuthViewModel.Factory factory =
                new AuthViewModel.Factory(InjectUtils.createAuthRepository(this));

        authViewModel =  new ViewModelProvider(this, factory).get(AuthViewModel.class);

        //jwt
        authViewModel.jwtFromLoginDTO.observe(this, jwtFromLoginDTO -> {
            if(jwtFromLoginDTO == null) return;

            Log.d("SVU", jwtFromLoginDTO.toString());

            UserEntity user = new UserEntity();
            String fullName = jwtFromLoginDTO.getFullName() == null?"No name":jwtFromLoginDTO.getFullName();
            user.setFullName(fullName);
            user.setId(jwtFromLoginDTO.getUserId());
            user.setAvatar(jwtFromLoginDTO.getAvatar());

            OnCallbackListener onCallbackListener = () -> {
                navToMainActivity(user);
            };

            SharePrefUtils.saveAccessTokenAndRefreshTokenToPrefFile(
                    USER_AUTH_FILE,
                    KEY_ACCESS_TOKEN,
                    KEY_REFRESH_TOKEN,
                    jwtFromLoginDTO.getAccessToken(),
                    jwtFromLoginDTO.getRefreshToken(),
                    this
            );

            SharePrefUtils.saveStringToPrefFile(USER_AUTH_FILE, "KEY_FULL_NAME", user.getFullName(), this);

            //Khi đăng nhập thành công tiến hành thiết lập lại giá trị của access/refresh Token trong ApiClient
            ApiClient.setAccessTokenAndRefreshToken(
                    jwtFromLoginDTO.getAccessToken(),
                    jwtFromLoginDTO.getRefreshToken()
            );

            userEntityViewModel.insertUser(user, onCallbackListener);
        });
    }

    private void navToMainActivity(UserEntity user) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.KEY_USER_HEADER, user);
        startActivity(intent);
        finish();
    }
}