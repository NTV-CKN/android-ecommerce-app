package com.infix.phukiencongnghe.data.repository.cart;

import com.infix.phukiencongnghe.data.dto.request.CartLocalDTO;
import com.infix.phukiencongnghe.data.dto.response.BadgeCartDTO;
import com.infix.phukiencongnghe.data.dto.response.CartDTO;
import com.infix.phukiencongnghe.data.source.remote.cart.CartService;

import javax.inject.Inject;

import retrofit2.Call;

public class CartRepositoryImpl implements ICartRepository {
    private final CartService cartService;

    @Inject
    public CartRepositoryImpl(CartService cartService) {
        this.cartService = cartService;
    }

    public Call<CartDTO> getCart() {
        return cartService.getCart();
    }

    public Call<CartDTO> updateQuantity(Integer itemId, Integer qty) {
        return cartService.updateQuantity(itemId, qty);
    }

    public Call<BadgeCartDTO> getCartCount() {
        return cartService.getCount();
    }

    public Call<CartDTO> deleteItem(Integer itemId) {
        return cartService.deleteItem(itemId);
    }

    public Call<CartDTO> clearCart() {
        return cartService.clearCart();
    }

    @Override
    public Call<BadgeCartDTO> addCart(CartLocalDTO request) {
        return cartService.addCart(request);
    }


}
