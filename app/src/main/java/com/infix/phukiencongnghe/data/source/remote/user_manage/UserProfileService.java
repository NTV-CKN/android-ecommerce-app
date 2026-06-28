package com.infix.phukiencongnghe.data.source.remote.user_manage;

import com.infix.phukiencongnghe.data.dto.request.UpdateUserDTO;
import com.infix.phukiencongnghe.data.dto.response.UserProfileDTO;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface UserProfileService {

    @GET("/api/v1/user-profile")
    Call<UserProfileDTO> getUserProfile(@Header("Authorization") String token);

    @PUT("/api/v1/user-profile/update")
    Call<Map<String, Object>> updateFullName(@Body UpdateUserDTO req);

}
