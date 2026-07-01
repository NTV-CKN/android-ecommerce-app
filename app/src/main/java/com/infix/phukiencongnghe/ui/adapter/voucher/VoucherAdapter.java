package com.infix.phukiencongnghe.ui.adapter.voucher;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.VoucherDTO;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private List<VoucherDTO> voucherList = new ArrayList<>();
    private final DecimalFormat currencyFormat = new DecimalFormat("#,###đ");
    private final DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private OnItemClickListener onItemClickListener;
    private boolean isApplyMode = false;
    public interface OnItemClickListener {
        void onItemClick(VoucherDTO voucher);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public void setApplyMode(boolean isApplyMode) {
        this.isApplyMode = isApplyMode;
        notifyDataSetChanged();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setVoucherList(List<VoucherDTO> newList) {
        this.voucherList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (voucherList != null) ? voucherList.size() : 0;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        VoucherDTO voucherDTO = voucherList.get(position);

        holder.tvTitle.setText(voucherDTO.getTitle());

        holder.tvCode.setText("Mã: " + voucherDTO.getCode());

        if (voucherDTO.getMinPriceAllow() != 0) {
            holder.tvMinPrice.setText("Đơn tối thiểu: " + currencyFormat.format(voucherDTO.getMinPriceAllow()));
        } else {
            holder.tvMinPrice.setText("Đơn tối thiểu: 0đ");
        }

        if (voucherDTO.getEndDate() != null) {
            String formattedDate = formatVoucherDate(voucherDTO.getEndDate());
            holder.tvEndDate.setText("HSD: " + formattedDate);
        } else {
            holder.tvEndDate.setText("HSD: Không giới hạn");
        }
        if (isApplyMode) {
            holder.btnCopy.setText("Áp dụng");
        } else {
            holder.btnCopy.setText("Sao chép");
        }
        holder.btnCopy.setOnClickListener(v -> {
            if (isApplyMode) {
                if (onItemClickListener != null) onItemClickListener.onItemClick(voucherDTO);
            }else {
                Context context = v.getContext();

                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Voucher Code", voucherDTO.getCode());

                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                    SnackbarUtils.showBaseSnackbar(v, "Đã sao chép mã: " + voucherDTO.getCode(), Snackbar.LENGTH_SHORT);
                }
            }
        });
    }

    private String formatVoucherDate(String endDateStr) {
        if (endDateStr == null || endDateStr.isEmpty()) {
            return "";
        }
        try {
            // Bước 1: Parse chuỗi gốc "2026-12-31T23:59:59" về lại đối tượng LocalDateTime
            LocalDateTime localDateTime = LocalDateTime.parse(endDateStr);

            // Bước 2: Dùng formatter của bạn để chuyển thành "31/12/2026"
            return localDateTime.format(outputFormatter);
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu lỡ server trả về sai định dạng hoàn toàn, trả về chuỗi gốc để app không bị crash
            return endDateStr;
        }
    }
    public static class VoucherViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvCode, tvMinPrice, tvEndDate;
        Button btnCopy;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvVoucherTitle);
            tvCode = itemView.findViewById(R.id.tvVoucherCode);
            tvMinPrice = itemView.findViewById(R.id.tvMinPrice);
            tvEndDate = itemView.findViewById(R.id.tvEndDate);
            btnCopy = itemView.findViewById(R.id.btnCopyCode);
        }
    }
}