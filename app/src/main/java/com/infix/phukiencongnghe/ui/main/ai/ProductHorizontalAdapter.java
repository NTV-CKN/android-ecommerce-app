package com.infix.phukiencongnghe.ui.main.ai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.ai.ProductChatSummaryDTO;
import com.infix.phukiencongnghe.databinding.ItemFeatureProductMainBinding;
import com.infix.phukiencongnghe.databinding.ItemProductRelatedBinding;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;

public class ProductHorizontalAdapter extends RecyclerView.Adapter<ProductHorizontalAdapter.ProductViewHolder> {

    private final List<ProductChatSummaryDTO> products;
    private final ChatAdapter.OnProductClickListener listener;

    public ProductHorizontalAdapter(List<ProductChatSummaryDTO> products, ChatAdapter.OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductRelatedBinding binding = ItemProductRelatedBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductChatSummaryDTO product = products.get(position);
        holder.bind(product, listener);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductRelatedBinding binding;
        private final Context context;

        public ProductViewHolder(@NonNull ItemProductRelatedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = binding.getRoot().getContext();
        }

        public void bind(ProductChatSummaryDTO product, ChatAdapter.OnProductClickListener listener) {
            binding.tvRelatedProductName.setText(product.getName());

            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            binding.tvRelatedProductPrice.setText(formatter.format(product.getMinPrice()));

            Glide.with(context)
                    .load(product.getMainImageUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.cancel_order_button)
                    .into(binding.imgRelatedProduct);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onProductClick(product);
                }
            });
        }
    }
}
