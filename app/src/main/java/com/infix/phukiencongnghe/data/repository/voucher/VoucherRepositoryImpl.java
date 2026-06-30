package com.infix.phukiencongnghe.data.repository.voucher;

import com.infix.phukiencongnghe.common.DiscountType;
import com.infix.phukiencongnghe.data.dto.response.VoucherDTO;
import com.infix.phukiencongnghe.data.source.remote.voucher.VoucherService;

import java.util.List;

import retrofit2.Call;

public class VoucherRepositoryImpl implements IVoucherRepository {

    VoucherService voucherService;

    public VoucherRepositoryImpl(VoucherService voucherService) {
        this.voucherService = voucherService;
    }

    @Override
    public Call<List<VoucherDTO>> getVouchers(String typeCode, DiscountType discountType, String keyword) {
        return voucherService.getVouchers(typeCode, discountType, keyword);
    }
}
