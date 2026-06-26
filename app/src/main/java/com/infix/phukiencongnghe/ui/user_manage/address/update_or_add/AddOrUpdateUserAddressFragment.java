package com.infix.phukiencongnghe.ui.user_manage.address.update_or_add;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;
import com.infix.phukiencongnghe.databinding.FragmentAddOrUpdateUserAddressBinding;
import com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.address_picker.AddressDeliveryPickerFragment;
import com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.address_picker.AddressDeliveryPickerViewModel;

public class AddOrUpdateUserAddressFragment extends Fragment {
    private FragmentAddOrUpdateUserAddressBinding binding;
    private AddressDeliveryPickerViewModel addressDeliveryPickerViewModel;

    private static final String ARG_IS_UPDATE = "AddOrUpdateUserAddressFragment.ARG_IS_UPDATE";
    private static final String ARG_USER_ADDRESS_DTO = "AddOrUpdateUserAddressFragment.ARG_USER_ADDRESS_DTO";

    private Boolean isUpdate;
    private UserAddressDTO userAddressDTO;

//    public static AddOrUpdateUserAddressFragment newInstanceForUpdate(String param1, String param2) {
//        AddOrUpdateUserAddressFragment fragment = new AddOrUpdateUserAddressFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static AddOrUpdateUserAddressFragment newInstance(boolean isUpdate) {
        AddOrUpdateUserAddressFragment fragment = new AddOrUpdateUserAddressFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_UPDATE, isUpdate);
        fragment.setArguments(args);
        return fragment;
    }

    public static AddOrUpdateUserAddressFragment newInstance(UserAddressDTO userAddressDTO) {
        AddOrUpdateUserAddressFragment fragment = new AddOrUpdateUserAddressFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_UPDATE, false);
        args.putSerializable(ARG_USER_ADDRESS_DTO, userAddressDTO);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isUpdate = getArguments().getBoolean(ARG_IS_UPDATE);
            userAddressDTO = (UserAddressDTO) getArguments().getSerializable(ARG_USER_ADDRESS_DTO);
        }
    }

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
        setEvents();
        initAddressDeliveryPickerVM();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initAddressDeliveryPickerVM() {

    }

    private void setEvents() {
        //editText enter detail address click
        binding.edtNavDeliveryAddressUoaUserAddress.setOnClickListener(v -> {
            goToAddressDeliveryPickerFragment();
        });
    }

    private void goToAddressDeliveryPickerFragment() {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_user_manage, new AddressDeliveryPickerFragment())
                .addToBackStack(null)
                .commit();
    }
}