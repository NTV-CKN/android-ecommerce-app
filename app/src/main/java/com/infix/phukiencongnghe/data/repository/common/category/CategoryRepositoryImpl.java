package com.infix.phukiencongnghe.data.repository.common.category;

import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.data.source.remote.main.CategoryService;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;

public class CategoryRepositoryImpl implements ICategoryRepository {
    private final CategoryService categoryService;

    @Inject
    public CategoryRepositoryImpl(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @Override
    public Call<List<CategoryDTO>> getParentCategory() {
        return categoryService.getCategories();
    }
}
