package com.infix.phukiencongnghe.ui.adapter.admin.voucher;

import com.infix.phukiencongnghe.data.dto.response.VoucherAdminDTO;

public interface OnVoucherItemClickListener {
    void onEditClick(VoucherAdminDTO req);
    void onDeleteClick(VoucherAdminDTO req);
}
