package com.infix.phukiencongnghe.ui.adapter.feature_product;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ProductImageSliderAdapter extends RecyclerView.Adapter<ProductImageSliderAdapter.SliderViewHolder> {
    private List<String> imageList = new ArrayList<>();

    public void setImages(List<String> images){
        if(images != null){
            this.imageList =images;
            notifyDataSetChanged();
        }
    }
    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(parent.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new SliderViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        String imageUrl = imageList.get(position);

        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_delete)
                .into((ImageView) holder.itemView);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder{

        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
