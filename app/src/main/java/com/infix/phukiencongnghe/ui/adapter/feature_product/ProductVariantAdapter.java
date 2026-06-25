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
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;

import java.util.List;

public class ProductVariantAdapter extends RecyclerView.Adapter<ProductVariantAdapter.ProductVariantViewHolder> {
    private List<ProductVariantDTO> variantDTOList;
    private int selected = 0;
    private OnItemClickListener listener;

    public ProductVariantAdapter(List<ProductVariantDTO> variantDTOList, OnItemClickListener listener) {
        this.variantDTOList = variantDTOList;
        this.listener = listener;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<ProductVariantDTO> newList){
        this.variantDTOList = newList;
        this.selected = 0;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ProductVariantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_variant,parent,false);
        return new ProductVariantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVariantViewHolder holder, int position) {
        ProductVariantDTO variantDTO = variantDTOList.get(position);
        holder.tvVariantName.setText(variantDTO.getName());
        Glide.with(holder.imgVariant.getContext())
                .load(variantDTO.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgVariant);
        if(position == selected){
            holder.itemView.setAlpha(1.0f);
        }else{
            holder.itemView.setAlpha(0.4f);
        }
        holder.itemView.setOnClickListener(v -> {
            int preSelected = selected;
            selected = holder.getBindingAdapterPosition();
            notifyItemChanged(preSelected);
            notifyItemChanged(selected);
            if(listener!=null){
                listener.onItemClick(variantDTO);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (variantDTOList !=null){
         return variantDTOList.size();
        }
        return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(ProductVariantDTO variant);
    }
    static class ProductVariantViewHolder extends RecyclerView.ViewHolder {
        ImageView imgVariant;
        TextView tvVariantName;

        public ProductVariantViewHolder(@NonNull View itemView) {
            super(itemView);
            imgVariant = itemView.findViewById(R.id.img_variant);
            tvVariantName = itemView.findViewById(R.id.tv_variant_name);
        }
    }
}
