package com.infix.phukiencongnghe.data.repository.user_manage.profile;

import com.infix.phukiencongnghe.data.dto.request.UpdateUserDTO;
import com.infix.phukiencongnghe.data.dto.response.UserProfileDTO;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.data.source.remote.user_manage.UserProfileService;

import retrofit2.Call;

public class UserProfileRepositoryImpl implements IUserProfileRepository {

    UserProfileService profileService;

    public UserProfileRepositoryImpl() {
        this.profileService = RetrofitHelper.getProfileService();
    }

    @Override
    public Call<UserProfileDTO> getUserProfile(String token) {
        return profileService.getUserProfile(token);
    }

    @Override
    public Call<String> updateFullName(String token, UpdateUserDTO req) {
        return profileService.updateFullName(token, req);
    }
}
