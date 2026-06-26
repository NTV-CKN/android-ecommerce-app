package com.infix.phukiencongnghe.data.source.remote.cart;

import com.infix.phukiencongnghe.data.dto.request.CartLocalDTO;
import com.infix.phukiencongnghe.data.dto.response.BadgeCartDTO;
import com.infix.phukiencongnghe.data.dto.response.CartDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartService {

    @GET("/api/v1/cart")
    Call<CartDTO> getCart();

    @PUT("/api/v1/cart/items/{itemId}")
    Call<CartDTO> updateQuantity(@Path("itemId") Integer itemId,@Query("qty") Integer qty);

    @DELETE("/api/v1/cart/items/{itemId}")
    Call<CartDTO> deleteItem(@Path("itemId") Integer itemId);

    @DELETE("/api/v1/cart/items")
    Call<CartDTO> clearCart();

    @POST("api/v1/cart/add")
    Call<BadgeCartDTO> addCart(@Body CartLocalDTO request);

}
