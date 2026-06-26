package com.infix.phukiencongnghe.data.repository.user_manage.address;

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
}
