package com.infix.phukiencongnghe.common;

import com.infix.phukiencongnghe.data.dto.request.UserLoginGoogleDTO;

public interface OnLoginGoogleListener {
    void onRevoke(UserLoginGoogleDTO userLoginGoogleDTO);

    void onLoginFailure(String s);
}
