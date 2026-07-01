package com.infix.phukiencongnghe.data.repository.order;

import com.infix.phukiencongnghe.data.dto.request.OrderRequestDTO;
import com.infix.phukiencongnghe.data.dto.response.OrderDetailsHistoryDTO;
import com.infix.phukiencongnghe.data.dto.response.OrderHistoryDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.source.remote.order.OrderSerivce;
import okhttp3.ResponseBody;

import java.util.List;

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

    @Override
    public Call<Void> cancelOrder(Integer orderId) {
        return orderSerivce.cancelOrder(orderId);
    }

    @Override
    public Call<List<OrderDetailsHistoryDTO>> getOrderDetailsHistory(Integer orderId) {
        return orderSerivce.getOrderDetails(orderId);
    }

    @Override
    public Call<ResponseBody> createOrder(String token, OrderRequestDTO orderRequest) {
        return orderSerivce.createOrder(token, orderRequest);
    }
}