package com.infix.phukiencongnghe.data.repository.admin.order;

import com.infix.phukiencongnghe.data.dto.request.UpdateOrderStatusRequest;
import com.infix.phukiencongnghe.data.dto.response.OrderDetailAdminDTO;
import com.infix.phukiencongnghe.data.dto.response.OrderManageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.data.source.remote.admin.AdminOrderService;

import javax.inject.Inject;

import retrofit2.Call;

public class AdminOrderRepositoryImpl implements IAdminOrderRepository {
    private AdminOrderService adminOrderService;

    @Inject
    public AdminOrderRepositoryImpl(AdminOrderService adminOrderService) {
        this.adminOrderService = adminOrderService;
    }

    @Override
    public Call<PageResponseDTO<OrderManageDTO>> getAllOrders(
            Integer page,
            Integer limit,
            String status,
            String keyword
    ) {
        return adminOrderService
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

        return adminOrderService
                .updateOrderStatus(
                        orderId,
                        new UpdateOrderStatusRequest(status)
                );
    }

    @Override
    public Call<OrderDetailAdminDTO> getOrderDetail(
            Integer orderId
    ) {

        return adminOrderService
                .getOrderDetail(
                        orderId
                );
    }

}