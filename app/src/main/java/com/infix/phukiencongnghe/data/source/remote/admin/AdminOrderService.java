package com.infix.phukiencongnghe.data.source.remote.admin;

import com.infix.phukiencongnghe.data.dto.request.UpdateOrderStatusRequest;
import com.infix.phukiencongnghe.data.dto.response.OrderDetailAdminDTO;
import com.infix.phukiencongnghe.data.dto.response.OrderManageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AdminOrderService {
    @GET("api/v1/admin-order/manage")
    Call<PageResponseDTO<OrderManageDTO>> getAllOrders(

            @Query("page")
            Integer page,

            @Query("limit")
            Integer limit,

            @Query("status")
            String status,

            @Query("keyword")
            String keyword
    );

    @PUT("api/v1/admin-order/{id}/status")
    Call<Void> updateOrderStatus(

            @Path("id")
            Integer orderId,

            @Body
            UpdateOrderStatusRequest request
    );

    @GET("api/v1/admin-order/{id}")
    Call<OrderDetailAdminDTO> getOrderDetail(

            @Path("id")
            Integer orderId
    );


}