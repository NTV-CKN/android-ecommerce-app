package com.infix.phukiencongnghe.ui.adapter.order;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.common.StatusOrder;
import com.infix.phukiencongnghe.data.dto.response.OrderHistoryDTO;
import com.infix.phukiencongnghe.databinding.ItemOrderHistoryBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {
    private List<OrderHistoryDTO> orderHistoryDTOList = new ArrayList<>();
    private OnOrderActionListener listener;
    public interface OnOrderActionListener {
        void onCancelOrder(OrderHistoryDTO order);
        void onViewOrderDetail(OrderHistoryDTO order);
    }
    public void setOnOrderActionListener(OnOrderActionListener listener) {
        this.listener = listener;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<OrderHistoryDTO> list) {
        this.orderHistoryDTOList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    public OrderHistoryAdapter() {
    }

    @NonNull
    @Override
    public OrderHistoryAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderHistoryBinding binding = ItemOrderHistoryBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.OrderViewHolder holder, int position) {
        holder.bind(orderHistoryDTOList.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return orderHistoryDTOList.size();
    }
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderHistoryBinding binding;
        private final DecimalFormat formatter = new DecimalFormat("#,###đ");

        public OrderViewHolder(ItemOrderHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OrderHistoryDTO orderHistoryDTO, OnOrderActionListener listener) {
            binding.tvItemOrderId.setText("Mã đơn: #" + orderHistoryDTO.getId());
            binding.tvItemOrderStatus.setText(orderHistoryDTO.getStatusOrder());
            binding.tvItemOrderDate.setText("Ngày đặt: " + orderHistoryDTO.getOrderDate().replace("T", " "));
            binding.tvItemReceiver.setText("Người nhận: " + orderHistoryDTO.getReceiverName());
            binding.tvItemTotalPay.setText(formatter.format(orderHistoryDTO.getTotalMustPay()));

            binding.btnViewOrderDetails.setOnClickListener(v->{
                if(listener!=null){
                    listener.onViewOrderDetail(orderHistoryDTO);
                }
            });
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onViewOrderDetail(orderHistoryDTO);
                }
            });
            String status = orderHistoryDTO.getStatusOrder();
            boolean cancel = StatusOrder.PENDING_APPROVE.getValue().equals(status) || StatusOrder.APPROVE_BY_ADMIN.getValue().equals(status);
            if(cancel){
                binding.llCancelOrderContainer.setVisibility(View.VISIBLE);
                binding.btnCancelOrder.setOnClickListener(v->{
                    if(listener!=null){
                        listener.onCancelOrder(orderHistoryDTO);
                    }
                });
            }else{
                binding.llCancelOrderContainer.setVisibility(View.GONE);
            }
        }
    }
}
