package com.infix.phukiencongnghe.data.repository.admin.product;

import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.source.remote.admin.product.IProductAdminRemoteDataSource;

import java.util.Map;

import retrofit2.Call;

public class ProductAdminRepositoryImpl implements IProductAdminRepository {
    private final IProductAdminRemoteDataSource productAdminRemoteDataSource;

    public ProductAdminRepositoryImpl(
            IProductAdminRemoteDataSource productAdminRemoteDataSource
    ) {
        this.productAdminRemoteDataSource = productAdminRemoteDataSource;
    }

    @Override
    public Call<PageResponseDTO<ProductAdminPageDTO>> loadProducts(
            String keyWord, String nameCategory, int page, int pageSize
    ) {
        return productAdminRemoteDataSource.loadProducts(
                keyWord, nameCategory, page, pageSize
        );
    }

    @Override
    public Call<Map<String, String>> generateUniqueSku(String productName, String color, String size) {
        return productAdminRemoteDataSource.generateUniqueSku(productName, color, size);
    }
}
