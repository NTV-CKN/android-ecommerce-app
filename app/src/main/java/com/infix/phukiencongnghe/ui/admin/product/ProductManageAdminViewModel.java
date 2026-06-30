package com.infix.phukiencongnghe.ui.admin.product;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.repository.admin.product.IProductAdminRepository;
import com.infix.phukiencongnghe.data.repository.common.category.ICategoryRepository;
import com.infix.phukiencongnghe.utils.paging.PaginationManager;

import java.util.List;

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
}
