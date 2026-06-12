package com.infix.phukiencongnghe.ui.adapter.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryDTO> categoryList;

    public CategoryAdapter(List<CategoryDTO> categoryList) {
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category_main, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return categoryList != null ? categoryList.size() : 0;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        CategoryDTO item = categoryList.get(position);
        holder.tvCategoryName.setText(item.getName());

        int iconRes = getIconByCategoryName(item.getName());
        holder.imgCategoryIcon.setImageResource(iconRes);
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategoryIcon;
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategoryIcon = itemView.findViewById(R.id.imgCategory);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }

    private int getIconByCategoryName(String name) {
        if (name == null) return android.R.drawable.ic_menu_compass;
        String lower = name.toLowerCase();
        switch (lower) {
            case "phụ kiện máy tính":
                return R.drawable.ic_category_pkmt;
            case "phụ kiện điện thoại":
                return R.drawable.ic_category_pkdt;
            case "dây cáp tín hiệu":
                return R.drawable.ic_category_dcth;
            case "bộ chia tín hiệu":
                return R.drawable.ic_category_bcth;
            case "phụ kiện xe":
                return R.drawable.ic_category_pkx;
            case "thiết bị mạng":
                return R.drawable.ic_category_tbm;
            case "thiết bị ngoại vi":
                return R.drawable.ic_category_tbnv;
            case "bộ chuyển đổi tín hiệu":
                return R.drawable.ic_category_bcdth;
        }
        return android.R.drawable.ic_menu_compass;
    }

}
