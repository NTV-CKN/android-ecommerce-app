package com.infix.phukiencongnghe.ui.admin.order;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.response.OrderManageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.model.Page;
import com.infix.phukiencongnghe.data.repository.admin.order.IOrderRepository;
import com.infix.phukiencongnghe.utils.paging.PaginationManager;
import com.infix.phukiencongnghe.utils.paging.PaginationRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderManageViewModel extends ViewModel {

    private final IOrderRepository repository;

    private String currentStatus = null;

    private String currentKeyword = null;

    private final PaginationManager paginationManager =
            new PaginationManager(5);

    public final LiveData<Page> currentPage =
            paginationManager.currentPage;

    public final LiveData<Integer> totalPages =
            paginationManager.totalPages;

    private final MutableLiveData<List<OrderManageDTO>> _orders =
            new MutableLiveData<>();

    public final LiveData<List<OrderManageDTO>> orders =
            _orders;

    private final MutableLiveData<String> _notify =
            new MutableLiveData<>();

    public final LiveData<String> notify =
            _notify;

    public OrderManageViewModel(
            IOrderRepository repository
    ) {
        this.repository = repository;
    }

    public void loadOrders(
            String status,
            String keyword
    ) {
        this.currentStatus = status;
        this.currentKeyword = keyword;

        PaginationRequest request =
                paginationManager.getRequestParams();

        repository.getAllOrders(
                request.getPage(),
                request.getPageSize(),
                status,
                keyword
        ).enqueue(

                new Callback<PageResponseDTO<OrderManageDTO>>() {

                    @Override
                    public void onResponse(
                            Call<PageResponseDTO<OrderManageDTO>> call,
                            Response<PageResponseDTO<OrderManageDTO>> response
                    ) {

                        Log.d(
                                "ORDER_TEST",
                                "Response code = " + response.code()
                        );

                        if (response.isSuccessful()
                                && response.body() != null) {

                            PageResponseDTO<OrderManageDTO> pageResponse =
                                    response.body();

                            List<OrderManageDTO> list =
                                    pageResponse.getItems();

                            Log.d(
                                    "ORDER_TEST",
                                    "Total orders = " + list.size()
                            );

                            _orders.setValue(
                                    list
                            );
                            paginationManager.setTotalPages(
                                    pageResponse.getTotalPages()
                            );
                            paginationManager.setCurrentPage(
                                    pageResponse.getCurrentPage()
                            );

                        } else {

                            _orders.setValue(
                                    new ArrayList<>()
                            );

                            paginationManager.setTotalPages(
                                    1
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<PageResponseDTO<OrderManageDTO>> call,
                            Throwable t
                    ) {

                        Log.d(
                                "ORDER_TEST",
                                "Error = " + t.getMessage()
                        );

                        _notify.setValue(
                                t.getMessage()
                        );
                    }
                }
        );
    }

    /*
     * đổi page từ PaginationBarView
     */
    public void changePage(
            int page,
            String status,
            String keyword
    ) {

        paginationManager.setCurrentPage(
                page
        );

        loadOrders(
                status,
                keyword
        );
    }

    public void resetPagination() {

        paginationManager.reset();
    }

    public void updateOrderStatus(
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

                        if (response.isSuccessful()) {

                            _notify.setValue(
                                    "Cập nhật thành công"
                            );

                            loadOrders(
                                    currentStatus,
                                    currentKeyword
                            );

                        } else {

                            _notify.setValue(
                                    "Cập nhật thất bại"
                            );
                        }
                    }

                    @Override
                    public void onFailure(
                            Call<Void> call,
                            Throwable t
                    ) {

                        _notify.setValue(
                                t.getMessage()
                        );
                        paginationManager.setTotalPages(
                                1
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

        @SuppressWarnings("unchecked")
        @Override
        public <T extends ViewModel> T create(
                Class<T> modelClass
        ) {
            return (T)
                    new OrderManageViewModel(
                            repository
                    );
        }
    }
}