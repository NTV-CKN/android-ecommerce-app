package com.infix.phukiencongnghe.ui.adapter.feature_product;

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
import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;

import java.math.BigDecimal;
import java.util.List;

public class FeatureProductAdapter extends RecyclerView.Adapter<FeatureProductAdapter.FeatureProductViewHolder> {

    private List<FeatureProductDTO> featureProductDTOList;
    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(FeatureProductDTO product);
    }

//    public FeatureProductAdapter(List<FeatureProductDTO> featureProductDTOList) {this.featureProductDTOList = featureProductDTOList;
//    }

    public FeatureProductAdapter(List<FeatureProductDTO> featureProductDTOList) {
        this.featureProductDTOList = featureProductDTOList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<FeatureProductDTO> newList) {
        this.featureProductDTOList = newList;
        notifyDataSetChanged();
    }
//    public void setData(List<FeatureProductDTO> newList) {
//        this.featureProductDTOList = newList;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public FeatureProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feature_product_main, parent, false);
        return new FeatureProductViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FeatureProductViewHolder holder, int position) {
        FeatureProductDTO productDTO = featureProductDTOList.get(position);
        holder.tvName.setText(productDTO.getName() + productDTO.getSubtitle());

        if(productDTO.getMinPrice() != null) {
            holder.tvPrice.setText(formatPrice(productDTO.getMinPrice()) + " VNĐ");
        } else {
            holder.tvPrice.setText("0.0");
        }

        if (productDTO.getAvgStar() != null) {
            holder.tvAvgStar.setText(String.format("%.1f", productDTO.getAvgStar()));
        } else {
            holder.tvAvgStar.setText("0.0");
        }

        Glide.with(holder.imgView.getContext())
                .load(productDTO.getMainImage())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgView);

        holder.itemView.setOnClickListener(v -> {
            int adapterPosition =
                    holder.getAdapterPosition();
            if(listener != null
                    && adapterPosition != RecyclerView.NO_POSITION){

                listener.onItemClick(
                        featureProductDTOList.get(
                                adapterPosition
                        )
                );
            }
        });
    }

    private String formatPrice(BigDecimal price) {
        return String.format("%,.0f", price);
    }

    @Override
    public int getItemCount() {
        return (featureProductDTOList != null) ? featureProductDTOList.size() : 0;
    }

    static class FeatureProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgView;
        TextView tvName, tvPrice, tvAvgStar;
        public FeatureProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgProduct);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvAvgStar = itemView.findViewById(R.id.tvProductStar);
        }
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
