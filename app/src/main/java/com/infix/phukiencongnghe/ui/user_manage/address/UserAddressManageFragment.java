package com.infix.phukiencongnghe.ui.user_manage.address;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;
import com.infix.phukiencongnghe.databinding.FragmentUserAddressManageBinding;
import com.infix.phukiencongnghe.ui.adapter.address_manage.user.UserAddressAdapter;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.ui.user_manage.UserManagerActivity;
import com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.AddOrUpdateUserAddressFragment;
import com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.AddOrUpdateUserAddressViewModel;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

public class UserAddressManageFragment extends Fragment {
    private FragmentUserAddressManageBinding binding;

    private UserAddressManageViewModel userAddressManageViewModel;
    //Dùng để thực hiện truyền UserAddressDTO/trạng thái cập nhật hay thêm địa chỉ
    private AddOrUpdateUserAddressViewModel addOrUpdateUserAddressViewModel;

    private UserAddressAdapter userAddressAdapter;
    private LoadingDialog loadingDialog;
    private static final  String ARG_IS_SELECT_MODE = "UserAddressManageFragment.ARG_IS_SELECT_MODE";
    private boolean isSelectMode = false;

    public static UserAddressManageFragment newInstance(boolean b) {
        UserAddressManageFragment fragment = new UserAddressManageFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_SELECT_MODE, b);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isSelectMode = getArguments().getBoolean(ARG_IS_SELECT_MODE, false);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserAddressManageBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog();
        initRecyclerView();
        initUserAddressManageViewModel();
        initAddOrUpdateAddressVM();
        setEvents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        userAddressManageViewModel.resetStates();
        binding = null;
        userAddressAdapter = null;
    }

    private void initRecyclerView() {
        userAddressAdapter = new UserAddressAdapter((userAddressDTO) -> {
        if(isSelectMode){
            Intent intent = new Intent();
            intent.putExtra(UserManagerActivity.EXTRA_SELECTED_ADDRESS, userAddressDTO);
            requireActivity().setResult(Activity.RESULT_OK,intent);
            requireActivity().finish();
        }
        });

        binding.rvUserAddressManage.setAdapter(userAddressAdapter);
    }


    private void initAddOrUpdateAddressVM() {
        AddOrUpdateUserAddressViewModel.Factory factory =
                new AddOrUpdateUserAddressViewModel.Factory(InjectUtils.createUserAddressManageRepository(requireContext()));

        addOrUpdateUserAddressViewModel =
                new ViewModelProvider(requireActivity(), factory).get(AddOrUpdateUserAddressViewModel.class);

    }

    private void initUserAddressManageViewModel() {
        UserAddressManageViewModel.Factory factory =
                new UserAddressManageViewModel.Factory(InjectUtils.createUserAddressManageRepository(requireContext()));

        userAddressManageViewModel =
                new ViewModelProvider(requireActivity(), factory).get(UserAddressManageViewModel.class);

        //observe user address list
        userAddressManageViewModel.getUserAddresses();
        userAddressManageViewModel.userAddresses.observe(getViewLifecycleOwner(), lst -> {
            if(lst == null) return;
            userAddressAdapter.updateList(lst);
        });

        //observe notify msg
        userAddressManageViewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg == null) return;
            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    msg,
                    Snackbar.LENGTH_SHORT
            );
        });

        //observe is loading
        userAddressManageViewModel.isLoading.observe(getViewLifecycleOwner(), bool -> {
            if(bool == null) return;
            if(bool)
                loadingDialog.show(requireActivity().getSupportFragmentManager(), null);
            else
                loadingDialog.dismiss();
        });
    }

    private void setEvents() {
        //fab
        binding.fabAddAddressUserManage.setOnClickListener(v -> {
            goToAddOrUpdateUserAddressFragment(false);
        });
    }

    private void goToAddOrUpdateUserAddressFragment(boolean isUpdate) {
        addOrUpdateUserAddressViewModel.setIsUpdate(isUpdate);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_user_manage, new AddOrUpdateUserAddressFragment())
                .addToBackStack(null)
                .commit();
    }
}