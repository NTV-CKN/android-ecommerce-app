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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.request.AddUserAddressDTO;
import com.infix.phukiencongnghe.data.dto.request.UpdateUserAddressDTO;
import com.infix.phukiencongnghe.data.dto.response.ShipFeeByAddressDTO;
import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;
import com.infix.phukiencongnghe.databinding.FragmentAddOrUpdateUserAddressBinding;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.address_picker.AddressDeliveryPickerFragment;
import com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.address_picker.AddressDeliveryPickerViewModel;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

import java.util.List;

public class AddOrUpdateUserAddressFragment extends Fragment {
    //Giúp xác định nếu fragment này đã load lần đầu tiên thì các lần sau sẽ không tạo lại đối tượng
    //UserAddressDTO và LatLngCurrent, tránh làm mất data từ AddressDeliveryPicker
    private boolean isFirstLoad = true;
    private LoadingDialog loadingDialog;

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
        loadingDialog = new LoadingDialog();
        setEvents();
        initAddressDeliveryPickerVM();
        initAddOrUpdateAddressVM();
        observeAddOrUpdateVM();
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
        addOrUpdateUserAddressViewModel.setLoadingState(null);
    }

    private void initAddressDeliveryPickerVM() {
        AddressDeliveryPickerViewModel.Factory factory = new AddressDeliveryPickerViewModel.Factory(
                InjectUtils.createShipFeeByAddressRepository()
        );

        addressDeliveryPickerViewModel = new ViewModelProvider(requireActivity(), factory).get(AddressDeliveryPickerViewModel.class);
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

    private void observeAddOrUpdateVM() {
        //shipFeeByAddress
        addOrUpdateUserAddressViewModel.shipFeeByAddresses.observe(
                getViewLifecycleOwner(),
                shipFeeByAddressDTOS -> {
                    if (!addOrUpdateUserAddressViewModel.isUpdate() && shipFeeByAddressDTOS != null) {
                        if (binding.spinnerProvinceCityUoaUserAddress.getAdapter() == null) {
                            ShipFeeSpinnerAdapter shipFeeAdapter = new ShipFeeSpinnerAdapter(requireContext(), shipFeeByAddressDTOS);
                            binding.spinnerProvinceCityUoaUserAddress.setAdapter(shipFeeAdapter);
                        }

                        //Nếu là lần đầu tải thì sẽ tạo dữ liệu tạm
                        if (isFirstLoad) {
                            int selected = 1;
                            binding.spinnerProvinceCityUoaUserAddress.setSelection(selected);

                            addOrUpdateUserAddressViewModel.setUserAddressState(
                                    new UserAddressDTO(
                                            null,
                                            "",
                                            "",
                                            shipFeeByAddressDTOS.get(selected).getProvinceCity(),
                                            "",
                                            shipFeeByAddressDTOS.get(selected).getLatitude()
                                            , shipFeeByAddressDTOS.get(selected).getLongitude()
                                    ));
                            addressDeliveryPickerViewModel.setLatLng(new AddressDeliveryPickerViewModel.LatLngCurrent(
                                    shipFeeByAddressDTOS.get(selected).getLatitude()
                                    , shipFeeByAddressDTOS.get(selected).getLongitude(),
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
                    }
                    else if (addOrUpdateUserAddressViewModel.isUpdate()) {
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

                        addressDeliveryPickerViewModel.setLatLng(new AddressDeliveryPickerViewModel.LatLngCurrent(
                                userAddressDTO.getLatitude(),
                                userAddressDTO.getLongitude(),
                                userAddressDTO.getAddressDetail()
                        ));

                        binding.switchAddressDefaultUoaUserAddress.setChecked(userAddressDTO.getDefault());
                    }
                });

        //show loading
        addOrUpdateUserAddressViewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading == null) return;

            if (isLoading)
                loadingDialog.show(
                        requireActivity().getSupportFragmentManager(),
                        null
                );
            else
                loadingDialog.dismiss();
        });

        //msg notify
        addOrUpdateUserAddressViewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg == null) return;
            Toast.makeText(
                    requireContext(),
                    msg,
                    Toast.LENGTH_LONG
            ).show();
        });
    }

    //hiển thị userAddressDTO lên form nhập
    private void inflateDataForm() {
        UserAddressDTO userAddressDTO = addOrUpdateUserAddressViewModel.userAddress.getValue();
        if (userAddressDTO == null) return;

        binding.edtNavDeliveryAddressUoaUserAddress.setText(userAddressDTO.getAddressDetail());
        binding.edtPhoneNumberUoaUserAddress.setText(userAddressDTO.getPhoneNumber());
        binding.edtReceiverNameUoaUserAddress.setText(userAddressDTO.getReceiverName());
        binding.tvLabelAddOrUpdateUserAddress.setText(
                addOrUpdateUserAddressViewModel.isUpdate()
                        ? getString(R.string.txt_update_user_address)
                        : getString(R.string.txt_add_user_address)
        );

        checkAndShowMiniMap();
    }

    private void setEvents() {
        //editText enter detail address click
        binding.edtNavDeliveryAddressUoaUserAddress.setOnClickListener(v -> {
            goToAddressDeliveryPickerFragment();
        });

        //spinner click
        handleSpinnerProvinceCityClicked();

        //confirm click
        binding.btnConfirmUoaUserAddress.setOnClickListener(v -> handleConfirmClick());

        //cancel
        binding.btnCancelUoaUserAddress.setOnClickListener(v -> handleCancelClick());

        //switch default address
        binding.switchAddressDefaultUoaUserAddress.setOnCheckedChangeListener((btnView, isChecked) -> {
            UserAddressDTO userAddressDTO = addOrUpdateUserAddressViewModel.userAddress.getValue();
            if (userAddressDTO == null) return;

//            if(isFirstLoad && addOrUpdateUserAddressViewModel.isUpdate()) {
//                isFirstLoad = false;
//                return;
//            }
            userAddressDTO.setDefault(isChecked);
        });
    }

    public void handleCancelClick() {
        SnackbarUtils.showSnackbarWithAction(
                binding.getRoot(),
                "Bạn có chắc muốn thoát không?",
                Snackbar.LENGTH_LONG,
                () -> requireActivity().getSupportFragmentManager().popBackStack()
        );
    }

    private void handleConfirmClick() {
        UserAddressDTO userAddressDTO = addOrUpdateUserAddressViewModel.userAddress.getValue();
        if (userAddressDTO == null) return;

        String receiverName = binding.edtReceiverNameUoaUserAddress.getText().toString();
        String phoneNumber = binding.edtPhoneNumberUoaUserAddress.getText().toString();

        userAddressDTO.setDefault(binding.switchAddressDefaultUoaUserAddress.isChecked());
        userAddressDTO.setReceiverName(receiverName);
        userAddressDTO.setPhoneNumber(phoneNumber);

        if (userAddressDTO.checkDataValid()) {
            SnackbarUtils.showSnackbarWithAction(
                    binding.getRoot(),
                    "Bạn có chắc chắn với hành động này?",
                    Snackbar.LENGTH_LONG,
                    () -> {
                        if (!addOrUpdateUserAddressViewModel.isUpdate()) {
                            AddUserAddressDTO addUserAddressDTO = new AddUserAddressDTO(
                                    userAddressDTO.getPhoneNumber(),
                                    userAddressDTO.getAddressDetail(),
                                    userAddressDTO.getProvinceCity(),
                                    userAddressDTO.getDefault(),
                                    userAddressDTO.getReceiverName(),
                                    userAddressDTO.getLatitude(),
                                    userAddressDTO.getLongitude()
                            );

                            addOrUpdateUserAddressViewModel.addUserAddress(addUserAddressDTO, () ->
                                    requireActivity().getSupportFragmentManager().popBackStack());
                        } else {
                            UpdateUserAddressDTO updateUserAddressDTO = new UpdateUserAddressDTO(
                                    userAddressDTO.getId(),
                                    userAddressDTO.getPhoneNumber(),
                                    userAddressDTO.getAddressDetail(),
                                    userAddressDTO.getProvinceCity(),
                                    userAddressDTO.getDefault(),
                                    userAddressDTO.getReceiverName(),
                                    userAddressDTO.getLatitude(),
                                    userAddressDTO.getLongitude()
                            );

                            addOrUpdateUserAddressViewModel.updateUserAddress(updateUserAddressDTO, () ->
                                    requireActivity().getSupportFragmentManager().popBackStack());
                        }
                    });
        } else
            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    "Có dữ liệu không hợp lệ",
                    Snackbar.LENGTH_SHORT
            );

    }

    private void handleSpinnerProvinceCityClicked() {
        binding.spinnerProvinceCityUoaUserAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ShipFeeByAddressDTO selectedShipFee = (ShipFeeByAddressDTO) parent.getItemAtPosition(position);
                if (selectedShipFee != null) {
                    UserAddressDTO userAddressDTO = addOrUpdateUserAddressViewModel.userAddress.getValue();
                    if (userAddressDTO == null) return;
                    Log.d("AddOrUpdate", userAddressDTO.toString());

                    if (addOrUpdateUserAddressViewModel.isReturnFromAddressDelivery()) {
                        Log.d("AddOrUpdate", "2");
                        addOrUpdateUserAddressViewModel.setReturnFromAddressDelivery(false);
                        inflateDataForm();

                        return;
                    }

                    Log.d("AddOrUpdate", "1");

                    if (!selectedShipFee.getProvinceCity().equals(userAddressDTO.getProvinceCity())) {
                        userAddressDTO.setProvinceCity(selectedShipFee.getProvinceCity());
                        userAddressDTO.setLongitude(selectedShipFee.getLongitude());
                        userAddressDTO.setLatitude(selectedShipFee.getLatitude());
                        userAddressDTO.setAddressDetail("");

                        if (binding != null) {
                            binding.edtNavDeliveryAddressUoaUserAddress.setText("");
                        }

                        addressDeliveryPickerViewModel.setLatLng(new AddressDeliveryPickerViewModel.LatLngCurrent(
                                selectedShipFee.getLatitude(),
                                selectedShipFee.getLongitude(),
                                ""
                        ));
                    }
                }
                inflateDataForm();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void goToAddressDeliveryPickerFragment() {
        addOrUpdateUserAddressViewModel.setReturnFromAddressDelivery(true);
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