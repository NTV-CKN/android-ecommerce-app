package com.infix.phukiencongnghe.ui.adapter.order;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.data.dto.response.OrderHistoryDTO;
import com.infix.phukiencongnghe.databinding.ItemOrderHistoryBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {
    private List<OrderHistoryDTO> orderHistoryDTOList = new ArrayList<>();
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
        holder.bind(orderHistoryDTOList.get(position));
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

        public void bind(OrderHistoryDTO orderHistoryDTO) {
            binding.tvItemOrderId.setText("Mã đơn: #" + orderHistoryDTO.getId());
            binding.tvItemOrderStatus.setText(orderHistoryDTO.getStatusOrder());
            binding.tvItemOrderDate.setText("Ngày đặt: " + orderHistoryDTO.getOrderDate().replace("T", " "));
            binding.tvItemReceiver.setText("Người nhận: " + orderHistoryDTO.getReceiverName());
            binding.tvItemTotalPay.setText(formatter.format(orderHistoryDTO.getTotalMustPay()));
        }
    }
}
