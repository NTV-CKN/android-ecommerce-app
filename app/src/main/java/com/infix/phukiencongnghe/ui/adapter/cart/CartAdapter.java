package com.infix.phukiencongnghe.ui.adapter.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

// Nếu bạn dùng Glide hoặc Picasso để load ảnh thì import vào
import com.bumptech.glide.Glide;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.CartItemDTO;
import com.infix.phukiencongnghe.ui.cart.OnCartItemClickListener;

public class CartAdapter extends ListAdapter<CartItemDTO, CartAdapter.CartViewHolder> {

    private final OnCartItemClickListener listener;

    private static final DiffUtil.ItemCallback<CartItemDTO> DIFF_CALLBACK = new DiffUtil.ItemCallback<CartItemDTO>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartItemDTO oldItem, @NonNull CartItemDTO newItem) {
            // Kiểm tra xem có phải cùng 1 sản phẩm không (dựa vào ID)
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItemDTO oldItem, @NonNull CartItemDTO newItem) {
            // Nếu cùng 1 sản phẩm, kiểm tra xem số lượng hoặc giá có thay đổi không
            return oldItem.getQuantity().equals(newItem.getQuantity()) &&
                    oldItem.getTotalPrice().equals(newItem.getTotalPrice());
        }
    };

    public CartAdapter(OnCartItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp giao diện item_cart.xml cho từng dòng
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemDTO currentItem = getItem(position);
        holder.bind(currentItem, listener);
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView tvProductName, tvProductVariant, tvProductPrice;
        private EditText edtQuantity;
        private TextView btnPlus, btnMinus, btnDelete;
        private ImageView imgProduct;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductVariant = itemView.findViewById(R.id.tvProductVariant);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            edtQuantity = itemView.findViewById(R.id.edtQuantity);

            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }

        public void bind(CartItemDTO item, OnCartItemClickListener listener) {
            // 1. Tên sản phẩm
            tvProductName.setText(item.getProductName());

            if (item.getProductVariantName() != null && !item.getProductVariantName().isEmpty()) {
                tvProductVariant.setVisibility(View.VISIBLE);
                tvProductVariant.setText(item.getProductVariantName());
            } else {
                tvProductVariant.setVisibility(View.GONE);
            }

            tvProductPrice.setText(String.format("%,.0fđ", item.getUnitPrice()));

            edtQuantity.setText(String.valueOf(item.getQuantity()));

            Glide.with(itemView.getContext())
                    .load(item.getProductImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imgProduct);

            // Bắt sự kiện Click chuyển ra Fragment
            btnPlus.setOnClickListener(v -> listener.onPlusClick(item.getId(), item.getQuantity()));
            btnMinus.setOnClickListener(v -> listener.onMinusClick(item.getId(), item.getQuantity()));
            btnDelete.setOnClickListener(v -> listener.onDeleteClick(item.getId()));
            edtQuantity.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                    String input = edtQuantity.getText().toString().trim();

                    try {
                        int newQty = Integer.parseInt(input);
                        if (newQty < 1) newQty = 1;

                        listener.onQuantityTextChanged(item.getId(), newQty);

                    } catch (NumberFormatException e) {
                        edtQuantity.setText(String.valueOf(item.getQuantity()));
                    }

                    android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager)
                            v.getContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    edtQuantity.clearFocus();
                    return true;
                }
                return false;
            });
        }
    }
}