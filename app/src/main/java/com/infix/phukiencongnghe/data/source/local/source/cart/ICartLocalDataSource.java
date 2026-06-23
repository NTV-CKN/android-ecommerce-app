package com.infix.phukiencongnghe.data.source.local.source.cart;

import androidx.lifecycle.LiveData;

import com.infix.phukiencongnghe.data.source.local.entity.CartEntity;

import java.util.List;

public interface ICartLocalDataSource {
    LiveData<List<CartEntity>> getAll();
    void addItem(CartEntity item);
    void updateItem(CartEntity item);
    void deleteItem(CartEntity item);
    void deleteById(List<Integer> ids);
    void clearAll();
}
