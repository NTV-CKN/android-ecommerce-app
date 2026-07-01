package com.infix.phukiencongnghe.data.repository.admin.voucher;

import com.infix.phukiencongnghe.common.DiscountType;
import com.infix.phukiencongnghe.data.dto.request.VoucherReqDTO;
import com.infix.phukiencongnghe.data.dto.response.VoucherAdminDTO;
import com.infix.phukiencongnghe.data.source.remote.admin.voucher.AdminVoucherService;

import java.util.List;

import retrofit2.Call;

public class AdminVoucherRepositoryImpl implements IAdminVoucherRepository {

    AdminVoucherService adminVoucherService;

    public AdminVoucherRepositoryImpl(AdminVoucherService adminVoucherService) {
        this.adminVoucherService = adminVoucherService;
    }

    @Override
    public Call<List<VoucherAdminDTO>> getVoucher(String typeCode, DiscountType discountType, String keyword) {
        return adminVoucherService.getVoucher(typeCode, discountType, keyword);
    }

    @Override
    public Call<Void> createVoucher(VoucherReqDTO req) {
        return adminVoucherService.createVoucher(req);
    }

    @Override
    public Call<Void> updateVoucher(Integer id, VoucherReqDTO req) {
        return adminVoucherService.updateVoucher(id, req);
    }

    @Override
    public Call<Void> deleteVoucher(Integer id) {
        return adminVoucherService.deleteVoucher(id);
    }
}
