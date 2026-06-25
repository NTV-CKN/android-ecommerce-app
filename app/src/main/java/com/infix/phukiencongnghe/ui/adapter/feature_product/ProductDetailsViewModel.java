package com.infix.phukiencongnghe.ui.adapter.feature_product;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.response.ProductDetailsDTO;
import com.infix.phukiencongnghe.data.repository.main.product.IProductRepository;
import com.infix.phukiencongnghe.ui.user_manage.address.UserAddressManageViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsViewModel extends ViewModel {
    private final IProductRepository repository;
    private final MutableLiveData<ProductDetailsDTO> _productDetails = new MutableLiveData<>();
    public final LiveData<ProductDetailsDTO> productDetails = _productDetails;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public final LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public final LiveData<Boolean> isLoading = _isLoading;

    public ProductDetailsViewModel(IProductRepository repository){
        this.repository = repository;
    }
    public static class Factory implements ViewModelProvider.Factory{
        private IProductRepository repository;

        public Factory(IProductRepository repository) {
            this.repository = repository;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ProductDetailsViewModel.class))
                return (T) new ProductDetailsViewModel(repository);
            throw new IllegalArgumentException("Model class illegal");
        }
    }
    public void getProductById(int productId){
        _isLoading.setValue(true);
        Call<ProductDetailsDTO> prdCall = repository.getProductDetails(productId);
        prdCall.enqueue(new Callback<ProductDetailsDTO>() {
            @Override
            public void onResponse(Call<ProductDetailsDTO> call, Response<ProductDetailsDTO> response) {
                if(response.isSuccessful()){
                    _productDetails.setValue(response.body());
                }else{
                    _notifyMsg.setValue("Không thể tải thông tin chi tiết sản phẩm.");
                }
                _isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<ProductDetailsDTO> call, Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue(throwable.getMessage());
            }
        });
    }
    public void resetStates(){
        _productDetails.setValue(null);
        _isLoading.setValue(null);
        _notifyMsg.setValue(null);
    }
}
