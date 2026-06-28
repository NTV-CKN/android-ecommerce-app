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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.LocationRestriction;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;
import com.infix.phukiencongnghe.data.model.user_manage.address.AddressSuggestion;
import com.infix.phukiencongnghe.databinding.FragmentAddressDeliveryPickerBinding;
import com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.AddOrUpdateUserAddressViewModel;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class AddressDeliveryPickerFragment extends Fragment implements OnMapReadyCallback {
    private FragmentAddressDeliveryPickerBinding binding;
    private AddressDeliveryPickerAdapter addressDeliveryPickerAdapter;
    
    private AddressDeliveryPickerViewModel addressDeliveryPickerViewModel;
    private AddOrUpdateUserAddressViewModel addOrUpdateUserAddressViewModel;

    private PlacesClient placesClient;
    private GoogleMap googleMap;
    private Handler handler;

    private LocationRestriction currentRegionRestriction = null;

    //Handler delay user enter key for address
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable = null;

    public AddressDeliveryPickerFragment() {

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
        handler = new Handler(Looper.getMainLooper());
        initAddressDeliveryPickerViewModel();
        AddressDeliveryPickerViewModel.LatLngCurrent latLngCurrent = addressDeliveryPickerViewModel.latLng.getValue();
        if (latLngCurrent == null || latLngCurrent.getCurLat() == null || latLngCurrent.getCurLng() == null) {
            requireActivity().getSupportFragmentManager().popBackStack();
            return;
        }

        initEditTextEnterAddressKey();
        initPlacesMap();
        initRecyclerView();
        setSearchRegionRestriction(latLngCurrent.getCurLat(), latLngCurrent.getCurLng());
        initAddOrUpdateVMAndObserveLatLngCurrent();
        initBaseLatLngByProvinceCity();

        //sdk map
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if (addressDeliveryPickerViewModel.isUpdate()) {
            binding.rvAddressSuggest.setVisibility(View.GONE);
            binding.mapFragment.setVisibility(View.VISIBLE);
            binding.imgCenterMarker.setVisibility(View.VISIBLE);
            binding.btnConfirmAddressDeliveryPicker.setEnabled(true);
            binding.edtEnterAddressAddressDeliveryPicker.setEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlacesMap();
        binding = null;
    }

    private void initRecyclerView() {
        addressDeliveryPickerAdapter = new AddressDeliveryPickerAdapter(this::handleAddressSuggestionSelect);

        binding.rvAddressSuggest.setAdapter(addressDeliveryPickerAdapter);
    }

    private void initBaseLatLngByProvinceCity() {
        UserAddressDTO userAddressDTO = addOrUpdateUserAddressViewModel.userAddress.getValue();
        if(userAddressDTO == null) return;

        addressDeliveryPickerViewModel.initBaseLatLngProvinceCity(userAddressDTO.getProvinceCity());
    }

    private void initAddressDeliveryPickerViewModel() {
        AddressDeliveryPickerViewModel.Factory factory = new AddressDeliveryPickerViewModel.Factory(
                InjectUtils.createShipFeeByAddressRepository()
        );

        addressDeliveryPickerViewModel = new ViewModelProvider(requireActivity(), factory).get(AddressDeliveryPickerViewModel.class);
        //observer list AddressSuggestion
        addressDeliveryPickerViewModel.addressSuggestions.observe(getViewLifecycleOwner(), lst -> {
            if (lst == null) return;
            addressDeliveryPickerAdapter.updateList(lst);
        });
    }

    private void setSearchRegionRestriction(double provinceLat, double provinceLng) {
        double offset = 0.2;

        LatLng southwest = new LatLng(provinceLat - offset, provinceLng - offset); // Góc Tây Nam
        LatLng northeast = new LatLng(provinceLat + offset, provinceLng + offset); // Góc Đông Bắc

        currentRegionRestriction = RectangularBounds.newInstance(southwest, northeast);
    }

    //Khi user xác nhận vị trí thì ta lưu lại LatLngCurrent, từ đó ta đi set lại cho UserAddressDTO
    //mà AddOrUpdate  đang quản lí, khi back lại thì AddOrUpdate dựa trên data đang quản lí
    //mà inflate lại
    private void initAddOrUpdateVMAndObserveLatLngCurrent() {
        AddOrUpdateUserAddressViewModel.Factory factory =
                new AddOrUpdateUserAddressViewModel.Factory(
                        InjectUtils.createUserAddressManageRepository(requireContext()),
                        InjectUtils.createShipFeeByAddressRepository()
                );

        addOrUpdateUserAddressViewModel =
                new ViewModelProvider(requireActivity(), factory).get(AddOrUpdateUserAddressViewModel.class);

        //observe latlng current
        addressDeliveryPickerViewModel.latLng.observe(getViewLifecycleOwner(), latLngCurrent -> {
            if (latLngCurrent == null) return;

            UserAddressDTO userAddressDTO = addOrUpdateUserAddressViewModel.userAddress.getValue();
            if(userAddressDTO == null) return;
            
            userAddressDTO.setAddressDetail(latLngCurrent.getDetailAddress());
            userAddressDTO.setLatitude(latLngCurrent.getCurLat());
            userAddressDTO.setLongitude(latLngCurrent.getCurLng());
            Log.d("AddressDeliveryPickerFragment", userAddressDTO.toString());
        });
    }

    private void initEditTextEnterAddressKey() {
        binding.edtEnterAddressAddressDeliveryPicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String key = editable.toString().trim();

                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }

                binding.rvAddressSuggest.setVisibility(View.VISIBLE);
                binding.mapFragment.setVisibility(View.INVISIBLE);
                binding.imgCenterMarker.setVisibility(View.GONE);
                binding.btnConfirmAddressDeliveryPicker.setEnabled(false);

                if (key.isEmpty()) {
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
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
        });
    }

    private void initPlacesMap() {
        placesClient = Places.createClient(requireContext());
    }

    private void releasePlacesMap() {
        placesClient = null;
    }

    private void handleAddressSuggestionSelect(AddressSuggestion addressSuggestion) {
        Log.d("AddressDeliveryPickerFragment", "Address Suggest Click");
        if (addressSuggestion == null) return;
        addressDeliveryPickerViewModel.getPlaceDetail(addressSuggestion.getPlaceId(), placesClient, latLng -> {
            Log.d("AddressDeliveryPickerFragment", "Address Suggest Click 2");

            if (googleMap != null) {
                binding.rvAddressSuggest.setVisibility(View.GONE);
                binding.mapFragment.setVisibility(View.VISIBLE);
                binding.btnConfirmAddressDeliveryPicker.setEnabled(true);

                moveCameraToLocation(latLng, addressSuggestion.getTitle() + ", " + addressSuggestion.getSubtitle());
            }
        });
    }

    private void moveCameraToLocation(LatLng latLng, String detailAddress) {
        if (googleMap == null) return;

        googleMap.clear();
        binding.imgCenterMarker.setVisibility(View.VISIBLE);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.5f));

        googleMap.setOnCameraIdleListener(() -> {
            LatLng centerLatLng = googleMap.getCameraPosition().target;

            double newLat = centerLatLng.latitude;
            double newLng = centerLatLng.longitude;

            Log.d("AddressDeliveryPickerFragment", "New lat: : " + newLat + " , new lng: " + newLng);

            binding.btnConfirmAddressDeliveryPicker.setOnClickListener(v -> {
                double distanceKm = addressDeliveryPickerViewModel.calculateDistanceToRepositoryBase(newLat, newLng);
                double MAX_ALLOWED_DISTANCE_KM = 45.0;

                if (distanceKm > MAX_ALLOWED_DISTANCE_KM) {
                    SnackbarUtils.showBaseSnackbar(
                            binding.getRoot(),
                            "Vị trí bạn ghim không thuộc Tỉnh/Thành phố đã chọn trước đó. Vui lòng kiểm tra lại!",
                            Snackbar.LENGTH_LONG
                    );
                } else {
                    handleConfirmAddressSelection(newLat, newLng, detailAddress);
                }
            });
        });
    }

    private void handleConfirmAddressSelection(double lat, double lng, String detailAddress) {
        Log.d("AddressDeliveryPickerFragment", "chốt tọa độ: " + lat + " - " + lng);
        SnackbarUtils.showSnackbarWithAction(
                binding.getRoot(),
                "Bạn có chắc muốn chọn vị trí này?",
                Snackbar.LENGTH_LONG, () -> {
                    addressDeliveryPickerViewModel.setLatLng(new AddressDeliveryPickerViewModel.LatLngCurrent(lat, lng, detailAddress));
                    SnackbarUtils.showBaseSnackbar(
                            binding.getRoot(),
                            "Chuyển hướng sau 2 giây",
                            Snackbar.LENGTH_SHORT
                    );
                    handler.postDelayed(() -> {
                        requireActivity().getSupportFragmentManager()
                                .popBackStack();
                    }, 2000);
                });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        if(addressDeliveryPickerViewModel.isUpdate()) {
            UserAddressDTO userAddressDTO = addOrUpdateUserAddressViewModel.userAddress.getValue();
            if(userAddressDTO == null) return;

            AddressDeliveryPickerViewModel.LatLngCurrent latLngCurrent = addressDeliveryPickerViewModel.latLng.getValue();
            if (latLngCurrent != null && latLngCurrent.getCurLat() != null && latLngCurrent.getCurLng() != null) {
                moveCameraToLocation(
                        new LatLng(latLngCurrent.getCurLat(), latLngCurrent.getCurLng()), userAddressDTO.getAddressDetail()
                );
            }
        }
    }
}