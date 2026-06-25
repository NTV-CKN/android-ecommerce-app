package com.infix.phukiencongnghe.utils;

import android.content.Context;

import com.infix.phukiencongnghe.data.repository.auth.AuthRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.auth.IAuthRepository;
import com.infix.phukiencongnghe.data.source.local.AppDatabase;
import com.infix.phukiencongnghe.data.source.local.source.user.UserLocalDataSourceImpl;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.data.source.remote.auth.AuthRemoteDataSourceImpl;

public class InjectUtils {
    public static IAuthRepository createAuthRepository(Context context) {
        return new AuthRepositoryImpl(
                RetrofitHelper.getAuthService(),
                new UserLocalDataSourceImpl(
                        AppDatabase.getInstance(context).userDAO()
                ),
                new AuthRemoteDataSourceImpl()
        );
    }
}