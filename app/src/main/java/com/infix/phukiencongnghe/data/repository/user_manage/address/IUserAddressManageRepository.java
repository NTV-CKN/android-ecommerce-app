package com.infix.phukiencongnghe.data.repository.user_manage.address;

import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;

import java.util.List;

import retrofit2.Call;

public interface IUserAddressManageRepository {
    Call<List<UserAddressDTO>> getUserAddressByUserId(Integer userId);
}
