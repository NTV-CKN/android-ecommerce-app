package com.infix.phukiencongnghe.data.repository.admin.order;

import com.infix.phukiencongnghe.data.dto.response.OrderDetailAdminDTO;
import com.infix.phukiencongnghe.data.dto.response.OrderManageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;

import retrofit2.Call;

public interface IOrderRepository {

    Call<PageResponseDTO<OrderManageDTO>> getAllOrders(
            Integer page,
            Integer limit,
            String status,
            String keyword
    );

    Call<Void> updateOrderStatus(
            Integer orderId,
            String status
    );

    Call<OrderDetailAdminDTO> getOrderDetail(
            Integer orderId
    );
}