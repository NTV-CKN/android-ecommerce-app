package com.infix.phukiencongnghe.data.repository.order;

import com.infix.phukiencongnghe.data.dto.response.OrderHistoryDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;

import retrofit2.Call;

public interface IOrderRepository {
    Call<PageResponseDTO<OrderHistoryDTO>> getOrderHistory(String status, Integer page, Integer pageSize);
}