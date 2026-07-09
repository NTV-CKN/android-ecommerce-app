package com.infix.phukiencongnghe.ui.main.ai;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.data.dto.response.ai.ProductChatSummaryDTO;
import com.infix.phukiencongnghe.data.model.MessageModel;
import com.infix.phukiencongnghe.databinding.ItemChatBotBinding;
import com.infix.phukiencongnghe.databinding.ItemChatUserBinding;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(ProductChatSummaryDTO product);
    }

    private final List<MessageModel> messageList = new ArrayList<>();
    private final OnProductClickListener onProductClickListener;

    public ChatAdapter(OnProductClickListener listener) {
        this.onProductClickListener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMessages(List<MessageModel> messages) {
        this.messageList.clear();
        if (messages != null) {
            this.messageList.addAll(messages);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == MessageModel.TYPE_USER) {
            ItemChatUserBinding binding = ItemChatUserBinding.inflate(inflater, parent, false);
            return new UserViewHolder(binding);
        } else {
            ItemChatBotBinding binding = ItemChatBotBinding.inflate(inflater, parent, false);
            return new BotViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageModel message = messageList.get(position);

        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder).bind(message);
        } else if (holder instanceof BotViewHolder) {
            ((BotViewHolder) holder).bind(message, onProductClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatUserBinding binding;

        public UserViewHolder(@NonNull ItemChatUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(MessageModel message) {
            binding.txtMessageUser.setText(message.getText());
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatBotBinding binding;
        private final Context context;

        public BotViewHolder(@NonNull ItemChatBotBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.context = binding.getRoot().getContext();
        }

        public void bind(MessageModel message, OnProductClickListener listener) {
            binding.txtMessageBot.setText(message.getText());

            if (message.getProducts() != null && !message.getProducts().isEmpty()) {
                binding.recyclerViewBotProducts.setVisibility(View.VISIBLE);

                binding.recyclerViewBotProducts.setLayoutManager(
                        new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                );

                ProductHorizontalAdapter productAdapter =
                        new ProductHorizontalAdapter(message.getProducts(), listener);
                binding.recyclerViewBotProducts.setAdapter(productAdapter);
            } else {
                binding.recyclerViewBotProducts.setVisibility(View.GONE);
            }
        }
    }
}