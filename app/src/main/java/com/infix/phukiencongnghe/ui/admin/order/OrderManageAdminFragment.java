package com.infix.phukiencongnghe.ui.admin.order;

import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.OrderManageDTO;
import com.infix.phukiencongnghe.data.model.Page;
import com.infix.phukiencongnghe.data.repository.admin.order.AdminOrderRepositoryImpl;
import com.infix.phukiencongnghe.databinding.FragmentOrderManageAdminBinding;
import com.infix.phukiencongnghe.ui.admin.order.detail.OrderDetailAdminFragment;

public class OrderManageAdminFragment extends Fragment {

    private FragmentOrderManageAdminBinding binding;

    private OrderManageViewModel viewModel;

    private OrderManageAdapter adapter;

    private String currentStatus = null;

    private String currentKeyword = null;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        binding =
                FragmentOrderManageAdminBinding.inflate(
                        inflater,
                        container,
                        false
                );

        initViewModel();

        initRecyclerView();

        initPagination();

        initSpinner();

        initSearch();

        observeData();

        currentStatus = null;
        currentKeyword = null;

        viewModel.loadOrders(
                currentStatus,
                currentKeyword
        );

        return binding.getRoot();
    }

    private void initViewModel() {

        OrderManageViewModel.Factory factory =
                new OrderManageViewModel.Factory(
                        new AdminOrderRepositoryImpl()
                );

        viewModel =
                new ViewModelProvider(
                        this,
                        factory
                ).get(OrderManageViewModel.class);
    }

    private void initRecyclerView() {

        adapter =
                new OrderManageAdapter(

                        new OrderManageAdapter.OnOrderActionListener() {

                            @Override
                            public void onOrderClick(
                                    OrderManageDTO order
                            ) {

                                requireActivity()

                                        .getSupportFragmentManager()

                                        .beginTransaction()

                                        .replace(

                                                R.id.fcv_admin_manage,

                                                OrderDetailAdminFragment
                                                        .newInstance(
                                                                order.getOrderId()
                                                        )
                                        )

                                        .addToBackStack(null)

                                        .commit();
                            }

                            @Override
                            public void onChangeStatusClick(
                                    OrderManageDTO order
                            ) {

                                showChangeStatusDialog(
                                        order.getOrderId()
                                );
                            }
                        }
                );

        binding.rvOrders.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );

        binding.rvOrders.setAdapter(
                adapter
        );
    }

    private void initPagination() {

        binding.paginationBar.setOnPageChangeListener(

                newPage -> {

                    viewModel.changePage(

                            newPage,

                            currentStatus,

                            currentKeyword
                    );
                }
        );
    }

    private void initSpinner() {

        String[] list = {
                "Tất cả",
                "Chờ xác nhận",
                "Hoàn thành",
                "Đã hủy"
        };

        ArrayAdapter<String> spinnerAdapter =
                new ArrayAdapter<>(

                        requireContext(),

                        android.R.layout.simple_spinner_item,

                        list
                );

        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        binding.spinnerStatus.setAdapter(
                spinnerAdapter
        );

        binding.spinnerStatus.setOnItemSelectedListener(

                new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(
                            AdapterView<?> parent,
                            View view,
                            int position,
                            long id
                    ) {

                        String status = null;

                        if (position == 1) {

                            status = "pending_approve";

                        } else if (position == 2) {

                            status = "completed";

                        } else if (position == 3) {

                            status = "cancel";
                        }

                        currentStatus = status;
                        currentKeyword = null;
                        binding.searchOrder.setQuery("", false);
                        binding.searchOrder.clearFocus();

                        // reset page về trang 1
                        viewModel.resetPagination();

                        viewModel.loadOrders(

                                currentStatus,

                                currentKeyword
                        );
                    }

                    @Override
                    public void onNothingSelected(
                            AdapterView<?> parent
                    ) {

                    }
                }
        );
    }

    private void observeData() {

        /*
         * data orders
         */
        viewModel.orders.observe(

                getViewLifecycleOwner(),

                orders -> {

                    adapter.setOrders(
                            orders
                    );
                }
        );

        /*
         * observe current page
         */
        viewModel.currentPage.observe(

                getViewLifecycleOwner(),

                page -> {

                    Integer totalPages =
                            viewModel.totalPages.getValue();

                    if (totalPages == null) {
                        totalPages = 1;
                    }

                    binding.paginationBar.updatePagination(

                            page.getPage(),

                            totalPages
                    );
                }
        );

        /*
         * observe total pages
         */
        viewModel.totalPages.observe(

                getViewLifecycleOwner(),

                totalPages -> {

                    Page currentPage =
                            viewModel.currentPage.getValue();

                    int page = 1;

                    if (currentPage != null) {
                        page = currentPage.getPage();
                    }

                    binding.paginationBar.updatePagination(

                            page,

                            totalPages
                    );
                }
        );

        /*
         * notify
         */
        viewModel.notify.observe(

                getViewLifecycleOwner(),

                message -> {

                    Toast.makeText(

                            requireContext(),

                            message,

                            Toast.LENGTH_SHORT

                    ).show();
                }
        );
    }

    private void showChangeStatusDialog(
            Integer orderId
    ) {

        String[] list = {
                "pending_approve",
                "completed",
                "cancel"
        };

        new AlertDialog.Builder(
                requireContext()
        )

                .setTitle("Chọn trạng thái")

                .setItems(
                        list,

                        (dialog, which) -> {

                            String status =
                                    list[which];

                            viewModel.updateOrderStatus(

                                    orderId,

                                    status
                            );
                        }
                )

                .show();
    }

    private void initSearch() {

        binding.searchOrder.setOnQueryTextListener(

                new SearchView.OnQueryTextListener() {

                    @Override
                    public boolean onQueryTextSubmit(
                            String query
                    ) {

                        currentKeyword = query.trim();
                        binding.searchOrder.clearFocus();
                        viewModel.resetPagination();
                        viewModel.loadOrders(
                                currentStatus,
                                currentKeyword
                        );

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(
                            String newText
                    ) {

                        /*
                         * nếu xóa search
                         */
                        if (newText.isEmpty()) {

                            currentKeyword = null;

                            viewModel.resetPagination();

                            viewModel.loadOrders(
                                    currentStatus,
                                    null
                            );
                        }

                        return true;
                    }
                }
        );
    }
}