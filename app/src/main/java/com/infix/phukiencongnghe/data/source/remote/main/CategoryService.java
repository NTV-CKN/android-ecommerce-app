package com.infix.phukiencongnghe.data.source.remote.main;

import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CategoryService {
    @GET("/api/v1/categories/parents")
    Call<List<CategoryDTO>> getCategories();
}
