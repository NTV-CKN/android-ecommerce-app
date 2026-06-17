package com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.address_picker;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.infix.phukiencongnghe.data.model.user_manage.address.AddressSuggestion;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AddressDeliveryPickerViewModel extends ViewModel {

    private final MutableLiveData<List<AddressSuggestion>> _addressSuggestions = new MutableLiveData<>();
    public final LiveData<List<AddressSuggestion>> addressSuggestions = _addressSuggestions;
//
//    //If user update address => enter address must not null
//    private final MutableLiveData<String> _enterAddress = new MutableLiveData<>();
//    public final LiveData<String> enterAddress = _enterAddress;
//
//    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
//    public final LiveData<Boolean> isLoading = _isLoading;

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
                    Log.d("GG_DEBUG", "Số lượng gợi ý nhận được: " + response.getAutocompletePredictions().size());
                    List<AddressSuggestion> list =
                            new ArrayList<>();

                    for (AutocompletePrediction prediction :
                            response.getAutocompletePredictions()) {

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
                            Log.e("GG_DEBUG", "LỖI: " + e.getMessage());
                        }
                ).addOnCanceledListener(() -> {
                    Log.e("GG_DEBUG", "HUY");

                });
    }

    public void updateAddressSuggestState(List<AddressSuggestion> addressSuggestions) {
        _addressSuggestions.setValue(addressSuggestions);
    }

//    public void getPlaceDetail(String placeId, PlacesClient placesClient) {
//        if (placesClient == null || placeId == null || placeId.trim().isEmpty()) {
//            return;
//        }
//
//        // 1. Định nghĩa các trường thông tin muốn Google trả về (Cần gì xin nấy để tiết kiệm tiền)
//        List<Place.Field> fields = Arrays.asList(
//                Place.Field.ID,
//                Place.Field.NAME,
//                Place.Field.ADDRESS,
//                Place.Field.LAT_LNG
//        );
//
//        // 2. Tạo yêu cầu lấy chi tiết địa điểm dựa vào placeId và danh sách fields ở trên
//        FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, fields);
//
//        // 3. Thực hiện gọi API bất đồng bộ lên máy chủ Google
//        placesClient.fetchPlace(request)
//                .addOnSuccessListener(response -> {
//                    Place place = response.getPlace();
//                    LatLng latLng = place.getLatLng();
//
//                    if (latLng != null) {
//                        double latitude = latLng.latitude;
//                        double longitude = latLng.longitude;
//                        String fullAddress = place.getAddress();
//
//                        // In thông tin ra Logcat để kiểm tra (Debug)
//                        Log.d("PLACE_DETAIL", "Name: " + place.getName());
//                        Log.d("PLACE_DETAIL", "Address: " + fullAddress);
//                        Log.d("PLACE_DETAIL", "Latitude: " + latitude);
//                        Log.d("PLACE_DETAIL", "Longitude: " + longitude);
//
//                        // 🌟 NƠI THỰC HIỆN LOGIC TIẾP THEO:
//                        // Bạn sẽ truyền fullAddress, latitude, longitude này vào Model Address
//                        // để lưu xuống SQLite/Room Database hoặc gửi lên Server Backend của bạn.
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("PLACE_DETAIL", "Lỗi lấy chi tiết địa điểm: " + e.getMessage());
//                    e.printStackTrace();
//                });
//    }

    public void resetStates() {
        _addressSuggestions.setValue(null);
    }
}