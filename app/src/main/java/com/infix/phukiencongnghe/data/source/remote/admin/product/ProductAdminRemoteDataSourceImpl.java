package com.infix.phukiencongnghe.data.source.remote.admin.product;

import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;

import java.util.Map;

import retrofit2.Call;

public class ProductAdminRemoteDataSourceImpl implements IProductAdminRemoteDataSource {
    private final ProductAdminService productAdminService;

    public ProductAdminRemoteDataSourceImpl(ProductAdminService productAdminService) {
        this.productAdminService = productAdminService;
    }

    @Override
    public Call<PageResponseDTO<ProductAdminPageDTO>> loadProducts(String keyWord, String nameCategory, int page, int pageSize) {
        return productAdminService.loadProducts(keyWord, nameCategory, page, pageSize);
    }

    @Override
    public Call<Map<String, String>> generateUniqueSku(String productName, String color, String size) {
        return productAdminService.generateUniqueSku(productName, color, size);
    }
}
