package com.infix.phukiencongnghe.data.source.remote.admin.product;

public class ProductAdminRemoteDataSourceImpl implements IProductAdminRemoteDataSource {
    private final ProductAdminService productAdminService;

    public ProductAdminRemoteDataSourceImpl(ProductAdminService productAdminService) {
        this.productAdminService = productAdminService;
    }
}
