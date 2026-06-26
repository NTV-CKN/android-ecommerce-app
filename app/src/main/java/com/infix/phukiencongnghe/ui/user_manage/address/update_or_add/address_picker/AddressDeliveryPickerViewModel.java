package com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.address_picker;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.infix.phukiencongnghe.data.model.user_manage.address.AddressSuggestion;

import java.util.ArrayList;
import java.util.Arrays;
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
                            Log.e("GG_DEBUG", "LỖI: " + e.getMessage());
                        }
                ).addOnCanceledListener(() -> {
                    Log.e("GG_DEBUG", "HUY");

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

    public void resetStates() {
        _addressSuggestions.setValue(null);
    }
}