package com.infix.phukiencongnghe.data.repository.main.product;

import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;

import java.util.List;

import retrofit2.Call;

public interface IProductRepository {
    Call<List<FeatureProductDTO>> getFeatureProduct(Integer categoryId ,int limit);

    Call<List<FeatureProductDTO>> searchProduct(String keyword);
}
