package com.infix.phukiencongnghe.ui.adapter.slider_show;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.infix.phukiencongnghe.data.dto.response.SliderShowDTO;

import java.util.List;

public class SliderShowAdapter extends RecyclerView.Adapter<SliderShowAdapter.SliderShowViewHolder> {

    private List<SliderShowDTO> urlList;

    public SliderShowAdapter(List<SliderShowDTO> urlList) {
        this.urlList = urlList;
    }

    @NonNull
    @Override
    public SliderShowAdapter.SliderShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ImageView imgView = new ImageView(parent.getContext());
        imgView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return new SliderShowViewHolder(imgView);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderShowViewHolder holder, int position) {
        SliderShowDTO currentSlider = urlList.get(position);
        String imageUrl = currentSlider.getUrlImage();
        Glide.with(holder.imageView.getContext()).load(imageUrl).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return urlList != null ? urlList.size() : 0;
    }

    public void setData(List<SliderShowDTO> newSliderList) {
        this.urlList = newSliderList;
        notifyDataSetChanged();
    }

    static class SliderShowViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public SliderShowViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView;
        }
    }
}
