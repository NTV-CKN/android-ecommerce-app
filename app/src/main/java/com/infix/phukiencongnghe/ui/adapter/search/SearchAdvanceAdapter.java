package com.infix.phukiencongnghe.ui.adapter.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;
import com.infix.phukiencongnghe.databinding.ItemSearchAdvanceBinding;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class SearchAdvanceAdapter extends
        RecyclerView.Adapter<SearchAdvanceAdapter.ViewHolder> {

    public interface OnItemClick {
        void onClick(FeatureProductDTO product);
    }

    private List<FeatureProductDTO> list;
    private OnItemClick listener;

    public SearchAdvanceAdapter(
            List<FeatureProductDTO> list,
            OnItemClick listener
    ) {
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ItemSearchAdvanceBinding binding;

        public ViewHolder(
                ItemSearchAdvanceBinding binding
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

        ItemSearchAdvanceBinding binding =
                ItemSearchAdvanceBinding.inflate(
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


        // PRICE

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


        // RATING

        if(item.getAvgStar() != null){

            holder.binding.txtRating
                    .setText(
                            "★ " +
                                    String.format(
                                            "%.1f",
                                            item.getAvgStar()
                                    )
                    );

        } else {

            holder.binding.txtRating
                    .setText(
                            "★ 0.0"
                    );
        }


        // IMAGE

        Glide.with(holder.itemView.getContext())
                .load(item.getMainImage())
                .into(holder.binding.imgProduct);


        holder.itemView.setOnClickListener(
                v -> listener.onClick(item)
        );
    }

    @Override
    public int getItemCount() {

        return list == null
                ? 0
                : list.size();
    }
}