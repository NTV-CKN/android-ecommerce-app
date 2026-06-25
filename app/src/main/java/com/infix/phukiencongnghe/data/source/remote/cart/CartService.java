package com.infix.phukiencongnghe.data.source.remote.cart;

import androidx.room.Delete;

import com.infix.phukiencongnghe.data.dto.response.CartDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
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


}
