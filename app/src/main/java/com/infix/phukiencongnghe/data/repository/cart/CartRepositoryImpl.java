package com.infix.phukiencongnghe.data.repository.cart;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.infix.phukiencongnghe.data.dto.response.CartDTO;
import com.infix.phukiencongnghe.data.source.local.entity.CartEntity;
import com.infix.phukiencongnghe.data.source.local.source.cart.CartLocalDataSourceImpl;
import com.infix.phukiencongnghe.data.source.local.source.cart.ICartLocalDataSource;
import com.infix.phukiencongnghe.data.source.remote.cart.CartService;

import java.util.List;

import retrofit2.Call;

public class CartRepositoryImpl implements ICartRepository {
    private final CartService cartService;
    public CartRepositoryImpl(CartService cartService) {
        this.cartService = cartService;
    }

    public Call<CartDTO> getCart() {
        return cartService.getCart();
    }

    public Call<CartDTO> updateQuantity(Integer itemId, Integer qty) {
        return cartService.updateQuantity(itemId, qty);
    }

    public Call<CartDTO> deleteItem(Integer itemId) {
        return cartService.deleteItem(itemId);
    }

    public Call<CartDTO> clearCart() {
        return cartService.clearCart();
    }

}
