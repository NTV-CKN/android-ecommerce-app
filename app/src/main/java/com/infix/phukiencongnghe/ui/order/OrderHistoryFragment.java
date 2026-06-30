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

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.common.StatusOrder;
import com.infix.phukiencongnghe.data.dto.response.OrderDetailsHistoryDTO;
import com.infix.phukiencongnghe.data.dto.response.OrderHistoryDTO;
import com.infix.phukiencongnghe.data.repository.order.OrderRepositoryImpl;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper; // Hoặc class chứa client của dự án bạn
import com.infix.phukiencongnghe.data.source.remote.order.OrderSerivce;
import com.infix.phukiencongnghe.databinding.FragmentOrderHistoryBinding;
import com.infix.phukiencongnghe.ui.adapter.order.OrderHistoryAdapter;
import com.infix.phukiencongnghe.ui.auth.AuthActivity;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

import java.util.List;

public class OrderHistoryFragment extends Fragment {

    private FragmentOrderHistoryBinding binding;
    private OrderHistoryViewModel orderHistoryViewModel;
    private OrderHistoryAdapter adapter;
    private LoadingDialog loadingDialog;

    private int currentPage = 1;
    private final int pageSize = 10;
    private String selectedStatus = null;

    private final String[] statusNames = {"Tất cả", "Chờ phê duyệt", "Đang giao hàng", "Hoàn thành", "Đã hủy"};

    private final String[] statusCodes = {
            null,
            StatusOrder.PENDING_APPROVE.getValue(),
            StatusOrder.DELIVERING.getValue(),
            StatusOrder.COMPLETED.getValue(),
            StatusOrder.CANCEL.getValue()
    };

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
        adapter.setOnOrderActionListener(new OrderHistoryAdapter.OnOrderActionListener() {
            @Override
            public void onCancelOrder(OrderHistoryDTO order) {
                String status = order.getStatusOrder();
                if(StatusOrder.PENDING_APPROVE.getValue().equals(status) || StatusOrder.APPROVE_BY_ADMIN.getValue().equals(status)){
                    showCancelOrder(order);
                }else{
                    SnackbarUtils.showBaseSnackbar(binding.getRoot(),"Không thể hủy đơn hàng ở trạng thái này!", Snackbar.LENGTH_SHORT);
                }
            }

            @Override
            public void onViewOrderDetail(OrderHistoryDTO order) {
              viewOrderDetail(order);
            }
        });
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
            if (Boolean.TRUE.equals(bool)) {
                if (loadingDialog==null) {
                    loadingDialog = new LoadingDialog();
                }
                if (!loadingDialog.isAdded()) {
                    loadingDialog.show(getChildFragmentManager(), "LoadingDialog");
                }
            } else {
                if (loadingDialog!=null) {
                   loadingDialog.dismissAllowingStateLoss();
                   loadingDialog = null;
                }
            }
        });
        orderHistoryViewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg == null) return;
            SnackbarUtils.showBaseSnackbar(binding.getRoot(), msg, Snackbar.LENGTH_SHORT);
        });
       orderHistoryViewModel.cancelResult.observe(getViewLifecycleOwner(),result->{
           if(result==null)return;
           if(result){
               SnackbarUtils.showBaseSnackbar(binding.getRoot(), "Hủy đơn hàng thành công!", Snackbar.LENGTH_SHORT);
               loadOrderHistoryData();
           }else{
               SnackbarUtils.showBaseSnackbar(binding.getRoot(),
                       "Hủy đơn hàng thất bại!", Snackbar.LENGTH_SHORT);
           }
       });
        orderHistoryViewModel.orderDetails.observe(getViewLifecycleOwner(), details -> {
            if (details != null && !details.isEmpty()) {
                OrderHistoryDTO currentOrder = orderHistoryViewModel.getCurrentOrder();
                if (currentOrder != null) {
                    OrderDetailFragment fragment = OrderDetailFragment.newInstance(currentOrder, details);
                    orderHistoryViewModel.clearOrderDetails();
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.fcv_user_manage, fragment)
                            .commit();
                }
            } else if (details != null) {
                SnackbarUtils.showBaseSnackbar(binding.getRoot(),
                        "Không có sản phẩm trong đơn hàng!", Snackbar.LENGTH_SHORT);
            }
        });    }
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
    private void showCancelOrder(OrderHistoryDTO order) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Xác nhận hủy đơn")
                .setMessage("Bạn có chắc chắn muốn hủy đơn hàng #" + order.getId() + " không?")
                .setPositiveButton("Hủy đơn", (dialog, which) -> {
                    orderHistoryViewModel.cancelOrder(order.getId());
                })
                .setNegativeButton("Quay lại", null)
                .show();
    }
    private void viewOrderDetail(OrderHistoryDTO order) {
        orderHistoryViewModel.setCurrentOrder(order);
        orderHistoryViewModel.fetchOrderDetails(order.getId());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}