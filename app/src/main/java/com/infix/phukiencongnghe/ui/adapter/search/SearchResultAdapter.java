package com.infix.phukiencongnghe.ui.adapter.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;
import com.infix.phukiencongnghe.databinding.ItemSearchResultBinding;

import java.util.List;
import com.bumptech.glide.Glide;
import java.text.NumberFormat;
import java.util.Locale;

public class SearchResultAdapter extends
        RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{

    public interface OnItemClick {
        void onClick(FeatureProductDTO product);
    }

    private List<FeatureProductDTO> list;
    private OnItemClick listener;

    public SearchResultAdapter(
            List<FeatureProductDTO> list,
            OnItemClick listener
    ) {
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ItemSearchResultBinding binding;

        public ViewHolder(
                ItemSearchResultBinding binding
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

        ItemSearchResultBinding binding =
                ItemSearchResultBinding.inflate(
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

        FeatureProductDTO item =
                list.get(position);

        holder.binding.txtName
                .setText(item.getName());

        // FIX NULL PRICE

        if(item.getMinPrice() != null){

            NumberFormat formatter =
                    NumberFormat.getInstance(
                            new Locale("vi", "VN")
                    );

            String price =
                    formatter.format(
                            item.getMinPrice()
                    ) + " ₫";

            holder.binding.txtPrice
                    .setText(price);

        } else {

            holder.binding.txtPrice
                    .setText("0");

        }

        Glide.with(holder.itemView.getContext())
                .load(item.getMainImage())
                .into(holder.binding.imgProduct);

        holder.itemView
                .setOnClickListener(v -> {

                    listener.onClick(item);
                });
    }

    @Override
    public int getItemCount() {

        if(list == null){

            return 0;
        }

        return list.size();
    }
}