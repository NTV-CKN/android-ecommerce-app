package com.infix.phukiencongnghe.data.repository.admin.product;

import com.infix.phukiencongnghe.data.source.remote.admin.product.IProductAdminRemoteDataSource;

public class ProductAdminRepositoryImpl implements IProductAdminRepository {
    private final IProductAdminRemoteDataSource productAdminRemoteDataSource;

    public ProductAdminRepositoryImpl(
            IProductAdminRemoteDataSource productAdminRemoteDataSource
    ) {
        this.productAdminRemoteDataSource = productAdminRemoteDataSource;
    }

}
