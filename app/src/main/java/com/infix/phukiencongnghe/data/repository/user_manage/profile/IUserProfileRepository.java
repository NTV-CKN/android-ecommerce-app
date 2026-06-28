package com.infix.phukiencongnghe.data.repository.user_manage.profile;

import com.infix.phukiencongnghe.data.dto.request.UpdateUserDTO;
import com.infix.phukiencongnghe.data.dto.response.UserProfileDTO;

import java.util.Map;

import retrofit2.Call;

public interface IUserProfileRepository {
    Call<UserProfileDTO> getUserProfile(String token);
    Call<Map<String, Object>> updateFullName(String token, UpdateUserDTO req);
}
