package com.infix.phukiencongnghe.ui.user_manage.address;

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
import com.infix.phukiencongnghe.data.repository.user_manage.address.IUserAddressManageRepository;
import com.infix.phukiencongnghe.data.repository.user_manage.address.UserAddressManageRepositoryImpl;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.databinding.FragmentUserAddressManageBinding;
import com.infix.phukiencongnghe.ui.adapter.address_manage.user.UserAddressAdapter;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.AddOrUpdateUserAddressFragment;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

public class UserAddressManageFragment extends Fragment {
    private FragmentUserAddressManageBinding binding;
    private UserAddressManageViewModel userAddressManageViewModel;
    private UserAddressAdapter userAddressAdapter;
    private LoadingDialog loadingDialog;

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
        setEvents();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        userAddressManageViewModel.resetStates();
        binding = null;
        userAddressAdapter = null;
    }

    private void initRecyclerView() {
        userAddressAdapter = new UserAddressAdapter((userAddressDTO) -> {

        });

        binding.rvUserAddressManage.setAdapter(userAddressAdapter);
    }

    private void initUserAddressManageViewModel() {
        IUserAddressManageRepository repository = new UserAddressManageRepositoryImpl(
                RetrofitHelper.getUserAddressManageService()
        );

        UserAddressManageViewModel.Factory factory =
                new UserAddressManageViewModel.Factory(repository);

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
        AddOrUpdateUserAddressFragment addOrUpdateUserAddressFragment = null;
        if(!isUpdate)
            addOrUpdateUserAddressFragment =
                    AddOrUpdateUserAddressFragment.newInstanceForAdd(1);

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcv_user_manage, addOrUpdateUserAddressFragment)
                .commit();
    }
}