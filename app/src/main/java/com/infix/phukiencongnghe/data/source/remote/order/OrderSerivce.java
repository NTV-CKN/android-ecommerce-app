package com.infix.phukiencongnghe.data.source.remote.order;

import com.infix.phukiencongnghe.data.dto.request.OrderRequestDTO;
import com.infix.phukiencongnghe.data.dto.response.OrderDetailsHistoryDTO;
import com.infix.phukiencongnghe.data.dto.response.OrderHistoryDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import okhttp3.ResponseBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderSerivce {
    @GET("api/v1/orders/history")
    Call<PageResponseDTO<OrderHistoryDTO>> getOrderHistory(@Query("status") String status, @Query("page") Integer page, @Query("pageSize") Integer pageSize);

    @POST("api/v1/orders/{orderId}/cancel")
    Call<Void> cancelOrder(@Path("orderId") Integer orderId);

    @GET("api/v1/orders/{orderId}/details")
    Call<List<OrderDetailsHistoryDTO>> getOrderDetails(@Path("orderId") Integer orderId);

    @POST("api/v1/orders")
    Call<ResponseBody> createOrder(
            @Header("Authorization") String token,
            @Body OrderRequestDTO orderRequest
    );
}