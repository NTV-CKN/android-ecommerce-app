package com.infix.phukiencongnghe.ui.admin.order.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.data.dto.response.OrderItemDTO;
import com.infix.phukiencongnghe.databinding.ItemOrderDetailProductBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderItemAdapter
        extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {

    private List<OrderItemDTO> items =
            new ArrayList<>();

    public void setItems(
            List<OrderItemDTO> items
    ) {

        this.items = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        ItemOrderDetailProductBinding binding =
                ItemOrderDetailProductBinding.inflate(
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

        OrderItemDTO item =
                items.get(position);

        holder.binding.tvProductName.setText(
                item.getProductName()
        );

        holder.binding.tvQuantity.setText(
                "SL: " + item.getQuantity()
        );

        holder.binding.tvPrice.setText(
                String.format(
                        "%.0f VNĐ",
                        item.getPrice()
                )
        );
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        ItemOrderDetailProductBinding binding;

        public ViewHolder(
                ItemOrderDetailProductBinding binding
        ) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}