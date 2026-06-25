package com.infix.phukiencongnghe.data.source.remote.main;

import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FeatureProductService {
    @GET("/api/v1/product/feature")
    Call<List<FeatureProductDTO>> getFeatureProduct(@Query("categoryId") Integer categoryId, @Query("limit") int limit);

    @GET("/api/v1/product/search")
    Call<List<FeatureProductDTO>> searchProduct(@Query("keyword") String keyword);

}
