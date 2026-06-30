package com.infix.phukiencongnghe.data.source.remote.admin.product;

import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductAdminService {
    @GET("/api/v1/admin-product/products")
    Call<PageResponseDTO<ProductAdminPageDTO>> loadProducts(
            @Query("keyWord") String keyWord,
            @Query("nameCategory") String nameCategory,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );
}
