package com.infix.phukiencongnghe.data.repository.admin.order;

import com.infix.phukiencongnghe.data.dto.request.UpdateOrderStatusRequest;
import com.infix.phukiencongnghe.data.dto.response.OrderDetailAdminDTO;
import com.infix.phukiencongnghe.data.dto.response.OrderManageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;

import retrofit2.Call;

public class AdminOrderRepositoryImpl
        implements IOrderRepository {
    @Override
    public Call<PageResponseDTO<OrderManageDTO>> getAllOrders(
            Integer page,
            Integer limit,
            String status,
            String keyword
    ) {
        return RetrofitHelper
                .getAdminOrderService()
                .getAllOrders(
                        page,
                        limit,
                        status,
                        keyword
                );
    }

    @Override
    public Call<Void> updateOrderStatus(
            Integer orderId,
            String status
    ) {

        return RetrofitHelper
                .getAdminOrderService()
                .updateOrderStatus(
                        orderId,
                        new UpdateOrderStatusRequest(status)
                );
    }

    @Override
    public Call<OrderDetailAdminDTO> getOrderDetail(
            Integer orderId
    ) {

        return RetrofitHelper
                .getAdminOrderService()
                .getOrderDetail(
                        orderId
                );
    }

}