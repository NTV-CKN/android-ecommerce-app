package com.infix.phukiencongnghe.ui.user_manage.address.update_or_add;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.infix.phukiencongnghe.data.dto.response.ShipFeeByAddressDTO;

import java.util.List;

public class ShipFeeSpinnerAdapter extends ArrayAdapter<ShipFeeByAddressDTO> {

    public ShipFeeSpinnerAdapter(@NonNull Context context, List<ShipFeeByAddressDTO> list) {
        super(context, android.R.layout.simple_spinner_item, list);
        setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        ShipFeeByAddressDTO item = getItem(position);
        if (item != null) {
            label.setText(item.getProvinceCity());
        }
        return label;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        ShipFeeByAddressDTO item = getItem(position);
        if (item != null) {
            label.setText(item.getProvinceCity());
        }
        return label;
    }
}