package com.infix.phukiencongnghe.ui.admin.order.detail;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.response.OrderDetailAdminDTO;
import com.infix.phukiencongnghe.data.repository.admin.order.IOrderRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailAdminViewModel extends ViewModel {

    private final IOrderRepository repository;

    private final MutableLiveData<OrderDetailAdminDTO> _order =
            new MutableLiveData<>();

    public final LiveData<OrderDetailAdminDTO> order =
            _order;

    private final MutableLiveData<String> _updateResult =
            new MutableLiveData<>();

    public final LiveData<String> updateResult =
            _updateResult;

    public OrderDetailAdminViewModel(
            IOrderRepository repository
    ) {
        this.repository = repository;
    }

    public void loadOrderDetail(Integer orderId) {

        repository.getOrderDetail(orderId)
                .enqueue(

                        new Callback<OrderDetailAdminDTO>() {
                            @Override
                            public void onResponse(
                                    Call<OrderDetailAdminDTO> call,
                                    Response<OrderDetailAdminDTO> response
                            ) {

                                Log.d(
                                        "ORDER_DETAIL",
                                        "LOAD CODE = " + response.code()
                                );

                                if (response.isSuccessful()
                                        && response.body() != null) {

                                    _order.setValue(
                                            response.body()
                                    );
                                }
                            }

                            @Override
                            public void onFailure(
                                    Call<OrderDetailAdminDTO> call,
                                    Throwable t
                            ) {

                                Log.d(
                                        "ORDER_DETAIL",
                                        "LOAD ERROR = " + t.getMessage()
                                );
                            }
                        }
                );
    }

    public void updateStatus(
            Integer orderId,
            String status
    ) {

        repository.updateOrderStatus(
                orderId,
                status
        ).enqueue(

                new Callback<Void>() {
                    @Override
                    public void onResponse(
                            Call<Void> call,
                            Response<Void> response
                    ) {

                        Log.d(
                                "UPDATE_TEST",
                                "UPDATE CODE = " + response.code()
                        );

                        Log.d(
                                "UPDATE_TEST",
                                "BODY = " + response.body()
                        );

                        if (response.isSuccessful()) {

                            _updateResult.setValue(
                                    "success"
                            );

                        } else {

                            _updateResult.setValue(
                                    "error"
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Void> call,
                            Throwable t
                    ) {

                        Log.d(
                                "UPDATE_TEST",
                                "ERROR = " + t.getMessage()
                        );

                        _updateResult.setValue(
                                "error"
                        );
                    }
                }
        );
    }

    public static class Factory
            implements ViewModelProvider.Factory {

        private final IOrderRepository repository;

        public Factory(
                IOrderRepository repository
        ) {
            this.repository = repository;
        }

        @Override
        public <T extends ViewModel> T create(
                Class<T> modelClass
        ) {
            return (T)
                    new OrderDetailAdminViewModel(
                            repository
                    );
        }
    }
}