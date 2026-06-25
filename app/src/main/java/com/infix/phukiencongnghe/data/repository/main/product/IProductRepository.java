package com.infix.phukiencongnghe.data.repository.main.product;

import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductDetailsDTO;

import java.util.List;

import retrofit2.Call;

public interface IProductRepository {
    Call<List<FeatureProductDTO>> getFeatureProduct(int limit);
    Call<ProductDetailsDTO> getProductDetails(int id);

    Call<List<FeatureProductDTO>> searchProduct(String keyword);
}
