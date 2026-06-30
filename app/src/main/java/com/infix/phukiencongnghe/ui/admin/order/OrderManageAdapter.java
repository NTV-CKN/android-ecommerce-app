package com.infix.phukiencongnghe.ui.admin.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.data.dto.response.OrderManageDTO;
import com.infix.phukiencongnghe.databinding.ItemOrderManageBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderManageAdapter
        extends RecyclerView.Adapter<OrderManageAdapter.ViewHolder> {

    private List<OrderManageDTO> orders =
            new ArrayList<>();

    public interface OnOrderActionListener {

        void onOrderClick(
                OrderManageDTO order
        );

        void onChangeStatusClick(
                OrderManageDTO order
        );
    }

    private final OnOrderActionListener listener;

    public OrderManageAdapter(
            OnOrderActionListener listener
    ) {
        this.listener = listener;
    }

    public void setOrders(
            List<OrderManageDTO> orders
    ) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        ItemOrderManageBinding binding =
                ItemOrderManageBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        OrderManageDTO order =
                orders.get(position);

        holder.binding.tvOrderId.setText(
                "Order #" + order.getOrderId()
        );

        holder.binding.tvCustomer.setText(
                order.getCustomerName()
        );

        holder.binding.tvTotal.setText(
                String.valueOf(order.getTotalPrice())
        );

        holder.binding.tvStatus.setText(
                order.getStatus()
        );

        holder.binding.tvDate.setText(
                order.getOrderDate()
        );

        holder.binding.btnChangeStatus.setOnClickListener(
                v -> listener.onChangeStatusClick(order)
        );
        holder.itemView.setOnClickListener(
                v -> listener.onOrderClick(order)
        );
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        ItemOrderManageBinding binding;

        public ViewHolder(
                ItemOrderManageBinding binding
        ) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}