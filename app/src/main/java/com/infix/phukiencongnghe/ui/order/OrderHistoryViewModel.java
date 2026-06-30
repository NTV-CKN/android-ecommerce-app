package com.infix.phukiencongnghe.ui.order;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.response.OrderHistoryDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.repository.order.IOrderRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryViewModel extends ViewModel {

    private final IOrderRepository repository;

    private final MutableLiveData<PageResponseDTO<OrderHistoryDTO>> _orderHistoryPage = new MutableLiveData<>();
    public final LiveData<PageResponseDTO<OrderHistoryDTO>> orderHistoryPage = _orderHistoryPage;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public final LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public final LiveData<Boolean> isLoading = _isLoading;

    public OrderHistoryViewModel(IOrderRepository repository) {
        this.repository = repository;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final IOrderRepository repository;
        public Factory(IOrderRepository repository) {
            this.repository = repository;
        }
        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(OrderHistoryViewModel.class)) {
                return (T) new OrderHistoryViewModel(repository);
            }
            throw new IllegalArgumentException("Model class illegal");
        }
    }
    public void fetchOrderHistory(String status, Integer page, Integer pageSize) {
        _isLoading.setValue(true);
        Call<PageResponseDTO<OrderHistoryDTO>> call = repository.getOrderHistory(status, page, pageSize);
        call.enqueue(new Callback<PageResponseDTO<OrderHistoryDTO>>() {
            @Override
            public void onResponse(@NonNull Call<PageResponseDTO<OrderHistoryDTO>> call, @NonNull Response<PageResponseDTO<OrderHistoryDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    _orderHistoryPage.setValue(response.body());
                } else {
                    _notifyMsg.setValue("Không thể lấy danh sách lịch sử đơn hàng.");
                }
                _isLoading.setValue(false);
            }
            @Override
            public void onFailure(@NonNull Call<PageResponseDTO<OrderHistoryDTO>> call, @NonNull Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue(throwable.getMessage());
            }
        });
    }
    public void resetStates() {
        _orderHistoryPage.setValue(null);
        _isLoading.setValue(null);
        _notifyMsg.setValue(null);
    }
}