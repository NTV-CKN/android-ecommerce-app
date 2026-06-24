package com.infix.phukiencongnghe.ui.adapter.search;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.data.source.local.entity.SearchKeywordEntity;
import com.infix.phukiencongnghe.databinding.ItemSearchKeywordBinding;

import java.util.List;

public class SearchKeywordAdapter extends
        RecyclerView.Adapter<SearchKeywordAdapter.ViewHolder> {

    public interface OnKeywordClick {

        void onClick(String keyword);
    }

    private List<SearchKeywordEntity> list;

    private OnKeywordClick listener;

    public SearchKeywordAdapter(
            List<SearchKeywordEntity> list,
            OnKeywordClick listener
    ) {
        this.list = list;
        this.listener = listener;
    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        ItemSearchKeywordBinding binding;

        public ViewHolder(
                ItemSearchKeywordBinding binding
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

        ItemSearchKeywordBinding binding =
                ItemSearchKeywordBinding.inflate(
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

        holder.binding.txtKeyword
                .setText(
                        list.get(position)
                                .getKeyword()
                );

        holder.itemView
                .setOnClickListener(v -> {

                    listener.onClick(
                            list.get(position)
                                    .getKeyword()
                    );
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