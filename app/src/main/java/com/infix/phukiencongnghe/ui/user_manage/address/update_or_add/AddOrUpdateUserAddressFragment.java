package com.infix.phukiencongnghe.ui.user_manage.address.update_or_add;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.ShipFeeByAddressDTO;
import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;
import com.infix.phukiencongnghe.databinding.FragmentAddOrUpdateUserAddressBinding;
import com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.address_picker.AddressDeliveryPickerFragment;
import com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.address_picker.AddressDeliveryPickerViewModel;
import com.infix.phukiencongnghe.utils.InjectUtils;

import java.util.List;

public class AddOrUpdateUserAddressFragment extends Fragment {
    //Giúp xác định nếu fragment này đã load lần đầu tiên thì các lần sau sẽ không tạo lại đối tượng
    //UserAddressDTO và LatLngCurrent, tránh làm mất data từ AddressDeliveryPicker
    private boolean isFirstLoad = true;

    private FragmentAddOrUpdateUserAddressBinding binding;

    private AddressDeliveryPickerViewModel addressDeliveryPickerViewModel;
    private AddOrUpdateUserAddressViewModel addOrUpdateUserAddressViewModel;

    private GoogleMap miniGoogleMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddOrUpdateUserAddressBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.mapViewMiniUoaUserAddress.onCreate(savedInstanceState);
        setEvents();
        initAddressDeliveryPickerVM();
        initAddOrUpdateAddressVM();
        observeAddOrUpdateVM();
        observeAddressDeliveryPickerVM();
        inflateDataForm();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (binding != null) binding.mapViewMiniUoaUserAddress.onResume();
    }

    @Override
    public void onPause() {
        if (binding != null) binding.mapViewMiniUoaUserAddress.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (binding != null) binding.mapViewMiniUoaUserAddress.onDestroy();
        super.onDestroy();
        addressDeliveryPickerViewModel.resetStates();
        addOrUpdateUserAddressViewModel.resetAllStates();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (binding != null) binding.mapViewMiniUoaUserAddress.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initAddressDeliveryPickerVM() {
        addressDeliveryPickerViewModel = new ViewModelProvider(requireActivity()).get(AddressDeliveryPickerViewModel.class);
    }

    private void initAddOrUpdateAddressVM() {
        AddOrUpdateUserAddressViewModel.Factory factory =
                new AddOrUpdateUserAddressViewModel.Factory(
                        InjectUtils.createUserAddressManageRepository(requireContext()),
                        InjectUtils.createShipFeeByAddressRepository()
                );

        addOrUpdateUserAddressViewModel =
                new ViewModelProvider(requireActivity(), factory).get(AddOrUpdateUserAddressViewModel.class);

        if (!addOrUpdateUserAddressViewModel.isUpdate() && isFirstLoad) {
            addOrUpdateUserAddressViewModel.getShipFeeByAddresses();
        }
        //Thiết lập option cập nhật hay thêm địa chỉ cho AddressDeliveryPickerViewMode
        addressDeliveryPickerViewModel.setIsUpdate(addOrUpdateUserAddressViewModel.isUpdate());
    }

    private void observeAddressDeliveryPickerVM() {
        //LatLngCurrent: Lắng nghe cập nhật khi ngưòi dùng đổi các chỉ số về address detail, lat/lng
        addressDeliveryPickerViewModel.latLng.observe(getViewLifecycleOwner(), latLngCurrent -> {
            Log.d("AddOrUpdateUserAddressFragment", "." + latLngCurrent);
            if (latLngCurrent == null) return;

            UserAddressDTO userAddressDTO = addOrUpdateUserAddressViewModel.userAddress.getValue();
            userAddressDTO.setAddressDetail(latLngCurrent.getDetailAddress());
            userAddressDTO.setLatitude(latLngCurrent.getCurLat());
            userAddressDTO.setLongitude(latLngCurrent.getCurLng());

            inflateDataForm();
        });
    }

    private void observeAddOrUpdateVM() {
        //shipFeeByAddress
        addOrUpdateUserAddressViewModel.shipFeeByAddresses.observe(
                getViewLifecycleOwner(),
                shipFeeByAddressDTOS -> {
                    if (!addOrUpdateUserAddressViewModel.isUpdate() && shipFeeByAddressDTOS != null) {
                        //Thêm vào spinner
                        ShipFeeSpinnerAdapter shipFeeAdapter = new ShipFeeSpinnerAdapter(requireContext(), shipFeeByAddressDTOS);
                        binding.spinnerProvinceCityUoaUserAddress.setAdapter(shipFeeAdapter);

                        //Nếu là lần đầu tải thì sẽ tạo dữ liệu tạm
                        if (isFirstLoad) {
                            binding.spinnerProvinceCityUoaUserAddress.setSelection(0);

                            addOrUpdateUserAddressViewModel.setUserAddressState(
                                    new UserAddressDTO(
                                            null,
                                            "",
                                            "",
                                            shipFeeByAddressDTOS.get(1).getProvinceCity(),
                                            "",
                                            shipFeeByAddressDTOS.get(1).getLatitude()
                                            , shipFeeByAddressDTOS.get(1).getLongitude()
                                    ));
                            addressDeliveryPickerViewModel.setLatLng(new AddressDeliveryPickerViewModel.LatLngCurrent(
                                    shipFeeByAddressDTOS.get(1).getLatitude()
                                    , shipFeeByAddressDTOS.get(1).getLongitude(),
                                    ""
                            ));

                            isFirstLoad = false;
                        }
                        //Nếu không thì hiển thị lại thông tin đã có sẵn ở phiên tạo đầu tiên với option thêm địa chỉ
                        else {
                            if (addOrUpdateUserAddressViewModel.userAddress.getValue() == null)
                                return;

                            for (int i = 0; i < shipFeeByAddressDTOS.size(); i++) {
                                if (shipFeeByAddressDTOS.get(i)
                                        .getProvinceCity()
                                        .equals(addOrUpdateUserAddressViewModel.userAddress.getValue().getProvinceCity())
                                ) {
                                    binding.spinnerProvinceCityUoaUserAddress.setSelection(i);
                                    break;
                                }
                            }
                        }

                    } else if (addOrUpdateUserAddressViewModel.isUpdate()) {
                        UserAddressDTO userAddressDTO = addOrUpdateUserAddressViewModel.userAddress.getValue();
                        if (userAddressDTO == null) return;

                        ShipFeeByAddressDTO shipFeeByAddressDTO = new ShipFeeByAddressDTO();
                        shipFeeByAddressDTO.setLatitude(userAddressDTO.getLatitude());
                        shipFeeByAddressDTO.setLongitude(userAddressDTO.getLongitude());
                        shipFeeByAddressDTO.setProvinceCity(userAddressDTO.getProvinceCity());

                        //Thêm vào spinner
                        ShipFeeSpinnerAdapter shipFeeAdapter = new ShipFeeSpinnerAdapter(requireContext(), List.of(shipFeeByAddressDTO));
                        binding.spinnerProvinceCityUoaUserAddress.setAdapter(shipFeeAdapter);
                        binding.spinnerProvinceCityUoaUserAddress.setSelection(0);
                    }
                });
    }

    //hiển thị userAddressDTO lên form nhập
    private void inflateDataForm() {
        UserAddressDTO userAddressDTO = addOrUpdateUserAddressViewModel.userAddress.getValue();
        if (userAddressDTO == null) return;

        checkAndShowMiniMap();
        binding.edtNavDeliveryAddressUoaUserAddress.setText(userAddressDTO.getAddressDetail());
        binding.edtPhoneNumberUoaUserAddress.setText(userAddressDTO.getPhoneNumber());
        binding.edtReceiverNameUoaUserAddress.setText(userAddressDTO.getReceiverName());
        binding.tvLabelAddOrUpdateUserAddress.setText(
                addOrUpdateUserAddressViewModel.isUpdate()
                        ? getString(R.string.txt_update_user_address)
                        : getString(R.string.txt_add_user_address)
        );
    }

    private void setEvents() {
        //editText enter detail address click
        binding.edtNavDeliveryAddressUoaUserAddress.setOnClickListener(v -> {
            goToAddressDeliveryPickerFragment();
        });

        //spinner click
        handleSpinnerProvinceCityClicked();
    }

    private void handleSpinnerProvinceCityClicked() {
        binding.spinnerProvinceCityUoaUserAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ShipFeeByAddressDTO selectedShipFee = (ShipFeeByAddressDTO) parent.getItemAtPosition(position);
                if (selectedShipFee != null) {
                    UserAddressDTO userAddressDTO = addOrUpdateUserAddressViewModel.userAddress.getValue();
                    if (userAddressDTO == null) return;

                    userAddressDTO.setLongitude(selectedShipFee.getLongitude());
                    userAddressDTO.setLatitude(selectedShipFee.getLatitude());
                    userAddressDTO.setProvinceCity(selectedShipFee.getProvinceCity());

                    inflateDataForm();
                    binding.edtNavDeliveryAddressUoaUserAddress.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void goToAddressDeliveryPickerFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_user_manage, new AddressDeliveryPickerFragment())
                .addToBackStack(null)
                .commit();
    }

    //Hàm này check xem đã có lat/lng chưa, nếu có thì show mapview
    private void checkAndShowMiniMap() {
        if (addOrUpdateUserAddressViewModel.userAddress.getValue() == null) return;

        Double selectedLat = addOrUpdateUserAddressViewModel.userAddress.getValue().getLatitude();
        Double selectedLng = addOrUpdateUserAddressViewModel.userAddress.getValue().getLongitude();
        if (selectedLat != null && selectedLng != null) {
            binding.mapViewMiniUoaUserAddress.setVisibility(View.VISIBLE);
            binding.mapViewMiniUoaUserAddress.getMapAsync(googleMap -> {
                this.miniGoogleMap = googleMap;
                LatLng targetLocation = new LatLng(selectedLat, selectedLng);
                miniGoogleMap.getUiSettings().setScrollGesturesEnabled(false);
                miniGoogleMap.getUiSettings().setZoomControlsEnabled(false);

                miniGoogleMap.clear();
                miniGoogleMap.addMarker(new MarkerOptions().position(targetLocation));

                miniGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(targetLocation, 16.0f));
            });
        } else {
            binding.mapViewMiniUoaUserAddress.setVisibility(View.GONE);
        }
    }
}