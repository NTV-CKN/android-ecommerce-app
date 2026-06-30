package com.infix.phukiencongnghe.data.repository.common.product;

import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductPageDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductDetailsDTO;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.data.source.remote.main.FeatureProductService;

import java.util.List;

import retrofit2.Call;

public class FeatureProductRepositoryImpl implements IProductRepository{

    FeatureProductService   featureProductService;

    public FeatureProductRepositoryImpl() {
        this.featureProductService = RetrofitHelper.getFeatureProductService();
    }

    public FeatureProductRepositoryImpl(FeatureProductService productService) {
        this.featureProductService = productService;
    }

    @Override
    public Call<List<FeatureProductDTO>> getFeatureProduct(Integer categoryId, int limit) {
        return featureProductService.getFeatureProduct(categoryId, limit);
    }

    @Override
    public Call<ProductDetailsDTO> getProductDetails(int id) {
        return featureProductService.getProductDetails(id);
    }

    public Call<List<FeatureProductDTO>> searchProduct(String keyword) {
        return featureProductService.searchProduct(keyword);
    }
    @Override
    public Call<ProductPageDTO> getProductByCategory(
            Integer categoryId,
            Integer page,
            Integer pageSize,
            Double minPrice,
            Double maxPrice,
            String sortBy,
            String keyword,
            String direction
    ){
        return featureProductService.getProductByCategory(
                categoryId,
                page,
                pageSize,
                minPrice,
                maxPrice,
                sortBy,
                keyword,
                direction
        );
    }
}
