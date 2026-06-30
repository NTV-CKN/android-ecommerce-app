package com.infix.phukiencongnghe.ui.admin.order.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.infix.phukiencongnghe.data.dto.response.OrderDetailAdminDTO;
import com.infix.phukiencongnghe.data.dto.response.VoucherDTO;
import com.infix.phukiencongnghe.data.repository.admin.order.AdminOrderRepositoryImpl;
import com.infix.phukiencongnghe.databinding.FragmentOrderDetailAdminBinding;

public class OrderDetailAdminFragment extends Fragment {

    private FragmentOrderDetailAdminBinding binding;
    private OrderDetailAdminViewModel viewModel;
    private OrderItemAdapter adapter;
    private Integer orderId;

    public static OrderDetailAdminFragment newInstance(Integer orderId) {

        OrderDetailAdminFragment fragment =
                new OrderDetailAdminFragment();

        Bundle bundle = new Bundle();
        bundle.putInt("order_id", orderId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        binding =
                FragmentOrderDetailAdminBinding.inflate(
                        inflater,
                        container,
                        false
                );

        if (getArguments() != null) {
            orderId = getArguments().getInt("order_id");
        }

        initViewModel();
        initRecyclerView();
        observeData();
        initButton();

        viewModel.loadOrderDetail(orderId);

        return binding.getRoot();
    }

    private void initViewModel() {

        OrderDetailAdminViewModel.Factory factory =
                new OrderDetailAdminViewModel.Factory(
                        new AdminOrderRepositoryImpl()
                );

        viewModel =
                new ViewModelProvider(
                        this,
                        factory
                ).get(OrderDetailAdminViewModel.class);
    }

    private void initRecyclerView() {

        adapter = new OrderItemAdapter();

        binding.rvItems.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        binding.rvItems.setAdapter(adapter);
    }

    private void initButton() {

        binding.btnChangeStatus.setOnClickListener(v -> {
            showStatusDialog();
        });
    }

    private void observeData() {

        viewModel.order.observe(

                getViewLifecycleOwner(),

                order -> {

                    /*
                     * ORDER BASIC
                     */
                    binding.tvOrderId.setText(
                            "Order #" + order.getOrderId()
                    );

                    binding.tvStatus.setText(
                            "Status: " + order.getStatus()
                    );

                    binding.tvOrderDate.setText(
                            "Date: " + order.getOrderDate()
                    );

                    /*
                     * CUSTOMER
                     */
                    String customerName =
                            order.getCustomerName();

                    if (customerName == null ||
                            customerName.isEmpty()) {

                        customerName = "No customer";
                    }

                    binding.tvCustomer.setText(
                            customerName
                    );

                    String email =
                            order.getCustomerEmail();

                    if (email == null ||
                            email.isEmpty()) {

                        email = "No email";
                    }

                    binding.tvEmail.setText(
                            "Email: " + email
                    );

                    /*
                     * PAYMENT
                     */
                    binding.tvTotalPrice.setText(
                            "Total: " +
                                    String.format(
                                            "%.0f VNĐ",
                                            order.getTotalPrice()
                                    )
                    );

                    Double shippingFee =
                            order.getShippingFee();

                    if (shippingFee == null) {
                        shippingFee = 0.0;
                    }

                    binding.tvShippingFee.setText(
                            "Shipping Fee: " +
                                    String.format(
                                            "%.0f VNĐ",
                                            shippingFee
                                    )
                    );

                    /*
                     * PAYMENT METHOD
                     */
                    String paymentMethod = "Unknown";

                    Integer paymentId =
                            order.getPaymentMethodId();

                    if (paymentId != null) {

                        if (paymentId == 1) {

                            paymentMethod = "COD";

                        } else if (paymentId == 2) {

                            paymentMethod = "Online Payment";
                        }
                    }

                    binding.tvPaymentMethod.setText(
                            "Payment: " + paymentMethod
                    );

                    /*
                     * NOTE
                     */
                    String note =
                            order.getNote();

                    if (note == null ||
                            note.isEmpty()) {

                        note = "No note";
                    }

                    binding.tvNote.setText(
                            "Note: " + note
                    );

                    /*
                     * PRODUCTS
                     */
                    adapter.setItems(
                            order.getItems()
                    );

                    /*
                     * VOUCHERS
                     */
                    showVoucher(order);
                }
        );

        viewModel.updateResult.observe(

                getViewLifecycleOwner(),

                result -> {

                    if ("success".equals(result)) {

                        Toast.makeText(
                                requireContext(),
                                "Update success",
                                Toast.LENGTH_SHORT
                        ).show();

                        viewModel.loadOrderDetail(
                                orderId
                        );

                    } else if ("error".equals(result)) {

                        Toast.makeText(
                                requireContext(),
                                "Update failed",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
    }

    private void showStatusDialog() {

        String[] statusList = {

                "approve_by_admin",
                "delivering",
                "completed",
                "cancel"
        };

        new androidx.appcompat.app.AlertDialog.Builder(
                requireContext()
        )

                .setTitle("Choose Status")

                .setItems(
                        statusList,
                        (dialog, which) -> {

                            String selectedStatus =
                                    statusList[which];

                            viewModel.updateStatus(
                                    orderId,
                                    selectedStatus
                            );
                        })

                .show();
    }

    /*
     * HIỂN THỊ VOUCHER
     */
    private void showVoucher(
            OrderDetailAdminDTO order
    ) {

        if (order.getAppliedVouchers() == null
                || order.getAppliedVouchers().isEmpty()) {

            binding.tvAppliedVoucher.setText(
                    "Không sử dụng voucher"
            );

            return;
        }

        StringBuilder builder =
                new StringBuilder();

        for (VoucherDTO voucher
                : order.getAppliedVouchers()) {

            builder.append("• ");

            builder.append(
                    voucher.getCode()
            );

            builder.append(" - ");

            builder.append(
                    voucher.getTitle()
            );

            builder.append("\n");

            builder.append("Giảm: ");

            builder.append(
                    String.format(
                            "%.0f",
                            voucher.getDiscountValue()
                    )
            );

            builder.append(" VNĐ");

            builder.append("\n");

            builder.append("Loại: ");

            if (voucher.getVoucherType() != null) {

                builder.append(
                        voucher.getVoucherType()
                                .getName()
                );

            } else {

                builder.append(
                        "Unknown"
                );
            }

            builder.append("\n\n");
        }

        binding.tvAppliedVoucher.setText(
                builder.toString()
        );
    }
}