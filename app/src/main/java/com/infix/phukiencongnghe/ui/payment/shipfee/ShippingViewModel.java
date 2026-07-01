package com.infix.phukiencongnghe.ui.payment.shipfee;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.response.ShipFeeByAddressDTO;
import com.infix.phukiencongnghe.data.repository.ship_fee.IShipFeeByAddressRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingViewModel extends ViewModel {
    private final IShipFeeByAddressRepository repository;
    private final MutableLiveData<ShipFeeByAddressDTO> _shipFeeData = new MutableLiveData<>();
    public final LiveData<ShipFeeByAddressDTO> shipFeeData = _shipFeeData;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public final LiveData<String> notifyMsg = _notifyMsg;

    public ShippingViewModel(IShipFeeByAddressRepository repository) {
        this.repository = repository;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final IShipFeeByAddressRepository repository;

        public Factory(IShipFeeByAddressRepository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ShippingViewModel.class)) {
                return (T) new ShippingViewModel(repository);
            }
            throw new IllegalArgumentException("Model class illegal");
        }
    }
    public void calculateShippingFee(String provinceCity){
        if(provinceCity==null||provinceCity.isEmpty())return;
        repository.getShipFeeByProvinceCity(provinceCity).enqueue(new Callback<ShipFeeByAddressDTO>() {
            @Override
            public void onResponse(Call<ShipFeeByAddressDTO> call, Response<ShipFeeByAddressDTO> response) {
                if(response.isSuccessful() && response.body() !=null){
                    _shipFeeData.setValue(response.body());
                }else{
                    _notifyMsg.setValue("Không tìm thấy mức phí ship phù hợp cho khu vực này.");
                }
            }

            @Override
            public void onFailure(Call<ShipFeeByAddressDTO> call, Throwable throwable) {
                _notifyMsg.setValue(throwable.getMessage());
            }
        });
    }
}

