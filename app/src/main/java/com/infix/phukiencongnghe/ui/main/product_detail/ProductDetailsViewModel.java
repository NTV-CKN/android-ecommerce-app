package com.infix.phukiencongnghe.ui.main.product_detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.request.CartLocalDTO;
import com.infix.phukiencongnghe.data.dto.response.BadgeCartDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductDetailsDTO;
import com.infix.phukiencongnghe.data.repository.cart.ICartRepository;
import com.infix.phukiencongnghe.data.repository.main.product.IProductRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsViewModel extends ViewModel {
    private final IProductRepository repository;
    private final ICartRepository cartRepository;
    private final MutableLiveData<ProductDetailsDTO> _productDetails = new MutableLiveData<>();
    public final LiveData<ProductDetailsDTO> productDetails = _productDetails;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public final LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public final LiveData<Boolean> isLoading = _isLoading;
    private final MutableLiveData<Long> _cartBadgeCount = new MutableLiveData<>();
    public final LiveData<Long> cartBadgeCount = _cartBadgeCount;
    public ProductDetailsViewModel(IProductRepository repository, ICartRepository cartRepository){
        this.repository = repository;
        this.cartRepository = cartRepository;
    }
    public static class Factory implements ViewModelProvider.Factory{
        private IProductRepository repository;
        private ICartRepository cartRepository;
        public Factory(IProductRepository repository, ICartRepository cartRepository) {
            this.repository = repository;
            this.cartRepository = cartRepository;
        }
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(ProductDetailsViewModel.class))
                return (T) new ProductDetailsViewModel(repository, cartRepository);
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
    public void addtoCart(CartLocalDTO request){
        _isLoading.setValue(true);

        // Ghi log kiểm tra xem dữ liệu trước khi gửi đi là gì
        android.util.Log.d("API_CART", "Bắt đầu gọi API với VariantId: " + request.getProductVariantId()); // (Sửa lại theo hàm get của bạn)

        cartRepository.addCart(request).enqueue(new Callback<BadgeCartDTO>() {
            @Override
            public void onResponse(Call<BadgeCartDTO> call, Response<BadgeCartDTO> response) {
                _isLoading.setValue(false);

                // IN MÃ LỖI SERVER TRẢ VỀ RA ĐÂY
                android.util.Log.d("API_CART", "Mã phản hồi từ Server (Code): " + response.code());

                if(response.isSuccessful() && response.body() != null){
                    _cartBadgeCount.setValue(response.body().getNumOfItems());
                    _notifyMsg.setValue("Đã thêm sản phẩm vào giỏ hàng");
                } else {
                    // Nếu lỗi, in thử nội dung lỗi từ Server xem sao
                    try {
                        android.util.Log.e("API_CART", "Lỗi Server trả về: " + response.errorBody().string());
                    } catch (Exception e) { e.printStackTrace(); }

                    _notifyMsg.setValue("Không thể thêm sản phẩm vào giỏ hàng");
                }
            }

            @Override
            public void onFailure(Call<BadgeCartDTO> call, Throwable throwable) {
                _isLoading.setValue(false);
                // IN LỖI NẾU KHÔNG KẾT NỐI ĐƯỢC INTERNET HOẶC LỖI MẠNG
                android.util.Log.e("API_CART", "LỖI KẾT NỐI MẠNG: " + throwable.getMessage());
                _notifyMsg.setValue("Lỗi: " + throwable.getMessage());
            }
        });
    }
    public void resetStates(){
        _productDetails.setValue(null);
        _isLoading.setValue(null);
        _notifyMsg.setValue(null);
        _cartBadgeCount.setValue(null);
    }
}
