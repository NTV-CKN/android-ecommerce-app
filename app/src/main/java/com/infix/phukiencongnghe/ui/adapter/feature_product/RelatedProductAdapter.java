package com.infix.phukiencongnghe.ui.adapter.feature_product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;
import com.infix.phukiencongnghe.data.dto.response.RelatedProductDTO;

import java.util.List;

public class RelatedProductAdapter extends RecyclerView.Adapter<RelatedProductAdapter.RelatedViewHolder> {
        private final List<RelatedProductDTO> relatedList;
        private OnItemClickListener listener;
    public RelatedProductAdapter(List<RelatedProductDTO> relatedList) {
        this.relatedList = relatedList;
    }
    public interface OnItemClickListener {
        void onItemClick(RelatedProductDTO product);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public RelatedProductAdapter.RelatedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_related,parent,false);
        return new RelatedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedProductAdapter.RelatedViewHolder holder, int position) {
        RelatedProductDTO productDTO = relatedList.get(position);
        holder.tvName.setText(productDTO.getName());
        if (productDTO.getMinPrice() != null) {
            holder.tvPrice.setText(String.format("%,.0f VNĐ", productDTO.getMinPrice()));
        } else {
            holder.tvPrice.setText("0 VNĐ");
        }
        if (productDTO.getMainImage() != null && !productDTO.getMainImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(productDTO.getMainImage())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.imgProduct);
        } else {
            holder.imgProduct.setImageResource(android.R.drawable.ic_menu_gallery);
        }
        holder.itemView.setOnClickListener(v -> {
            if(listener!=null && position != RecyclerView.NO_POSITION){
                listener.onItemClick(relatedList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return (relatedList != null) ? relatedList.size() : 0;
    }
    static class RelatedViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProduct;
        TextView tvName, tvPrice;
        public RelatedViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.img_related_product);
            tvName = itemView.findViewById(R.id.tv_related_product_name);
            tvPrice = itemView.findViewById(R.id.tv_related_product_price);
        }
    }
}
