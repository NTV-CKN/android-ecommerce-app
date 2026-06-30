package com.infix.phukiencongnghe.ui.admin.product.add_or_update;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;
import com.infix.phukiencongnghe.databinding.ItemInputVariantsBinding;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VariantInputAdapter extends RecyclerView.Adapter<VariantInputAdapter.ViewHolder> {

    private final List<ProductVariantDTO> variants = new ArrayList<>();
    private final OnVariantActionListener listener;
    private boolean isUpdateMode = false;

    public interface OnVariantActionListener {
        void onDelete(int position);
        void onSelectImage(int position, ProductVariantDTO item);
        void onGenerateSkuRequested(int position, ProductVariantDTO item);
    }

    public VariantInputAdapter(OnVariantActionListener listener) {
        this.listener = listener;
    }

    public void triggerNotifyItemChanged(int pos) {
        notifyItemChanged(pos);
    }

    public void updateList(List<ProductVariantDTO> newList, boolean isUpdateMode) {
        this.isUpdateMode = isUpdateMode;
        variants.clear();
        if (newList != null) {
            variants.addAll(newList);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInputVariantsBinding binding = ItemInputVariantsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(variants.get(position));
    }

    @Override
    public int getItemCount() {
        return variants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemInputVariantsBinding binding;
        private CustomTextWatcher skuWatcher, nameWatcher, priceWatcher, stockWatcher, colorWatcher, sizeWatcher, gramWatcher;

        public ViewHolder(@NonNull ItemInputVariantsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductVariantDTO item) {
            removeTextWatchers();

            binding.edtSku.setText(item.getSku());
            binding.edtName.setText(item.getName());
            binding.edtPrice.setText(item.getPrice() != null ? item.getPrice().toPlainString() : "");
            binding.edtStock.setText(item.getStock() != null ? String.valueOf(item.getStock()) : "0");
            binding.edtColor.setText(item.getColor());
            binding.edtSize.setText(item.getSize());
            binding.edtGram.setText(item.getGram() != null ? String.valueOf(item.getGram()) : "0");

            if (isUpdateMode) {
                binding.btnDeleteVariant.setVisibility(View.GONE);
            } else {
                binding.btnDeleteVariant.setVisibility(View.VISIBLE);
            }

            Glide.with(binding.ivVariantPhoto.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.ic_add_24px)
                    .into(binding.ivVariantPhoto);

            initTextWatchers(item);

            binding.ivVariantPhoto.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onSelectImage(pos, variants.get(pos));
                }
            });

            binding.tlSku.setEndIconOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onGenerateSkuRequested(pos, variants.get(pos));
                }
            });

            binding.btnDeleteVariant.setOnClickListener(v -> {
                int pos = getBindingAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && listener != null) {
                    listener.onDelete(pos);
                }
            });
        }

        private void initTextWatchers(ProductVariantDTO item) {
            skuWatcher = new CustomTextWatcher(s -> item.setSku(s.trim()));
            nameWatcher = new CustomTextWatcher(s -> item.setName(s.trim()));
            priceWatcher = new CustomTextWatcher(s -> {
                try { item.setPrice(new BigDecimal(s)); } catch (Exception e) { item.setPrice(BigDecimal.ZERO); }
            });
            stockWatcher = new CustomTextWatcher(s -> {
                try { item.setStock(Integer.parseInt(s)); } catch (Exception e) { item.setStock(0); }
            });
            colorWatcher = new CustomTextWatcher(s -> item.setColor(s.trim()));
            sizeWatcher = new CustomTextWatcher(s -> item.setSize(s.trim()));
            gramWatcher = new CustomTextWatcher(s -> {
                try { item.setGram(Integer.parseInt(s)); } catch (Exception e) { item.setGram(0); }
            });

            binding.edtName.addTextChangedListener(nameWatcher);
            binding.edtPrice.addTextChangedListener(priceWatcher);
            binding.edtStock.addTextChangedListener(stockWatcher);
            binding.edtColor.addTextChangedListener(colorWatcher);
            binding.edtSize.addTextChangedListener(sizeWatcher);
            binding.edtGram.addTextChangedListener(gramWatcher);
        }

        private void removeTextWatchers() {
            if (nameWatcher != null) binding.edtName.removeTextChangedListener(nameWatcher);
            if (priceWatcher != null) binding.edtPrice.removeTextChangedListener(priceWatcher);
            if (stockWatcher != null) binding.edtStock.removeTextChangedListener(stockWatcher);
            if (colorWatcher != null) binding.edtColor.removeTextChangedListener(colorWatcher);
            if (sizeWatcher != null) binding.edtSize.removeTextChangedListener(sizeWatcher);
            if (gramWatcher != null) binding.edtGram.removeTextChangedListener(gramWatcher);
        }
    }

    private static class CustomTextWatcher implements TextWatcher {
        private final OnTextChangedListener listener;
        public interface OnTextChangedListener { void onTextChanged(String s); }

        public CustomTextWatcher(OnTextChangedListener listener) { this.listener = listener; }
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override public void afterTextChanged(Editable s) { listener.onTextChanged(s.toString()); }
    }
}