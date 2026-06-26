package com.infix.phukiencongnghe.data.repository.cart;

import androidx.lifecycle.LiveData;

import com.infix.phukiencongnghe.data.dto.request.CartLocalDTO;
import com.infix.phukiencongnghe.data.dto.response.BadgeCartDTO;
import com.infix.phukiencongnghe.data.dto.response.CartDTO;
import com.infix.phukiencongnghe.data.source.local.entity.CartEntity;
import com.infix.phukiencongnghe.data.source.remote.cart.CartService;

import java.util.List;

import retrofit2.Call;

public interface ICartRepository {

    public Call<CartDTO> getCart();

    public Call<CartDTO> updateQuantity(Integer itemId, Integer qty);

    public Call<CartDTO> deleteItem(Integer itemId);

    public Call<CartDTO> clearCart();
    public Call<BadgeCartDTO> addCart(CartLocalDTO request);
}
