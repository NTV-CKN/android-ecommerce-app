package com.infix.phukiencongnghe.ui.cart;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.infix.phukiencongnghe.data.dto.response.CartDTO;
import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.data.repository.cart.ICartRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartViewModel extends ViewModel {
    private final ICartRepository cartRepository;

    private final MutableLiveData<CartDTO> _cartLiveData = new MutableLiveData<>();
    public final LiveData<CartDTO> cartLiveData = _cartLiveData;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public final LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public final LiveData<Boolean> isLoading = _isLoading;


    public CartViewModel(ICartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void loadCart() {
        _isLoading.setValue(true);
        cartRepository.getCart().enqueue(new Callback<CartDTO>() {
            @Override
            public void onResponse(Call<CartDTO> call, Response<CartDTO> response) {
                _isLoading.setValue(false);
                if(response.isSuccessful()) {
                    _cartLiveData.setValue(response.body());
                } else {
                    _notifyMsg.setValue("Không thể tải giỏ hàng (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<CartDTO> call, Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue("Lỗi kết nối mạng: " + throwable.getMessage());
            }
        });
    }

    public void updateQuantity(Integer itemId, Integer qty, boolean plus) {
        int newQty = plus ? qty + 1 : qty - 1;
        if (newQty < 1) return;
        cartRepository.updateQuantity(itemId, qty).enqueue(new Callback<CartDTO>() {
            @Override
            public void onResponse(Call<CartDTO> call, Response<CartDTO> response) {
                if(response.isSuccessful() && response.body() != null) {
                    _cartLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CartDTO> call, Throwable throwable) {
                _notifyMsg.setValue("Cập nhật số lượng thất bại!");
            }
        });
    }

    public void deleteCartItem(Integer itemId) {
        cartRepository.deleteItem(itemId).enqueue(new Callback<CartDTO>() {
            @Override
            public void onResponse(Call<CartDTO> call, Response<CartDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _cartLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CartDTO> call, Throwable throwable) {
                _notifyMsg.setValue("Xóa sản phẩm thất bại!");
            }
        });
    }

    public void clearAll() {
        cartRepository.clearCart().enqueue(new Callback<CartDTO>() {
            @Override
            public void onResponse(Call<CartDTO> call, Response<CartDTO> response) {
                if(response.isSuccessful()) {
                    _cartLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CartDTO> call, Throwable throwable) {
                _notifyMsg.setValue("Xóa thất bại!");
            }
        });
    }

    public void setExactQuantity(Integer itemId, Integer newQty) {
        if (newQty < 1) return;
        cartRepository.updateQuantity(itemId, newQty).enqueue(new Callback<CartDTO>() {
            @Override
            public void onResponse(Call<CartDTO> call, Response<CartDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _cartLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<CartDTO> call, Throwable t) {
                _notifyMsg.setValue("Cập nhật số lượng thất bại!");
            }
        });
    }

}
