package com.infix.phukiencongnghe.data.repository.main.product;

import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;
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
    public Call<List<FeatureProductDTO>> getFeatureProduct(int limit) {
        return featureProductService.getFeatureProduct(limit);
    }

    @Override
    public Call<ProductDetailsDTO> getProductDetails(int id) {
        return featureProductService.getProductDetails(id);
    public Call<List<FeatureProductDTO>> searchProduct(String keyword) {
        return featureProductService.searchProduct(keyword);
    }
}
