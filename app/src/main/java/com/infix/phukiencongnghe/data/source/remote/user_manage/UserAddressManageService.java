package com.infix.phukiencongnghe.data.source.remote.user_manage;

import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserAddressManageService {
    @GET("/api/v1/user-manage-address/view-addresses")
    Call<List<UserAddressDTO>> getUserAddressByUserId(@Query("userId") Integer userId);
}
