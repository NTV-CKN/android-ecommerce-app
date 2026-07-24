package com.infix.phukiencongnghe.data.repository.admin.product;

import android.content.Context;
import android.content.Intent;

import com.infix.phukiencongnghe.common.OnCallbackListener;
import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;
import com.infix.phukiencongnghe.data.model.ImageUploadWrapper;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import retrofit2.Call;

public interface IProductAdminRepository {
    Call<PageResponseDTO<ProductAdminPageDTO>> loadProducts(
            String keyWord, String nameCategory, int page, int pageSize
    );
    void uploadImagesAndSaveProduct(
            List<ImageUploadWrapper> uploadWrappers,
            ProductAdminPageDTO productDTO,
            Consumer<String> callback
    );
    Call<Map<String, String>> generateUniqueSku(String productName, String color, String size);

   void removeVariant(String path, Context context, ProductVariantDTO productVariantDTO, OnCallbackListener callbackListener) throws Exception;
}
