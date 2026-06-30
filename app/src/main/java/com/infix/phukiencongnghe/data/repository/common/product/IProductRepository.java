package com.infix.phukiencongnghe.data.repository.common.product;

import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductDetailsDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductPageDTO;

import java.util.List;

import retrofit2.Call;

public interface IProductRepository {
    Call<List<FeatureProductDTO>> getFeatureProduct(Integer categoryId ,int limit);
    Call<ProductDetailsDTO> getProductDetails(int id);

    Call<List<FeatureProductDTO>> searchProduct(String keyword);

    Call<ProductPageDTO> getProductByCategory(
            Integer categoryId,
            Integer page,
            Integer pageSize,
            Double minPrice,
            Double maxPrice,
            String sortBy,
            String keyword,
            String direction
    );
}
