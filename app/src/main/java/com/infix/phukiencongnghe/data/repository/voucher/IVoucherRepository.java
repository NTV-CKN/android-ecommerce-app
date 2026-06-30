package com.infix.phukiencongnghe.data.repository.voucher;

import com.infix.phukiencongnghe.common.DiscountType;
import com.infix.phukiencongnghe.data.dto.response.VoucherDTO;

import java.util.List;

import retrofit2.Call;

public interface IVoucherRepository {
    Call<List<VoucherDTO>> getVouchers(String typeCode, DiscountType discountType, String keyword);
}
