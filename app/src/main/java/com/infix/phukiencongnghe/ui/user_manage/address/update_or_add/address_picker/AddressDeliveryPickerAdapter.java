package com.infix.phukiencongnghe.ui.user_manage.address.update_or_add.address_picker;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.data.model.user_manage.address.AddressSuggestion;
import com.infix.phukiencongnghe.databinding.ItemAddressSuggestionBinding; // Tên file XML của bạn dạng PascalCase + Binding

import java.util.ArrayList;
import java.util.List;

public class AddressDeliveryPickerAdapter extends RecyclerView.Adapter<AddressDeliveryPickerAdapter.ViewHolder> {
    private final List<AddressSuggestion> addressSuggestions = new ArrayList<>();
    private final OnAddressClick onAddressClick;

    public AddressDeliveryPickerAdapter(OnAddressClick onAddressClick) {
        this.onAddressClick = onAddressClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddressSuggestionBinding binding = ItemAddressSuggestionBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(addressSuggestions.get(position));
    }

    @Override
    public int getItemCount() {
        return addressSuggestions.size();
    }

    public void updateList(List<AddressSuggestion> lst) {
        addressSuggestions.clear();
        addressSuggestions.addAll(lst);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemAddressSuggestionBinding binding;

        public ViewHolder(@NonNull ItemAddressSuggestionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            setEventClick();
        }

        private void setEventClick() {
            binding.getRoot().setOnClickListener(v -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onAddressClick.onClick(addressSuggestions.get(position));
                }
            });
        }

        public void bind(AddressSuggestion addressSuggestion) {
            binding.tvTitleAddressSuggestion.setText(addressSuggestion.getTitle());
            binding.tvSubtitleAddressSuggestion.setText(addressSuggestion.getSubtitle());
        }
    }

    public interface OnAddressClick {
        void onClick(AddressSuggestion addressSuggestion);
    }
}