package com.infix.phukiencongnghe.ui.searchadvance;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductPageDTO;
import com.infix.phukiencongnghe.data.model.Page;
import com.infix.phukiencongnghe.data.repository.main.product.IProductRepository;
import com.infix.phukiencongnghe.utils.paging.PaginationManager;
import com.infix.phukiencongnghe.utils.paging.PaginationRequest;
import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.data.repository.main.category.ICategoryRepository;
import com.infix.phukiencongnghe.data.source.local.source.search.ISearchLocalRepository;
import com.infix.phukiencongnghe.data.source.local.entity.RecentSearchProductEntity;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchAdvanceViewModel extends ViewModel {

    private final IProductRepository productRepository;

    private final ICategoryRepository categoryRepository;
    private final ISearchLocalRepository searchLocalRepository;

    private final MutableLiveData<List<CategoryDTO>>
            _categories = new MutableLiveData<>();

    public final LiveData<List<CategoryDTO>>
            categories = _categories;

    private final MutableLiveData<List<FeatureProductDTO>>
            _products = new MutableLiveData<>();

    public final LiveData<List<FeatureProductDTO>>
            products = _products;

    private final MutableLiveData<String>
            _notify = new MutableLiveData<>();

    public final LiveData<String>
            notify = _notify;

    private String keyword;

    private Integer categoryId = null;

    private Double minPrice = null;

    private Double maxPrice = null;

    private String sortBy = null;

    private String direction = null;

    private final PaginationManager
            paginationManager =
            new PaginationManager(10);

    private final androidx.lifecycle.Observer<Page>
            pageObserver =
            page -> fetchProducts();

    public SearchAdvanceViewModel(
            IProductRepository productRepository,
            ICategoryRepository categoryRepository,
            ISearchLocalRepository searchLocalRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.searchLocalRepository = searchLocalRepository;

        observePagination();
    }


    // ===== FETCH API =====

    private void fetchProducts() {

        // Không search nếu keyword rỗng

        if (keyword == null || keyword.trim().isEmpty()) {
            return;
        }


        PaginationRequest request =
                paginationManager
                        .getRequestParams();


        productRepository
                .getProductByCategory(
                        categoryId,
                        request.getPage(),
                        request.getPageSize(),
                        minPrice,
                        maxPrice,
                        sortBy,
                        keyword,
                        direction
                )
                .enqueue(
                        new Callback<ProductPageDTO>() {

                            @Override
                            public void onResponse(
                                    Call<ProductPageDTO> call,
                                    Response<ProductPageDTO> response
                            ) {

                                if (response.isSuccessful()
                                        && response.body() != null) {

                                    ProductPageDTO data =
                                            response.body();


                                    /*
                                     * FIX QUAN TRỌNG
                                     *
                                     * Update totalPages trước
                                     * để Fragment đọc đúng pagination
                                     */

                                    if (data.getTotalPages() != null) {

                                        paginationManager
                                                .setTotalPages(
                                                        data.getTotalPages()
                                                );
                                    }


                                    // Update products sau

                                    if (data.getProducts() != null
                                            && !data.getProducts().isEmpty()) {

                                        _products.setValue(
                                                data.getProducts()
                                        );

                                    } else {

                                        _products.setValue(
                                                new ArrayList<>()
                                        );
                                    }

                                } else {

                                    _notify.setValue(
                                            "Không tải được sản phẩm"
                                    );
                                }
                            }


                            @Override
                            public void onFailure(
                                    Call<ProductPageDTO> call,
                                    Throwable t
                            ) {

                                _notify.setValue(
                                        t.getMessage() != null
                                                ? t.getMessage()
                                                : "Lỗi kết nối server"
                                );
                            }
                        }
                );
    }


    // ===== SET KEYWORD =====

    public void setKeyword(
            String keyword
    ) {

        this.keyword = keyword;

        Page currentPage =
                paginationManager
                        .currentPage
                        .getValue();

        if (currentPage != null
                && currentPage.getPage() == 1) {

            fetchProducts();

        } else {

            paginationManager
                    .setCurrentPage(1);
        }
    }

    // ===== SORT =====

    public void setSort(
            String sortBy,
            String direction
    ) {

        this.sortBy = sortBy;
        this.direction = direction;

        paginationManager
                .setCurrentPage(1);
    }


    // ===== PAGINATION =====

    public PaginationManager
    getPaginationManager() {

        return paginationManager;
    }

    private void observePagination() {

        paginationManager
                .currentPage
                .observeForever(
                        pageObserver
                );
    }

    @Override
    protected void onCleared() {

        paginationManager
                .currentPage
                .removeObserver(
                        pageObserver
                );

        super.onCleared();
    }


    // ===== FACTORY =====

    public static class Factory
            implements ViewModelProvider.Factory {

        private final IProductRepository productRepository;
        private final ICategoryRepository categoryRepository;
        private final ISearchLocalRepository searchLocalRepository;

        public Factory(
                IProductRepository productRepository,
                ICategoryRepository categoryRepository,
                ISearchLocalRepository searchLocalRepository
        ) {
            this.productRepository = productRepository;
            this.categoryRepository = categoryRepository;
            this.searchLocalRepository = searchLocalRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel>
        T create(
                @NonNull Class<T> modelClass
        ) {

            if (modelClass.isAssignableFrom(
                    SearchAdvanceViewModel.class
            )) {
                return (T)
                        new SearchAdvanceViewModel(
                                productRepository,
                                categoryRepository,
                                searchLocalRepository
                        );
            }

            throw new IllegalArgumentException(
                    "Unknown ViewModel"
            );
        }
    }

    public void loadCategories() {

        categoryRepository
                .getParentCategory()
                .enqueue(
                        new Callback<List<CategoryDTO>>() {

                            @Override
                            public void onResponse(
                                    Call<List<CategoryDTO>> call,
                                    Response<List<CategoryDTO>> response
                            ) {

                                if(response.isSuccessful()
                                        && response.body() != null){

                                    _categories.setValue(
                                            response.body()
                                    );
                                }
                            }

                            @Override
                            public void onFailure(
                                    Call<List<CategoryDTO>> call,
                                    Throwable t
                            ) {

                                _notify.setValue(
                                        t.getMessage()
                                );
                            }
                        }
                );
    }

    public void applyFilter(
            Integer categoryId,
            Double minPrice,
            Double maxPrice
    ) {

        this.categoryId = categoryId;

        this.minPrice = minPrice;

        this.maxPrice = maxPrice;

        paginationManager.setCurrentPage(1);
    }

    public void saveRecentProduct(
            FeatureProductDTO product
    ){

        double price = 0;

        if(product.getMinPrice() != null){

            price =
                    product.getMinPrice()
                            .doubleValue();
        }

        RecentSearchProductEntity entity =
                new RecentSearchProductEntity(
                        product.getId(),
                        product.getName(),
                        product.getMainImage(),
                        price,
                        System.currentTimeMillis()
                );

        searchLocalRepository
                .saveRecentProduct(entity);
    }
}