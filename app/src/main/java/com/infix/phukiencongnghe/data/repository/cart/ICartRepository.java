package com.infix.phukiencongnghe.data.repository.cart;

import androidx.lifecycle.LiveData;

import com.infix.phukiencongnghe.data.source.local.entity.CartEntity;

import java.util.List;

public interface ICartRepository {
    LiveData<List<CartEntity>> getCartItems();
    void addItem(CartEntity item);
    void updateItem(CartEntity item);
    void deleteItem(CartEntity item);
    void deleteItems(List<Integer> ids);
    void clearAll();
}
