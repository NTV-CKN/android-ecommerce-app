package com.infix.phukiencongnghe.data.repository.admin.product;

import android.content.Context;

import com.infix.phukiencongnghe.common.OnCallbackListener;
import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;
import com.infix.phukiencongnghe.data.model.ImageUploadWrapper;
import com.infix.phukiencongnghe.data.source.remote.admin.product.IProductAdminRemoteDataSource;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
    public void uploadImagesAndSaveProduct(List<ImageUploadWrapper> uploadWrappers, ProductAdminPageDTO productDTO, Consumer<String> callback) {
        productAdminRemoteDataSource.uploadImagesAndSaveProduct(
                uploadWrappers, productDTO, callback
        );
    }

    @Override
    public Call<Map<String, String>> generateUniqueSku(String productName, String color, String size) {
        return productAdminRemoteDataSource.generateUniqueSku(productName, color, size);
    }

    @Override
    public void removeVariant(String path, Context context, ProductVariantDTO productVariantDTO, OnCallbackListener callbackListener) throws Exception {
         productAdminRemoteDataSource.removeVariant( path, context, productVariantDTO, callbackListener);
    }
}
