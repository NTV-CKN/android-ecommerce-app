package com.infix.phukiencongnghe.ui.adapter.address_manage.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;
import com.infix.phukiencongnghe.databinding.ItemUserAddressBinding;

import java.util.ArrayList;
import java.util.List;

public class UserAddressAdapter extends RecyclerView.Adapter<UserAddressAdapter.ViewHolder> {
    private final List<UserAddressDTO> userAddressDTOS = new ArrayList<>();
    private final OnItemClick onItemClick;
    private final OnRemoveClick onRemoveClick;

    public UserAddressAdapter(
            OnItemClick onItemClick,
            OnRemoveClick onRemoveClick
    ) {
        this.onItemClick = onItemClick;
        this.onRemoveClick = onRemoveClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemUserAddressBinding binding = ItemUserAddressBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(userAddressDTOS.get(position));
    }

    @Override
    public int getItemCount() {
        return userAddressDTOS.size();
    }

    public void updateList(List<UserAddressDTO> lst) {
        userAddressDTOS.clear();
        userAddressDTOS.addAll(lst);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemUserAddressBinding binding;

        public ViewHolder(@NonNull ItemUserAddressBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            setEventClick();
        }

        private void setEventClick() {
            //remove
            binding.btnRemoveItemUserAddress.setOnClickListener(v -> {
                onRemoveClick.onRemove(userAddressDTOS.get(getBindingAdapterPosition()));
            });

            //update
            binding.getRoot().setOnClickListener(v -> {
                onItemClick.onItemClick(userAddressDTOS.get(getBindingAdapterPosition()));
            });
        }

        public void bind(UserAddressDTO userAddressDTO) {
            Context context = binding.getRoot().getContext();
            int isVisible = userAddressDTO.getDefault() ? View.VISIBLE : View.GONE;

            if(userAddressDTO.getDefault())
                binding.btnRemoveItemUserAddress.setVisibility(View.GONE);

            binding.txtItemAddressDefault.setVisibility(isVisible);
            binding.txtItemAddressDetail.setText(context.getString(R.string.txt_address_detail_args, userAddressDTO.getAddressDetail()));
            binding.txtItemAddressPhone.setText(context.getString(R.string.txt_phone_number_args, userAddressDTO.getPhoneNumber()));
            binding.txtItemAddressReceiverName.setText(context.getString(R.string.txt_name_receiver_args, userAddressDTO.getReceiverName()));
            binding.txtItemAddressProvince.setText(context.getString(R.string.txt_province_city_args, userAddressDTO.getProvinceCity()));
        }
    }


   public interface OnItemClick {
        void onItemClick(UserAddressDTO userAddressDTO);
    }

    public interface OnRemoveClick {
        void onRemove(UserAddressDTO userAddressDTO);
    }
}
