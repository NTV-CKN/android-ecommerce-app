package com.infix.phukiencongnghe.data.source.remote.order;

import com.infix.phukiencongnghe.data.dto.response.OrderHistoryDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OrderSerivce {
    @GET("api/v1/orders/history")
    Call<PageResponseDTO<OrderHistoryDTO>> getOrderHistory(@Query("status") String status, @Query("page") Integer page, @Query("pageSize") Integer pageSize);
}
