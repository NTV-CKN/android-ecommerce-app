package com.infix.phukiencongnghe.data.repository.order;

import com.infix.phukiencongnghe.data.dto.response.OrderHistoryDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.source.remote.order.OrderSerivce;

import retrofit2.Call;

public class OrderRepositoryImpl implements IOrderRepository {
    private OrderSerivce orderSerivce;

    public OrderRepositoryImpl(OrderSerivce orderSerivce) {
        this.orderSerivce = orderSerivce;
    }

    @Override
    public Call<PageResponseDTO<OrderHistoryDTO>> getOrderHistory(String status, Integer page, Integer pageSize) {
        return orderSerivce.getOrderHistory(status, page, pageSize);
    }
}