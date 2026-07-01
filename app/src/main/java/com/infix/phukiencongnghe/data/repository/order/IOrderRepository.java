package com.infix.phukiencongnghe.data.repository.order;

import com.infix.phukiencongnghe.data.dto.request.OrderRequestDTO;
import com.infix.phukiencongnghe.data.dto.response.OrderDetailsHistoryDTO;
import com.infix.phukiencongnghe.data.dto.response.OrderHistoryDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import okhttp3.ResponseBody;

import java.util.List;

import retrofit2.Call;

public interface IOrderRepository {
    Call<PageResponseDTO<OrderHistoryDTO>> getOrderHistory(String status, Integer page, Integer pageSize);

    Call<Void> cancelOrder(Integer orderId);

    Call<List<OrderDetailsHistoryDTO>> getOrderDetailsHistory(Integer orderId);
    Call<ResponseBody> createOrder(String token, OrderRequestDTO orderRequest);
}