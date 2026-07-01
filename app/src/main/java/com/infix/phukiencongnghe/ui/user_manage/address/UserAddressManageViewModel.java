package com.infix.phukiencongnghe.ui.user_manage.address;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.infix.phukiencongnghe.data.dto.response.ExceptionResponseDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;
import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;
import com.infix.phukiencongnghe.data.repository.user_manage.address.IUserAddressManageRepository;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAddressManageViewModel extends ViewModel {
    private final IUserAddressManageRepository userAddressManageRepository;
    private final MutableLiveData<List<UserAddressDTO>> _userAddresses = new MutableLiveData<>();
    public final LiveData<List<UserAddressDTO>> userAddresses = _userAddresses;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;


    public UserAddressManageViewModel(IUserAddressManageRepository userAddressManageRepository) {
        this.userAddressManageRepository = userAddressManageRepository;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private IUserAddressManageRepository userAddressManageRepository;

        public Factory(IUserAddressManageRepository userAddressManageRepository) {
            this.userAddressManageRepository = userAddressManageRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(UserAddressManageViewModel.class))
                return (T) new UserAddressManageViewModel(userAddressManageRepository);
            throw new IllegalArgumentException("Model class illegal");
        }
    }

    public void removeUserAddress(UserAddressDTO userAddressDTO) {
        _isLoading.setValue(true);
        userAddressManageRepository.removeUserAddress(userAddressDTO).enqueue(new Callback<SuccessBasicDTO>() {
            @Override
            public void onResponse(Call<SuccessBasicDTO> call, Response<SuccessBasicDTO> response) {
                if (response.isSuccessful()) {
                    SuccessBasicDTO succ = response.body();
                    if(succ != null){
                        _isLoading.setValue(false);
                        _notifyMsg.setValue(succ.getMessage());
                        getUserAddresses();
                        return;
                    }else {
                        _notifyMsg.setValue("Body null");
                    }
                } else {
                    Gson gson = new Gson();
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody == null) {
                        _notifyMsg.setValue("Không thể hiểu lỗi");
                        return;
                    }
                    ExceptionResponseDTO exc = null;
                    try {
                        exc = gson.fromJson(responseBody.string(), ExceptionResponseDTO.class);
                        if(exc == null) return;
                        _notifyMsg.setValue(exc.getMessage());
                    } catch (IOException e) {
                        _notifyMsg.setValue(e.getMessage());
                    }
                }
                _isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<SuccessBasicDTO> call, Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue(throwable.getMessage());
            }
        });
    }

    public void getUserAddresses() {
        Call<List<UserAddressDTO>> usCall = userAddressManageRepository.getUserAddresses();
        _isLoading.setValue(true);
        usCall.enqueue(new Callback<List<UserAddressDTO>>() {
            @Override
            public void onResponse(Call<List<UserAddressDTO>> call, Response<List<UserAddressDTO>> response) {
                if (response.isSuccessful()) {
                    List<UserAddressDTO> succ = response.body();
                    _userAddresses.setValue(succ != null ? succ : new java.util.ArrayList<>());
                } else {
                    Gson gson = new Gson();
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody == null) {
                        _notifyMsg.setValue("Không thể hiểu lỗi");
                        return;
                    }
                    ExceptionResponseDTO exc = null;
                    try {
                        exc = gson.fromJson(responseBody.string(), ExceptionResponseDTO.class);
                        if(exc == null) return;
                        _notifyMsg.setValue(exc.getMessage());
                    } catch (IOException e) {
                        _notifyMsg.setValue(e.getMessage());
                    }
                }
                _isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<UserAddressDTO>> call, Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue(throwable.getMessage());
            }
        });
    }

    public void resetStates() {
        _userAddresses.setValue(null);
        _notifyMsg.setValue(null);
        _isLoading.setValue(null);
    }
}
