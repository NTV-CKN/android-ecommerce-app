package com.infix.phukiencongnghe.data.repository.user_manage.address;

import com.infix.phukiencongnghe.data.dto.request.AddUserAddressDTO;
import com.infix.phukiencongnghe.data.dto.request.UpdateUserAddressDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;
import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;
import com.infix.phukiencongnghe.data.source.remote.user_manage.UserAddressManageService;

import java.util.List;

import retrofit2.Call;

public class UserAddressManageRepositoryImpl  implements IUserAddressManageRepository{
    private UserAddressManageService userAddressManageService;

    public UserAddressManageRepositoryImpl(UserAddressManageService userAddressManageService) {
        this.userAddressManageService = userAddressManageService;
    }

    @Override
    public Call<List<UserAddressDTO>> getUserAddresses() {
        return userAddressManageService.getUserAddresses();
    }

    @Override
    public Call<SuccessBasicDTO> addUserAddress(AddUserAddressDTO addUserAddressDTO) {
        return userAddressManageService.addUserAddress(addUserAddressDTO);
    }

    @Override
    public Call<SuccessBasicDTO> updateUserAddress(UpdateUserAddressDTO updateUserAddressDTO) {
        return userAddressManageService.updateUserAddress(updateUserAddressDTO);
    }

    @Override
    public Call<SuccessBasicDTO> removeUserAddress(UserAddressDTO userAddressDTO) {
        return userAddressManageService.removeUserAddress(userAddressDTO);
    }
}
