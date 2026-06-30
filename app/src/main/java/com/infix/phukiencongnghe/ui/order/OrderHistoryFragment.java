package com.infix.phukiencongnghe.ui.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.data.repository.order.OrderRepositoryImpl;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper; // Hoặc class chứa client của dự án bạn
import com.infix.phukiencongnghe.data.source.remote.order.OrderSerivce;
import com.infix.phukiencongnghe.databinding.FragmentOrderHistoryBinding;
import com.infix.phukiencongnghe.ui.adapter.order.OrderHistoryAdapter;
import com.infix.phukiencongnghe.ui.auth.AuthActivity;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

public class OrderHistoryFragment extends Fragment {

    private FragmentOrderHistoryBinding binding;
    private OrderHistoryViewModel orderHistoryViewModel;
    private OrderHistoryAdapter adapter;
    private LoadingDialog loadingDialog;

    private int currentPage = 1;
    private final int pageSize = 10;
    private String selectedStatus = null;

    private final String[] statusNames = {"Tất cả", "Chờ phê duyệt", "Đang giao hàng", "Hoàn thành", "Đã hủy"};
    private final String[] statusCodes = {null, "pending_approve", "delivering", "completed", "cancel"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingDialog = new LoadingDialog();

        initRecyclerView();
        initDropdownStatus();
        initOrderHistoryViewModel();
        initPagination();
    }

    private void initRecyclerView() {
        adapter = new OrderHistoryAdapter();
        binding.rvOrderHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvOrderHistory.setAdapter(adapter);
    }

    private void initDropdownStatus() {
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, statusNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerOrderStatus.setAdapter(spinnerAdapter);

        binding.spinnerOrderStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = statusCodes[position];
                currentPage = 1;
                loadOrderHistoryData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void initOrderHistoryViewModel() {
        OrderSerivce orderSerivce = RetrofitHelper.getOrderService();
        OrderHistoryViewModel.Factory factory = new OrderHistoryViewModel.Factory(new OrderRepositoryImpl(orderSerivce));
        orderHistoryViewModel = new ViewModelProvider(this, factory).get(OrderHistoryViewModel.class);

        loadOrderHistoryData();

        orderHistoryViewModel.orderHistoryPage.observe(getViewLifecycleOwner(), pageData -> {
            if (pageData == null) return;
            adapter.submitList(pageData.getItems());
            binding.paginationBar.updatePagination(pageData.getCurrentPage(), pageData.getTotalPages());
        });

        orderHistoryViewModel.isLoading.observe(getViewLifecycleOwner(), bool -> {
            if (bool == null) return;
            if (bool) {
                if (!loadingDialog.isAdded()) {
                    loadingDialog.show(requireActivity().getSupportFragmentManager(), null);
                }
            } else {
                if (loadingDialog.isAdded()) {
                    loadingDialog.dismiss();
                }
            }
        });
        orderHistoryViewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg == null) return;
            SnackbarUtils.showBaseSnackbar(binding.getRoot(), msg, Snackbar.LENGTH_SHORT);
        });
    }

    private void loadOrderHistoryData() {
        orderHistoryViewModel.fetchOrderHistory(selectedStatus, currentPage, pageSize);
    }
    private void initPagination() {
        binding.paginationBar.setOnPageChangeListener(page -> {
            this.currentPage = page;
            loadOrderHistoryData();
            binding.mainLayout.smoothScrollTo(0, 0); // Đẩy cuộn mượt về đầu trang tránh khuất tầm nhìn
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}