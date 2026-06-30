package com.infix.phukiencongnghe.data.repository.common.category;

import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;

import java.util.List;

import retrofit2.Call;

public interface ICategoryRepository {
    Call<List<CategoryDTO>> getParentCategory();
}
