package com.infix.phukiencongnghe.data.repository.user_manage.profile;

import com.infix.phukiencongnghe.data.dto.request.UpdateUserDTO;
import com.infix.phukiencongnghe.data.dto.response.UserProfileDTO;

import retrofit2.Call;

public interface IUserProfileRepository {
    Call<UserProfileDTO> getUserProfile(String token);
    Call<String> updateFullName(String token, UpdateUserDTO req);
}
