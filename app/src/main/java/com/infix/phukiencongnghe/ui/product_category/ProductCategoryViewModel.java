package com.infix.phukiencongnghe.ui.product_category;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductPageDTO;
import com.infix.phukiencongnghe.data.model.Page;
import com.infix.phukiencongnghe.data.repository.common.category.ICategoryRepository;
import com.infix.phukiencongnghe.data.repository.common.product.IProductRepository;
import com.infix.phukiencongnghe.utils.paging.PaginationManager;
import com.infix.phukiencongnghe.utils.paging.PaginationRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductCategoryViewModel extends ViewModel {
    private final ICategoryRepository categoryRepository;
    private final IProductRepository productRepository;
    private final MutableLiveData<List<CategoryDTO>> _categories = new MutableLiveData<>();
    public final LiveData<List<CategoryDTO>> categories = _categories;
    private final MutableLiveData<List<FeatureProductDTO>> _products = new MutableLiveData<>();
    public final LiveData<List<FeatureProductDTO>> products = _products;
    private final MutableLiveData<String> _notify = new MutableLiveData<>();
    public final LiveData<String> notify = _notify;
    private Integer selectedCategoryId = null;
    private final PaginationManager paginationManager = new PaginationManager(10);
    private final androidx.lifecycle.Observer<Page> pageObserver = page -> fetchProducts();
    private Double selectedMinPrice = null;
    private Double selectedMaxPrice = null;
    private String sortBy = null;
    private String keyword = null;
    private String direction = null;
    public ProductCategoryViewModel(
            ICategoryRepository categoryRepository,
            IProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
        observePagination();
    }
    public void changeCategory(Integer categoryId) {
        boolean isCategoryChanged =
                (selectedCategoryId == null && categoryId != null)
                        ||
                        (selectedCategoryId != null && categoryId == null)
                        ||
                        (selectedCategoryId != null &&
                                !selectedCategoryId.equals(categoryId));

        selectedCategoryId = categoryId;
        selectedMinPrice = null;
        selectedMaxPrice = null;
        sortBy = null;
        direction = null;
        keyword = null;

        if(isCategoryChanged){
            Page currentPage =
                    paginationManager.currentPage.getValue();
            if(currentPage != null && currentPage.getPage() == 1){
                fetchProducts();
            }else{
                paginationManager.setCurrentPage(1);
            }
        }else{
            fetchProducts();
        }
    }
    private void fetchProducts(){
        PaginationRequest request =
                paginationManager.getRequestParams();
        System.out.println(
                "CALL API category="
                        + selectedCategoryId
                        + " page="
                        + request.getPage()
                        + " min="
                        + selectedMinPrice
                        + " max="
                        + selectedMaxPrice
                        + " sortBy="
                        + sortBy
                        + " keyword="
                        + keyword
                        + " direction="
                        + direction
        );
        productRepository.getProductByCategory(
                        selectedCategoryId,
                        request.getPage(),
                        request.getPageSize(),
                        selectedMinPrice,
                        selectedMaxPrice,
                        sortBy,
                        keyword,
                        direction
                )
                .enqueue(new Callback<ProductPageDTO>() {

                    @Override
                    public void onResponse(
                            Call<ProductPageDTO> call,
                            Response<ProductPageDTO> response
                    ) {

                        if (response.isSuccessful()
                                && response.body() != null) {

                            ProductPageDTO data =
                                    response.body();

                            if(data.getProducts() != null){

                                _products.setValue(
                                        data.getProducts()
                                );

                            }else{

                                _products.setValue(
                                        new ArrayList<>()
                                );
                            }
                            if(data.getTotalPages() != null){
                                paginationManager.setTotalPages(
                                        data.getTotalPages()
                                );
                            }else{
                                paginationManager.setTotalPages(1);
                            }

                        } else {

                            _products.setValue(new ArrayList<>());

                            paginationManager.reset();

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

                        _products.setValue(new ArrayList<>());

                        paginationManager.reset();

                        _notify.setValue(
                                "Lỗi kết nối: " + t.getMessage()
                        );
                    }
                });
    }

    public void loadCategories() {

        categoryRepository
                .getParentCategory()
                .enqueue(new Callback<List<CategoryDTO>>() {
                    @Override
                    public void onResponse(
                            Call<List<CategoryDTO>> call,
                            Response<List<CategoryDTO>> response
                    ) {
                        if (response.isSuccessful()
                                && response.body() != null) {

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
                });
    }

    public void setPriceFilter(
            Double minPrice,
            Double maxPrice
    ){
        selectedMinPrice = minPrice;
        selectedMaxPrice = maxPrice;

        Page currentPage =
                paginationManager.currentPage.getValue();

        if(currentPage != null && currentPage.getPage() == 1){

            fetchProducts();

        }else{

            paginationManager.setCurrentPage(1);
        }
    }

    public void setSort(
            String sortBy,
            String direction
    ){
        this.sortBy = sortBy;
        this.direction = direction;

        Page currentPage =
                paginationManager.currentPage.getValue();

        if(currentPage != null && currentPage.getPage() == 1){

            fetchProducts();

        }else{

            paginationManager.setCurrentPage(1);
        }
    }

    public static class Factory
            implements ViewModelProvider.Factory {

        private final ICategoryRepository categoryRepository;
        private final IProductRepository productRepository;

        public Factory(
                ICategoryRepository categoryRepository,
                IProductRepository productRepository
        ) {
            this.categoryRepository =
                    categoryRepository;

            this.productRepository =
                    productRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(
                @NonNull Class<T> modelClass
        ) {

            if (modelClass.isAssignableFrom(
                    ProductCategoryViewModel.class
            )) {

                return (T) new ProductCategoryViewModel(
                        categoryRepository,
                        productRepository
                );
            }

            throw new IllegalArgumentException(
                    "Unknown ViewModel"
            );
        }
    }

    public void clearPriceFilter(){
        selectedMinPrice = null;
        selectedMaxPrice = null;
        sortBy = null;
        direction = null;
        Page currentPage =
                paginationManager.currentPage.getValue();
        if(currentPage != null && currentPage.getPage() == 1){
            fetchProducts();
        }else{
            paginationManager.setCurrentPage(1);
        }
    }

    public PaginationManager getPaginationManager(){
        return paginationManager;
    }

    private void observePagination(){

        paginationManager
                .currentPage
                .observeForever(pageObserver);
    }

    @Override
    protected void onCleared() {

        paginationManager
                .currentPage
                .removeObserver(pageObserver);

        super.onCleared();
    }

    public void setKeyword(String keyword){

        this.keyword = keyword;

        Page currentPage =
                paginationManager.currentPage.getValue();

        if(currentPage != null && currentPage.getPage() == 1){

            fetchProducts();

        }else{

            paginationManager.setCurrentPage(1);
        }
    }
}