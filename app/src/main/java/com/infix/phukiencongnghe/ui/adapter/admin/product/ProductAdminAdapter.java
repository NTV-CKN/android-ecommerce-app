package com.infix.phukiencongnghe.ui.adapter.admin.product;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.databinding.ItemAdminProductBinding;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProductAdminAdapter extends RecyclerView.Adapter<ProductAdminAdapter.ViewHolder> {
    private final List<ProductAdminPageDTO> productList = new ArrayList<>();
    private final OnProductActionListener actionListener;

    public interface OnProductActionListener {
        //        void onItemClick(ProductAdminPageDTO product);
        void onOptionsClick(ProductAdminPageDTO product);
    }

    public ProductAdminAdapter(OnProductActionListener actionListener) {
        this.actionListener = actionListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAdminProductBinding binding = ItemAdminProductBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(productList.get(position));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<ProductAdminPageDTO> newList) {
        if (newList == null) return;
        this.productList.clear();
        this.productList.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAdminProductBinding binding;
        private final DecimalFormat priceFormatter = new DecimalFormat("#,###đ");

        public ViewHolder(@NonNull ItemAdminProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint({"SetTextI18n", "DefaultLocale"})
        public void bind(ProductAdminPageDTO product) {
            binding.tvProductNameIap.setText(product.getName());
            binding.tvProductCodeIap.setText(
                    binding.getRoot().getContext().getString(R.string.text_code_prefix, "" + product.getId())
            );

            if (product.getMinPrice() != null && product.getMaxPrice() != null) {
                String minPriceStr = priceFormatter.format(product.getMinPrice());
                String maxPriceStr = priceFormatter.format(product.getMaxPrice());
                binding.tvProductPriceRangeIap.setText(String.format("%s - %s", minPriceStr, maxPriceStr));
            } else if (product.getMinPrice() != null && product.getMaxPrice() == null) {
                String minPriceStr = priceFormatter.format(product.getMinPrice());
                binding.tvProductPriceRangeIap.setText(String.format("%s", minPriceStr));
            } else {
                binding.tvProductPriceRangeIap.setText("Chưa cập nhật giá");
            }

            int variantCount = (product.getProductVariantDTOS() != null) ? product.getProductVariantDTOS().size() : 0;
            binding.tvVariantCountIap.setText(String.format("%d biến thể", variantCount));

            int totalStock = (product.getStock() != null) ? product.getStock() : 0;
            binding.tvTotalStockIap.setText(String.format("Kho: %d", totalStock));

            int warranty = (product.getWarrantyPeriod() != null) ? product.getWarrantyPeriod() : 0;
            binding.tvWarrantyMonthsIap.setText(String.format("Bảo hành: %d tháng", warranty));

            Glide.with(binding.ivMainImageIap.getContext())
                    .load(product.getImages() != null && !product.getImages().isEmpty() ? product.getImages().get(0) : null)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.ivMainImageIap);

            binding.ibOptionsIap.setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION && actionListener != null) {
                    actionListener.onOptionsClick(productList.get(position));
                }
            });
        }
    }
}