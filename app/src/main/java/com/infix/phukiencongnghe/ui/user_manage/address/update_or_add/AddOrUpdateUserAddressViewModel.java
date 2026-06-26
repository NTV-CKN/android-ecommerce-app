package com.infix.phukiencongnghe.ui.user_manage.address.update_or_add;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;
import com.infix.phukiencongnghe.data.repository.user_manage.address.IUserAddressManageRepository;


public class AddOrUpdateUserAddressViewModel extends ViewModel {
    private IUserAddressManageRepository userAddressManageRepository;
    private boolean isUpdate;

    private final MutableLiveData<UserAddressDTO> _userAddress = new MutableLiveData<>();
    public final LiveData<UserAddressDTO> userAddress = _userAddress;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    public AddOrUpdateUserAddressViewModel(IUserAddressManageRepository userAddressManageRepository) {
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
            if (modelClass.isAssignableFrom(AddOrUpdateUserAddressViewModel.class))
                return (T) new AddOrUpdateUserAddressViewModel(userAddressManageRepository);
            throw new IllegalArgumentException("Model class illegal");
        }
    }

    public void setUserAddressState(UserAddressDTO userAddressDTO) {
        _userAddress.setValue(userAddressDTO);
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public void resetAllStates() {
        _isLoading.setValue(null);
        _userAddress.setValue(null);
        _notifyMsg.setValue(null);
    }
}
