package com.infix.phukiencongnghe.data.repository.admin.product;

import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;

import java.util.Map;

import retrofit2.Call;

public interface IProductAdminRepository {
    Call<PageResponseDTO<ProductAdminPageDTO>> loadProducts(
            String keyWord, String nameCategory, int page, int pageSize
    );

    Call<Map<String, String>> generateUniqueSku(String productName, String color, String size);
}
