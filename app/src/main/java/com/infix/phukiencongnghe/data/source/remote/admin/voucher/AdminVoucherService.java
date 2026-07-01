package com.infix.phukiencongnghe.data.source.remote.admin.voucher;

import com.infix.phukiencongnghe.common.DiscountType;
import com.infix.phukiencongnghe.data.dto.request.VoucherReqDTO;
import com.infix.phukiencongnghe.data.dto.response.VoucherAdminDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminVoucherService {
    @GET("/api/v1/admin-voucher")
    Call<List<VoucherAdminDTO>> getVoucher(@Query("typeCode") String typeCode,
                                           @Query("discountType") DiscountType discountType,
                                           @Query("keyword") String keyword);

    @POST("/api/v1/admin-voucher")
    Call<Void> createVoucher(@Body VoucherReqDTO req);

    @PUT("/api/v1/admin-voucher/{id}")
    Call<Void> updateVoucher(@Path("id") Integer id,
                             @Body VoucherReqDTO req);

    @DELETE("/api/v1/admin-voucher/{id}")
    Call<Void> deleteVoucher(@Path("id") Integer id);
}
