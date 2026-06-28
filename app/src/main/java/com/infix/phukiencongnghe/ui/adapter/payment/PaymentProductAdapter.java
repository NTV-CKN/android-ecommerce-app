package com.infix.phukiencongnghe.ui.adapter.payment;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.CheckoutProductDTO;
import java.text.DecimalFormat;
import java.util.List;

public class PaymentProductAdapter extends RecyclerView.Adapter<PaymentProductAdapter.PaymentProductViewHolder> {
    private List<CheckoutProductDTO> mList; // Thay đổi kiểu dữ liệu ở đây
    public PaymentProductAdapter(List<CheckoutProductDTO> list) {
        this.mList = list;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<CheckoutProductDTO> newList) {
        this.mList = newList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public PaymentProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_product, parent, false);
        return new PaymentProductViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull PaymentProductViewHolder holder, int position) {
        CheckoutProductDTO item = mList.get(position);
        if (item == null) return;
        DecimalFormat format = new DecimalFormat("#,###đ");
        holder.tvProductName.setText(item.getProductName());

        if (item.getPrice() != null) {
            holder.tvProductPrice.setText(format.format(item.getPrice()));
        } else {
            holder.tvProductPrice.setText("0đ");
        }
        holder.tvProductQuantity.setText("x" + item.getQuantity());
        Glide.with(holder.imgProduct.getContext())
                .load(item.getImgUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgProduct);
    }
    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }
    static class PaymentProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView tvProductName;
        TextView tvProductPrice;
        TextView tvProductQuantity;

        public PaymentProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvProductQuantity = itemView.findViewById(R.id.tv_product_quantity);
        }
    }
}