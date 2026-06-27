package com.infix.phukiencongnghe.data.repository.ship_fee;

import com.infix.phukiencongnghe.data.dto.response.ShipFeeByAddressDTO;

import java.util.List;

import retrofit2.Call;

public interface IShipFeeByAddressRepository {
    Call<List<ShipFeeByAddressDTO>> getShipFeeByAddresses();
    Call<ShipFeeByAddressDTO> getShipFeeByProvinceCity(String provinceCity);
}
