package com.infix.phukiencongnghe.data.source.remote.auth;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.infix.phukiencongnghe.common.OnLoginGoogleListener;
import com.infix.phukiencongnghe.data.dto.request.UserLoginGoogleDTO;

public class AuthRemoteDataSourceImpl implements IAuthRemoteDataSource {
    private final FirebaseAuth auth;

    public AuthRemoteDataSourceImpl() {
        this.auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onLoginGoogle(String idToken, OnLoginGoogleListener loginGoogleListener) {
        if (idToken == null || idToken.isEmpty()) {
            if (loginGoogleListener != null) {
                loginGoogleListener.onLoginFailure("Google ID Token bị rỗng hoặc không hợp lệ.");
            }
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        auth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();

                        if (user != null && loginGoogleListener != null) {
                            String urlPhoto = user.getPhotoUrl() == null ? null : user.getPhotoUrl().toString();

                            loginGoogleListener.onRevoke(new UserLoginGoogleDTO(
                                    user.getDisplayName(),
                                    urlPhoto,
                                    user.getEmail()
                            ));
                        }
                    } else {
                        if (loginGoogleListener != null) {
                            String errorMessage = task.getException() != null
                                    ? task.getException().getMessage()
                                    : "Lỗi xác thực chứng chỉ với Server Firebase.";
                            loginGoogleListener.onLoginFailure(errorMessage);
                        }
                    }
                });
    }
}
