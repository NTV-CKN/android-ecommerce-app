package com.infix.phukiencongnghe.ui.adapter.admin.voucher;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.VoucherAdminDTO;
import com.infix.phukiencongnghe.data.dto.response.VoucherDTO;
import com.infix.phukiencongnghe.ui.adapter.voucher.VoucherAdapter;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VoucherAdminAdapter extends RecyclerView.Adapter<VoucherAdminAdapter.VoucherAdminViewHolder> {

    private List<VoucherAdminDTO> adminVoucherList = new ArrayList<>();
    private final DecimalFormat currencyFormat = new DecimalFormat("#,###đ");
    private final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    OnVoucherItemClickListener listener;

    public VoucherAdminAdapter(OnVoucherItemClickListener listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setVoucherList(List<VoucherAdminDTO> newList) {
        this.adminVoucherList= newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VoucherAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher_manager_admin, parent, false);
        return new VoucherAdminViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VoucherAdminViewHolder holder, int position) {
        VoucherAdminDTO voucherAdminDTO = adminVoucherList.get(position);
        holder.tvVoucherTitle.setText(voucherAdminDTO.getTitle());

        if(voucherAdminDTO.getStatus() == 1) {
            holder.tvStatus.setText("Hoạt động");
            holder.tvStatus.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            holder.tvStatus.setText("Tạm dừng");
            holder.tvStatus.setTextColor(Color.parseColor("#C62828"));
        }

        holder.tvVoucherCode.setText(voucherAdminDTO.getCode());

        if (voucherAdminDTO.getMinPriceAllow() != 0) {
            holder.tvMinPrice.setText("Đơn tối thiểu: " + currencyFormat.format(voucherAdminDTO.getMinPriceAllow()));
        } else {
            holder.tvMinPrice.setText("Đơn tối thiểu: 0đ");
        }

        if (voucherAdminDTO.getStartDate() != null) {
            String formattedDate = formatVoucherDate(voucherAdminDTO.getStartDate());
            holder.tvEndDate.setText("Từ: " + formattedDate);
        } else {
            holder.tvEndDate.setText("Từ: ");
        }

        if (voucherAdminDTO.getEndDate() != null) {
            String formattedDate = formatVoucherDate(voucherAdminDTO.getEndDate());
            holder.tvEndDate.setText("Đến: " + formattedDate);
        } else {
            holder.tvEndDate.setText("Không giới hạn");
        }

        String progress = "Đã dùng: " + voucherAdminDTO.getUsedCount() + " / " + voucherAdminDTO.getUsageLimit() + " lượt";
        holder.tvUsageProgress.setText(progress);

        if (voucherAdminDTO.getUsedCount() >= voucherAdminDTO.getUsageLimit()) {
            holder.tvUsageProgress.setTextColor(Color.RED);
        }

        holder.iBtnEdit.setOnClickListener(view -> {
            if (listener != null) {
                listener.onEditClick(voucherAdminDTO);
            }
        });

        holder.iBtnDel.setOnClickListener(view -> {
            if (listener != null) {
                listener.onDeleteClick(voucherAdminDTO);
            }
        });
    }

    private String formatVoucherDate(String endDateStr) {
        if (endDateStr == null || endDateStr.isEmpty()) {
            return "";
        }
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(endDateStr);

            return localDateTime.format(outputFormatter);
        } catch (Exception e) {
            e.printStackTrace();
            return endDateStr;
        }
    }

    @Override
    public int getItemCount() {
        return (adminVoucherList != null) ? adminVoucherList.size() : 0;
    }

    public static class VoucherAdminViewHolder extends RecyclerView.ViewHolder {

        TextView tvVoucherTitle, tvStatus, tvVoucherCode, tvMinPrice, tvUsageProgress, tvStartDate, tvEndDate;
        ImageButton iBtnEdit, iBtnDel;

        public VoucherAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvVoucherTitle = itemView.findViewById(R.id.tvVoucherTitle);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvVoucherCode = itemView.findViewById(R.id.tvVoucherCode);
            tvMinPrice = itemView.findViewById(R.id.tvMinPrice);
            tvUsageProgress = itemView.findViewById(R.id.tvUsageProgress);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);

            iBtnEdit = itemView.findViewById(R.id.btnEditVoucher);
            iBtnDel = itemView.findViewById(R.id.btnDeleteVoucher);
        }
    }

}
