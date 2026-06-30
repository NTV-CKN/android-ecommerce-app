package com.infix.phukiencongnghe.ui.adapter.order;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.infix.phukiencongnghe.data.dto.response.OrderDetailsHistoryDTO;
import com.infix.phukiencongnghe.databinding.ItemOrderDetailBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    private List<OrderDetailsHistoryDTO> details = new ArrayList<>();
    private final DecimalFormat formatter = new DecimalFormat("#,###đ");
    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<OrderDetailsHistoryDTO> list) {
        this.details = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderDetailBinding binding = ItemOrderDetailBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new OrderDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        holder.bind(details.get(position));
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    static class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        private final ItemOrderDetailBinding binding;
        private final DecimalFormat formatter = new DecimalFormat("#,###đ");

        public OrderDetailViewHolder(@NonNull ItemOrderDetailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(OrderDetailsHistoryDTO detail) {
            binding.tvProductName.setText(detail.getProductVariantName());

            binding.tvQuantity.setText("x" + detail.getQuantity());

            binding.tvPrice.setText(formatter.format(detail.getTotalPrice()));
            String imageUrl = detail.getProductVariantImage();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(binding.getRoot().getContext())
                        .load(imageUrl)
                        .placeholder(android.R.drawable.ic_menu_gallery)
                        .error(android.R.drawable.ic_menu_report_image)
                        .into(binding.ivProductImage);
            } else {
                binding.ivProductImage.setImageResource(android.R.drawable.ic_menu_gallery);
            }
        }
    }
}