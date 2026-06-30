package com.infix.phukiencongnghe.ui.admin.product;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.repository.admin.product.IProductAdminRepository;
import com.infix.phukiencongnghe.data.repository.common.category.ICategoryRepository;
import com.infix.phukiencongnghe.utils.paging.PaginationManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductManageAdminViewModel extends ViewModel {
    private final ICategoryRepository categoryRepository;
    private final IProductAdminRepository productAdminRepository;

    private MutableLiveData<List<CategoryDTO>> _categories = new MutableLiveData<>();
    public LiveData<List<CategoryDTO>> categories = _categories;

    private MutableLiveData<PageResponseDTO<ProductAdminPageDTO>> _products = new MutableLiveData<>();
    public LiveData<PageResponseDTO<ProductAdminPageDTO>> products = _products;

    //pagination
    private PaginationManager paginationManager;

    public ProductManageAdminViewModel(ICategoryRepository categoryRepository,
                                       IProductAdminRepository productAdminRepository) {
        this.categoryRepository = categoryRepository;
        this.productAdminRepository = productAdminRepository;
        paginationManager = new PaginationManager(12);
    }

    public void loadCategoriesAvailable() {
        categoryRepository.getParentCategory().enqueue(new Callback<List<CategoryDTO>>() {
            @Override
            public void onResponse(Call<List<CategoryDTO>> call, Response<List<CategoryDTO>> response) {
                if (response.isSuccessful())
                    if (response.body() != null)
                        _categories.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<CategoryDTO>> call, Throwable throwable) {
                Log.d("ProductManageAdminViewModel", throwable.getMessage());
            }
        });
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final ICategoryRepository categoryRepository;
        private final IProductAdminRepository productAdminRepository;

        public Factory(ICategoryRepository categoryRepository,
                       IProductAdminRepository productAdminRepository) {
            this.categoryRepository = categoryRepository;
            this.productAdminRepository = productAdminRepository;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ProductManageAdminViewModel.class)) {
                return (T) new ProductManageAdminViewModel(categoryRepository, productAdminRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}
