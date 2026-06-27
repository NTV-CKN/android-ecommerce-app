package com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.address_picker;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.infix.phukiencongnghe.data.dto.response.ExceptionResponseDTO;
import com.infix.phukiencongnghe.data.dto.response.ShipFeeByAddressDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;
import com.infix.phukiencongnghe.data.model.user_manage.address.AddressSuggestion;
import com.infix.phukiencongnghe.data.repository.ship_fee.IShipFeeByAddressRepository;
import com.infix.phukiencongnghe.data.repository.user_manage.address.IUserAddressManageRepository;
import com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.AddOrUpdateUserAddressViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Lớp này chịu trách nhiệm quản lí LatLngCurrent, nhận sự thay đổi lng/lat/address detail từ người dùng
//Đầu vào: LatLngCurrent,  isUpdate: boolean
public class AddressDeliveryPickerViewModel extends ViewModel {
    private final IShipFeeByAddressRepository shipFeeByAddressRepository;

    //Nếu là update thì đồng nghĩa người dùng đang ở giao diện chỉnh sửa, ta sẽ gắn Lat/Lng đã có và hiện gg map sdk lên
    private boolean isUpdate;
    //Khi tạo AddressDeliveryPicker thì ta gọi load data từ provinceCity của AddOrUpdateUserAddress
    private Double baseLat, baseLng;

    //Khi AddOrUpdateUserAddressFragment nhấn vào để mở ra AddressDeliveryPickerFragment
    //thì sẽ truyền vào curLat/curLng, nếu sau đó user di chuyển cam qua lại thì nó sẽ lưu cập nhật lại
    private final MutableLiveData<LatLngCurrent> _latLng = new MutableLiveData<>();
    public final LiveData<LatLngCurrent> latLng = _latLng;

    private final MutableLiveData<List<AddressSuggestion>> _addressSuggestions = new MutableLiveData<>();
    public final LiveData<List<AddressSuggestion>> addressSuggestions = _addressSuggestions;

    public AddressDeliveryPickerViewModel(IShipFeeByAddressRepository shipFeeByAddressRepository) {
        this.shipFeeByAddressRepository = shipFeeByAddressRepository;
    }

    public void searchAddress(
            String keyword,
            Consumer<List<AddressSuggestion>> callback,
            PlacesClient placesClient,
            LocationRestriction restriction
    ) {

        FindAutocompletePredictionsRequest.Builder requestBuilder =
                FindAutocompletePredictionsRequest.builder()
                        .setQuery(keyword)
                        .setCountries("VN");

        if (restriction != null) {
            requestBuilder.setLocationRestriction(restriction);
        }

        FindAutocompletePredictionsRequest request = requestBuilder.build();
        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(response -> {
                    Log.d("AddressDeliveryPickerViewModel", "Số lượng gợi ý nhận được: " + response.getAutocompletePredictions().size());
                    List<AddressSuggestion> list = new ArrayList<>();

                    for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        list.add(
                                new AddressSuggestion(
                                        prediction.getPlaceId(),
                                        prediction.getPrimaryText(null).toString(),
                                        prediction.getSecondaryText(null).toString()
                                )
                        );
                    }

                    callback.accept(list);
                })
                .addOnFailureListener(
                        e -> {
                            callback.accept(new ArrayList<>());
                            Log.e("AddressDeliveryPickerViewModel", "LỖI: " + e.getMessage());
                        }
                ).addOnCanceledListener(() -> {
                    Log.e("AddressDeliveryPickerViewModel", "HUY");

                });
    }

    public void updateAddressSuggestState(List<AddressSuggestion> addressSuggestions) {
        _addressSuggestions.setValue(addressSuggestions);
    }

    //Hàm này sẽ nhận vào placeId khi người dùng chọn vào 1 trong các item được fetch từ AddressSuggest
    //Hàm sẽ fetch ra địa điểm và trả về LatLng để zoom google map
    public void getPlaceDetail(String placeId, PlacesClient placesClient, Consumer<LatLng> callback) {
        if (placesClient == null || placeId == null || placeId.trim().isEmpty()) return;

        List<com.google.android.libraries.places.api.model.Place.Field> fields = Arrays.asList(
                com.google.android.libraries.places.api.model.Place.Field.ID,
                com.google.android.libraries.places.api.model.Place.Field.ADDRESS,
                com.google.android.libraries.places.api.model.Place.Field.LAT_LNG
        );

        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, fields);
        placesClient.fetchPlace(request)
                .addOnSuccessListener(response -> {
                    com.google.android.libraries.places.api.model.Place place = response.getPlace();
                    if (place.getLatLng() != null) {
                        callback.accept(place.getLatLng());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("AddressDeliveryPickerViewModel", "Lỗi lấy chi tiết địa điểm: " + e.getMessage());
                });
    }

    public void setLatLng(LatLngCurrent latLng) {
        _latLng.setValue(latLng);
    }

    public void initBaseLatLngProvinceCity(String provinceCity) {
        shipFeeByAddressRepository.getShipFeeByProvinceCity(provinceCity).enqueue(new Callback<ShipFeeByAddressDTO>() {
            @Override
            public void onResponse(
                    @NonNull Call<ShipFeeByAddressDTO> call,
                    @NonNull Response<ShipFeeByAddressDTO> response
            ) {
                if (response.isSuccessful()) {
                    ShipFeeByAddressDTO succ = response.body();
                    if (succ != null) {
                        baseLat = succ.getLatitude();
                        baseLng = succ.getLongitude();
                        Log.d("AddressDeliveryPickerViewModel", baseLat + ", " + baseLng);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShipFeeByAddressDTO> call, @NonNull Throwable
                    throwable) {}
        });
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public void resetStates() {
        _addressSuggestions.setValue(null);
        _latLng.setValue(null);
        baseLat = null;
        baseLng = null;
    }

    public static class  LatLngCurrent {
        private Double curLat, curLng;
        private String detailAddress;

        public LatLngCurrent(Double curLat, Double curLng, String detailAddress) {
            this.curLat = curLat;
            this.detailAddress = detailAddress;
            this.curLng = curLng;
        }

        public String getDetailAddress() {
            return detailAddress;
        }

        public Double getCurLat() {
            return curLat;
        }

        public Double getCurLng() {
            return curLng;
        }

        @Override
        public String toString() {
            return "LatLngCurrent{" +
                    "curLat=" + curLat +
                    ", curLng=" + curLng +
                    ", detailAddress='" + detailAddress + '\'' +
                    '}';
        }
    }

    public static class Factory implements ViewModelProvider.Factory {
        private IShipFeeByAddressRepository shipFeeByAddressRepository;

        public Factory(
                IShipFeeByAddressRepository shipFeeByAddressRepository
        ) {
            this.shipFeeByAddressRepository = shipFeeByAddressRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AddressDeliveryPickerViewModel.class))
                return (T) new AddressDeliveryPickerViewModel(shipFeeByAddressRepository);
            throw new IllegalArgumentException("Model class illegal");
        }
    }
}