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

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class ProductManageAdminViewModel extends ViewModel {
    private final ICategoryRepository categoryRepository;
    private final IProductAdminRepository productAdminRepository;

    private MutableLiveData<PageResponseDTO<ProductAdminPageDTO>> _productsAdmin = new MutableLiveData<>();
    public LiveData<PageResponseDTO<ProductAdminPageDTO>> productsAdmin = _productsAdmin;

    //pagination
    private PaginationManager paginationManager;

    @Inject
    public ProductManageAdminViewModel(ICategoryRepository categoryRepository,
                                       IProductAdminRepository productAdminRepository) {
        this.categoryRepository = categoryRepository;
        this.productAdminRepository = productAdminRepository;
        paginationManager = new PaginationManager(12);
    }

    public void loadProducts(String keyWord, String nameCategory, int page, int pageSize) {
        productAdminRepository.loadProducts(
                keyWord, nameCategory, page, pageSize).enqueue(new Callback<>() {
            @Override
            public void onResponse(
                    @NonNull Call<PageResponseDTO<ProductAdminPageDTO>> call,
                    @NonNull Response<PageResponseDTO<ProductAdminPageDTO>> response) {
                if (response.isSuccessful()) {
                    PageResponseDTO<ProductAdminPageDTO> body = response.body();
                    if (body != null)
                        _productsAdmin.setValue(body);
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<PageResponseDTO<ProductAdminPageDTO>> call,
                    @NonNull Throwable throwable) {
                Log.d("ProductManageAdminViewModel", throwable.getMessage());
            }
        });
    }

    public void setCurrentPageAndPageSizeState(int page, int pageSize) {
        paginationManager.setCurrentPage(page);
        paginationManager.setTotalPages(pageSize);
    }

    public int getPageSize() {
        return paginationManager.getPageSize();
    }

    public PaginationManager getPaginationManager() {
        return paginationManager;
    }

}
