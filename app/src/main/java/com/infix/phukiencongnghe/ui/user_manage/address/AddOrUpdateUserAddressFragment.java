package com.infix.phukiencongnghe.ui.user_manage.address;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.databinding.FragmentAddOrUpdateUserAddressBinding;

public class AddOrUpdateUserAddressFragment extends Fragment {
    private FragmentAddOrUpdateUserAddressBinding binding;

    private static final String ARG_IS_UPDATE = "AddOrUpdateUserAddressFragment.ARG_IS_UPDATE";
    private static final String ARG_USER_ID = "AddOrUpdateUserAddressFragment.ARG_USER_ID";

    private Boolean isUpdate;
    private Integer userId;

//    public static AddOrUpdateUserAddressFragment newInstanceForUpdate(String param1, String param2) {
//        AddOrUpdateUserAddressFragment fragment = new AddOrUpdateUserAddressFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static AddOrUpdateUserAddressFragment newInstanceForAdd(Integer userId) {
        AddOrUpdateUserAddressFragment fragment = new AddOrUpdateUserAddressFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_UPDATE, false);
        args.putInt(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isUpdate = getArguments().getBoolean(ARG_IS_UPDATE);
            userId = getArguments().getInt(ARG_USER_ID);
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

    }
}