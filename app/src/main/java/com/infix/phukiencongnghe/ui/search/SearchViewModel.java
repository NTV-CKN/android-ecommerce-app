package com.infix.phukiencongnghe.ui.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;
import com.infix.phukiencongnghe.data.repository.main.product.IProductRepository;
import com.infix.phukiencongnghe.data.source.local.entity.RecentSearchProductEntity;
import com.infix.phukiencongnghe.data.source.local.entity.SearchKeywordEntity;
import com.infix.phukiencongnghe.data.source.local.source.search.ISearchLocalRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {

    private final IProductRepository productRepository;
    private final ISearchLocalRepository searchLocalRepository;

    private final MutableLiveData<List<FeatureProductDTO>>
            _products = new MutableLiveData<>();

    public final LiveData<List<FeatureProductDTO>>
            products = _products;

    private final MutableLiveData<String>
            _notifyMsg = new MutableLiveData<>();

    public final LiveData<String>
            notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean>
            _isLoading = new MutableLiveData<>();

    public final LiveData<Boolean>
            isLoading = _isLoading;

    public final LiveData<List<SearchKeywordEntity>>
            searchKeywords;

    public final LiveData<List<RecentSearchProductEntity>>
            recentProducts;

    public SearchViewModel(
            IProductRepository productRepository,
            ISearchLocalRepository searchLocalRepository
    ) {
        this.productRepository = productRepository;
        this.searchLocalRepository = searchLocalRepository;

        this.searchKeywords =
                searchLocalRepository.getSearchKeywords();

        this.recentProducts =
                searchLocalRepository.getRecentProducts();
    }

    public static class Factory
            implements ViewModelProvider.Factory {

        private final IProductRepository productRepository;
        private final ISearchLocalRepository searchLocalRepository;

        public Factory(
                IProductRepository productRepository,
                ISearchLocalRepository searchLocalRepository
        ) {
            this.productRepository = productRepository;
            this.searchLocalRepository = searchLocalRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(
                @NonNull Class<T> modelClass
        ) {

            if(modelClass.isAssignableFrom(
                    SearchViewModel.class
            )) {

                return (T) new SearchViewModel(
                        productRepository,
                        searchLocalRepository
                );
            }

            throw new IllegalArgumentException(
                    "Model class illegal"
            );
        }
    }

    public void searchProduct(
            String keyword
    ){

        if(keyword == null ||
                keyword.trim().isEmpty()) {

            _notifyMsg.setValue(
                    "Nhập từ khóa tìm kiếm"
            );

            return;
        }

        _isLoading.setValue(true);

        productRepository
                .searchProduct(keyword)
                .enqueue(
                        new Callback<List<FeatureProductDTO>>() {

                            @Override
                            public void onResponse(
                                    Call<List<FeatureProductDTO>> call,
                                    Response<List<FeatureProductDTO>>
                                            response
                            ) {

                                if(response.isSuccessful()) {

                                    List<FeatureProductDTO> data =
                                            response.body();

                                    _products.setValue(data);

                                    if(data != null && !data.isEmpty()) {
                                        saveKeyword(keyword);
                                    }
                                } else {

                                    _notifyMsg.setValue(
                                            "Không thể tìm kiếm"
                                    );
                                }

                                _isLoading.setValue(false);
                            }

                            @Override
                            public void onFailure(
                                    Call<List<FeatureProductDTO>> call,
                                    Throwable throwable
                            ) {

                                _notifyMsg.setValue(
                                        throwable.getMessage() != null
                                                ? throwable.getMessage()
                                                : "Lỗi kết nối server"
                                );

                                _isLoading.setValue(false);
                            }
                        });
    }

    public void saveKeyword(
            String keyword
    ){

        SearchKeywordEntity entity =
                new SearchKeywordEntity(
                        keyword,
                        System.currentTimeMillis()
                );

        searchLocalRepository
                .saveKeyword(entity);
    }

    public void saveRecentProduct(
            FeatureProductDTO product
    ){

        RecentSearchProductEntity entity =
                new RecentSearchProductEntity(
                        product.getId(),
                        product.getName(),
                        product.getMainImage(),
                        product.getMinPrice()
                                .doubleValue(),
                        System.currentTimeMillis()
                );

        searchLocalRepository
                .saveRecentProduct(entity);
    }

    public void clearKeywordHistory() {
        searchLocalRepository.clearKeywords();
    }

    public void clearRecentProducts() {
        searchLocalRepository.clearRecentProducts();
    }
}