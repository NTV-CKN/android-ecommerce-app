package com.infix.phukiencongnghe.data.repository.ship_fee;

import com.infix.phukiencongnghe.data.dto.response.ShipFeeByAddressDTO;
import com.infix.phukiencongnghe.data.source.remote.ship_fee.ShipFeeByAddressService;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class ShipFeeByAddressRepositoryImpl implements IShipFeeByAddressRepository {
    private ShipFeeByAddressService shipFeeByAddressService;

    @Inject
    public ShipFeeByAddressRepositoryImpl(ShipFeeByAddressService shipFeeByAddressService) {
        this.shipFeeByAddressService = shipFeeByAddressService;
    }

    @Override
    public Call<List<ShipFeeByAddressDTO>> getShipFeeByAddresses() {
        return shipFeeByAddressService.getShipFeeByAddresses();
    }

    @Override
    public Call<ShipFeeByAddressDTO> getShipFeeByProvinceCity(String provinceCity) {
        return shipFeeByAddressService.getShipFeeByProvinceCity(provinceCity);
    }
}
