package com.infix.phukiencongnghe.data.repository.admin.voucher;

import com.infix.phukiencongnghe.common.DiscountType;
import com.infix.phukiencongnghe.data.dto.request.VoucherReqDTO;
import com.infix.phukiencongnghe.data.dto.response.VoucherAdminDTO;

import java.util.List;

import retrofit2.Call;

public interface IAdminVoucherRepository {

    Call<List<VoucherAdminDTO>> getVoucher(String typeCode, DiscountType discountType, String keyword);
    Call<Void> createVoucher(VoucherReqDTO req);
    Call<Void> updateVoucher(Integer id, VoucherReqDTO req);
    Call<Void> deleteVoucher(Integer id);

}
