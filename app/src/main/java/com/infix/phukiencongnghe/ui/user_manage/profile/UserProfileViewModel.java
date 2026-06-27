package com.infix.phukiencongnghe.ui.user_manage.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.request.UpdateUserDTO;
import com.infix.phukiencongnghe.data.dto.response.UserProfileDTO;
import com.infix.phukiencongnghe.data.repository.user_manage.profile.IUserProfileRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileViewModel extends ViewModel {
    private final IUserProfileRepository profileRepository;

    private final MutableLiveData<UserProfileDTO> _userProfile = new MutableLiveData<>();
    public LiveData<UserProfileDTO> userProfile = _userProfile;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    public UserProfileViewModel(IUserProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public void loadUserProfile(String token) {
        _isLoading.setValue(true);
        profileRepository.getUserProfile(token).enqueue(new Callback<UserProfileDTO>() {
            @Override
            public void onResponse(Call<UserProfileDTO> call, Response<UserProfileDTO> response) {
                _isLoading.setValue(false);
                if(response.isSuccessful() && response.body() != null) {
                    _userProfile.setValue(response.body());
                } else {
                    _notifyMsg.setValue("Không thể tải thông tin cá nhân");
                }
            }

            @Override
            public void onFailure(Call<UserProfileDTO> call, Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue("Lỗi kết nối mạng: " + throwable.getMessage());
            }
        });
    }

    public void updateFullName(String token, String newName) {
        _isLoading.setValue(true);
        UpdateUserDTO body = new UpdateUserDTO(newName);
        profileRepository.updateFullName(token, body).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful()) {
                    _notifyMsg.setValue("Cập nhật họ và tên thành công!");
                } else {
                    _notifyMsg.setValue("Lỗi khi cập nhật thông tin");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue("Lỗi kết nối mạng: " + throwable.getMessage());
            }
        });
    }
    public static class Factory implements ViewModelProvider.Factory {
        private final IUserProfileRepository profileRepository;

        public Factory(IUserProfileRepository profileRepository) {
            this.profileRepository = profileRepository;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(UserProfileViewModel.class)) {
                return (T) new UserProfileViewModel(profileRepository);
            }
            throw new IllegalArgumentException("Không tìm thấy class ViewModel hợp lệ: " + modelClass.getName());
        }
    }
}
