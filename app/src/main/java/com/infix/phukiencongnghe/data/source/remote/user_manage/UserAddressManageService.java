package com.infix.phukiencongnghe.data.source.remote.user_manage;

import com.infix.phukiencongnghe.data.dto.request.AddUserAddressDTO;
import com.infix.phukiencongnghe.data.dto.request.UpdateUserAddressDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;
import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface UserAddressManageService {
    @GET("/api/v1/user-manage-address/view-addresses")
    Call<List<UserAddressDTO>> getUserAddresses();

    @POST("/api/v1/user-manage-address/add-address")
    Call<SuccessBasicDTO> addUserAddress(@Body AddUserAddressDTO addUserAddressDTO);

    @POST("/api/v1/user-manage-address/update-address")
    Call<SuccessBasicDTO> updateUserAddress(@Body UpdateUserAddressDTO updateUserAddressDTO);
}
