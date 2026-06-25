package com.infix.phukiencongnghe.ui.main.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.data.dto.response.FeatureProductDTO;
import com.infix.phukiencongnghe.data.repository.main.category.ICategoryRepository;
import com.infix.phukiencongnghe.data.repository.main.product.IProductRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {
    /*
        REPOSITORY
     */
    private final ICategoryRepository categoryRepository;
    private final IProductRepository featureProductRepository;
    /*
        CATEGORY
    */
    private final MutableLiveData<List<CategoryDTO>> _categoryLiveData = new MutableLiveData<>();
    public final LiveData<List<CategoryDTO>> categoryLiveData = _categoryLiveData;
    /*
        FEATURE PRODUCT
    */
    private final MutableLiveData<List<FeatureProductDTO>> _ftProdLiveData = new MutableLiveData<>();
    public final LiveData<List<FeatureProductDTO>> ftProdLiveData = _ftProdLiveData;

    private final MutableLiveData<List<FeatureProductDTO>> _mouseLiveData = new MutableLiveData<>();
    public final LiveData<List<FeatureProductDTO>> mouseLiveData = _mouseLiveData;

    private final MutableLiveData<List<FeatureProductDTO>> _keyboardLiveData = new MutableLiveData<>();
    public final LiveData<List<FeatureProductDTO>> keyboardLiveData = _keyboardLiveData;

    /*
        NOTIFY
    */
    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public final LiveData<String> notifyMsg = _notifyMsg;
    /*
        LOADING
    */
    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public final LiveData<Boolean> isLoading = _isLoading;

    public HomeViewModel(ICategoryRepository categoryRepository, IProductRepository featureProductRepository) {
        this.featureProductRepository = featureProductRepository;
        this.categoryRepository = categoryRepository;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final ICategoryRepository categoryRepository;
        private final IProductRepository productRepository;

        public Factory(ICategoryRepository categoryRepository,
                       IProductRepository productRepository) {
            this.categoryRepository = categoryRepository;
            this.productRepository = productRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(HomeViewModel.class))
                return (T) new HomeViewModel(categoryRepository, productRepository);
            throw new IllegalArgumentException("Model class illegal");
        }
    }

    public void loadFeatureProduct(Integer categoryId, int limit, MutableLiveData<List<FeatureProductDTO>> targetLiveData) {
        _isLoading.setValue(true);
        featureProductRepository.getFeatureProduct(categoryId, limit).enqueue(new Callback<List<FeatureProductDTO>>() {
            @Override
            public void onResponse(Call<List<FeatureProductDTO>> call, Response<List<FeatureProductDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    targetLiveData.setValue(response.body());
                } else {
                    _notifyMsg.setValue("Không thể tải danh sách");
                }
                _isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<FeatureProductDTO>> call, Throwable throwable) {
                _notifyMsg.setValue("Lỗi kết nối " + throwable.getMessage());
                _isLoading.setValue(false);
            }
        });
    }

    public void loadParentCategories() {
        _isLoading.setValue(true);
        categoryRepository.getParentCategory().enqueue(new Callback<List<CategoryDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<CategoryDTO>> call, @NonNull Response<List<CategoryDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _categoryLiveData.setValue(response.body());
                } else {
                    _notifyMsg.setValue("Không thể tải danh sách");
                }
                _isLoading.setValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<CategoryDTO>> call, @NonNull Throwable throwable) {
                _notifyMsg.setValue("Lỗi kết nối: " + throwable.getMessage());
                _isLoading.setValue(false);
            }
        });
    }

    public void loadFeatureProduct(int limit) {
        loadFeatureProduct(null, limit, _ftProdLiveData);
    }

    public void loadMouseProduct(int limit) {
        loadFeatureProduct(15, limit, _mouseLiveData);
    }

    public void loadKeyboardProduct(int limit) {
        loadFeatureProduct(16, limit, _keyboardLiveData);
    }

    public void resetStates() {
        _notifyMsg.setValue(null);
        _isLoading.setValue(null);
    }
}
