package com.infix.phukiencongnghe.data.source.remote.auth;

import com.infix.phukiencongnghe.common.OnLoginGoogleListener;

public interface IAuthRemoteDataSource {
    void onLoginGoogle(String idToken, OnLoginGoogleListener loginGoogleListener);
}
