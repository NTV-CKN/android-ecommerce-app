package com.infix.phukiencongnghe.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.infix.phukiencongnghe.data.dto.response.OrderDetailsHistoryDTO;
import com.infix.phukiencongnghe.data.dto.response.OrderHistoryDTO;
import com.infix.phukiencongnghe.databinding.FragmentOrderDetailBinding;
import com.infix.phukiencongnghe.ui.adapter.order.OrderDetailAdapter;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailFragment extends Fragment {
    private FragmentOrderDetailBinding binding;
    private OrderDetailAdapter adapter;
    private final DecimalFormat formatter = new DecimalFormat("#,###đ");
    private static final String ARG_ORDER = "order";
    private static final String ARG_DETAILS = "details";

    public static OrderDetailFragment newInstance(OrderHistoryDTO order, List<OrderDetailsHistoryDTO> details) {
        OrderDetailFragment fragment = new OrderDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ORDER, order);
        args.putSerializable(ARG_DETAILS, (java.io.Serializable) details);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrderDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new OrderDetailAdapter();
        binding.rvOrderDetail.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvOrderDetail.setAdapter(adapter);

        Bundle args = getArguments();
        if (args != null) {
            OrderHistoryDTO order = (OrderHistoryDTO) args.getSerializable(ARG_ORDER);
            List<OrderDetailsHistoryDTO> details = (List<OrderDetailsHistoryDTO>) args.getSerializable(ARG_DETAILS);

            if (order != null && details != null && !details.isEmpty()) {
                binding.tvOrderId.setText("Mã đơn: #" + order.getId());
                binding.tvOrderStatus.setText(order.getStatusOrder());
                binding.tvOrderDate.setText("Ngày đặt: " + order.getOrderDate().replace("T", " "));
                binding.tvReceiver.setText("Người nhận: " + order.getReceiverName());
                binding.tvTotalAmount.setText(formatter.format(order.getTotalMustPay()));

                adapter.submitList(details);
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}