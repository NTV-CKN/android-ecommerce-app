package com.infix.phukiencongnghe.data.repository.main.category;

import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.data.source.remote.main.CategoryService;

import java.util.List;

import retrofit2.Call;

public class CategoryRepositoryImpl implements ICategoryRepository {
    private CategoryService categoryService;

    public CategoryRepositoryImpl() {
        this.categoryService = RetrofitHelper.getCategoryService();
    }
    @Override
    public Call<List<CategoryDTO>> getParentCategory() {
        return categoryService.getCategories();
    }
}
