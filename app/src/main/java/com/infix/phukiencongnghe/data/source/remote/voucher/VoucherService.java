package com.infix.phukiencongnghe.data.source.remote.voucher;

import com.infix.phukiencongnghe.common.DiscountType;
import com.infix.phukiencongnghe.data.dto.response.VoucherDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VoucherService {
    @GET("api/v1/voucher")
    Call<List<VoucherDTO>> getVouchers(
            @Query("typeCode") String typeCode,
            @Query("discountType") DiscountType discountType,
            @Query("keyword") String keyword
    );
}
