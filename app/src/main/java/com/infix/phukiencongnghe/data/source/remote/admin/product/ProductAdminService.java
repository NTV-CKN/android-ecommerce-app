package com.infix.phukiencongnghe.data.source.remote.admin.product;

import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProductAdminService {
    @GET("/api/v1/admin-product/products")
    Call<PageResponseDTO<ProductAdminPageDTO>> loadProducts(
            @Query("keyWord") String keyWord,
            @Query("nameCategory") String nameCategory,
            @Query("page") int page,
            @Query("pageSize") int pageSize
    );

    @GET("/api/v1/admin-product/generate-sku")
    Call<Map<String, String>> generateUniqueSku(
            @Query("productName")
            String productName,
            @Query("color")
            String color,
            @Query("size")
            String size
    );

    @POST("/api/v1/admin-product/add-product")
    Call<SuccessBasicDTO> saveProduct(@Body ProductAdminPageDTO productAdminPageDTO);
}
