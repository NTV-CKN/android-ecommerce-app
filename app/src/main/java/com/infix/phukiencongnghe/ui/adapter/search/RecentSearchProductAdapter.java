package com.infix.phukiencongnghe.ui.adapter.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.data.source.local.entity.RecentSearchProductEntity;
import com.infix.phukiencongnghe.databinding.ItemRecentSearchProductBinding;

import java.util.List;
import com.bumptech.glide.Glide;
import java.text.NumberFormat;
import java.util.Locale;

public class RecentSearchProductAdapter extends
        RecyclerView.Adapter<RecentSearchProductAdapter.ViewHolder>{

    public interface OnRecentClick {

        void onClick(
                RecentSearchProductEntity item
        );
    }

    private List<RecentSearchProductEntity> list;

    private OnRecentClick listener;

    public RecentSearchProductAdapter(
            List<RecentSearchProductEntity> list,
            OnRecentClick listener
    ) {
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ItemRecentSearchProductBinding binding;

        public ViewHolder(
                ItemRecentSearchProductBinding binding
        ) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        ItemRecentSearchProductBinding binding =
                ItemRecentSearchProductBinding.inflate(
                        LayoutInflater.from(
                                parent.getContext()
                        ),
                        parent,
                        false
                );

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        RecentSearchProductEntity item =
                list.get(position);

        holder.binding.txtName
                .setText(
                        item.getProductName()
                );

        NumberFormat formatter =
                NumberFormat.getInstance(
                        new Locale("vi", "VN")
                );

        String price =
                formatter.format(item.getPrice()) + " ₫";

        holder.binding.txtPrice
                .setText(price);

        holder.itemView
                .setOnClickListener(v -> {

                    listener.onClick(item);
                });

        Glide.with(holder.itemView.getContext())
                .load(item.getProductImage())
                .into(holder.binding.imgProduct);

    }

    @Override
    public int getItemCount() {

        if(list == null){

            return 0;
        }

        return list.size();
    }
}