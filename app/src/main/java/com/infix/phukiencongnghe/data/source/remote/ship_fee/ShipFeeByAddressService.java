package com.infix.phukiencongnghe.data.source.remote.ship_fee;

import com.infix.phukiencongnghe.data.dto.response.ShipFeeByAddressDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ShipFeeByAddressService {
    @GET("/api/v1/ship-fee-address/view-ship-fee-by-address")
    Call<List<ShipFeeByAddressDTO>> getShipFeeByAddresses();

    @GET("/api/v1/ship-fee-address/get-ship-fee-by-province-city")
    Call<ShipFeeByAddressDTO> getShipFeeByProvinceCity(@Query("provinceCity") String provinceCity);
}
