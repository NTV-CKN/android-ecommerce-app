package com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.address_picker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.databinding.FragmentAddressDeliveryPickerBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class AddressDeliveryPickerFragment extends Fragment {
    private FragmentAddressDeliveryPickerBinding binding;
    private AddressDeliveryPickerAdapter addressDeliveryPickerAdapter;
    private AddressDeliveryPickerViewModel addressDeliveryPickerViewModel;
    private  PlacesClient placesClient;

    private LocationRestriction currentRegionRestriction = null;

    //Handler delay user enter key for address
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable = null;

    //args from newInstance
    private double provinceCityLat;
    private double provinceCityLng;
    private static final String ARG_LAT = "AddressDeliveryPickerFragment.ARG_LAT";
    private static final String ARG_LNG = "AddressDeliveryPickerFragment.ARG_LNG";

    public AddressDeliveryPickerFragment() {

    }

    public static AddressDeliveryPickerFragment newInstance(double provinceCityLat, double provinceCityLng) {
        AddressDeliveryPickerFragment fragment = new AddressDeliveryPickerFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LAT, provinceCityLat);
        args.putDouble(ARG_LNG, provinceCityLng);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            provinceCityLng = getArguments().getDouble(ARG_LNG);
            provinceCityLat = getArguments().getDouble(ARG_LAT);
            Log.d("SVU", provinceCityLat + " " + provinceCityLng);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddressDeliveryPickerBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEditTextEnterAddressKey();
        initPlacesMap();
        initViewModel();
        initRecyclerView();
        setSearchRegionRestriction(provinceCityLat, provinceCityLng);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlacesMap();
        binding = null;
    }

    private void initRecyclerView() {
        addressDeliveryPickerAdapter = new AddressDeliveryPickerAdapter(addressSuggestion -> {

        });

        binding.rvAddressSuggest.setAdapter(addressDeliveryPickerAdapter);
    }

    private void initViewModel() {
        addressDeliveryPickerViewModel = new ViewModelProvider(this).get(AddressDeliveryPickerViewModel.class);
        //observer list AddressSuggestion
        addressDeliveryPickerViewModel.addressSuggestions.observe(getViewLifecycleOwner(), lst -> {
            if(lst == null) return;
            addressDeliveryPickerAdapter.updateList(lst);
        });
    }

    public void setSearchRegionRestriction(double provinceLat, double provinceLng) {
        double offset = 0.2;

        LatLng southwest = new LatLng(provinceLat - offset, provinceLng - offset); // Góc Tây Nam
        LatLng northeast = new LatLng(provinceLat + offset, provinceLng + offset); // Góc Đông Bắc

        currentRegionRestriction = RectangularBounds.newInstance(southwest, northeast);
    }

    private void initEditTextEnterAddressKey() {
        binding.edtEnterAddressAddressDeliveryPicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String key = editable.toString().trim();

                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                if(key.isEmpty()) {
                    addressDeliveryPickerViewModel.updateAddressSuggestState(new ArrayList<>());
                    return;
                }

                searchRunnable = () -> {
                    Log.d("SVU", key);

                    addressDeliveryPickerViewModel.searchAddress(key, lst -> {
                        Log.d("SVU", Arrays.toString(lst.toArray()));
                        addressDeliveryPickerViewModel.updateAddressSuggestState(lst);
                    }, placesClient, currentRegionRestriction);
                };

                searchHandler.postDelayed(searchRunnable, 300);
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //ignore
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        });
    }

    private void initPlacesMap() {
        placesClient = Places.createClient(requireContext());
    }

    private void releasePlacesMap() {
        placesClient = null;
    }
}