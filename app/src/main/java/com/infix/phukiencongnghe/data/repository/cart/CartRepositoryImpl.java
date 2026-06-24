package com.infix.phukiencongnghe.data.repository.cart;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.infix.phukiencongnghe.data.source.local.entity.CartEntity;
import com.infix.phukiencongnghe.data.source.local.repo.CartLocalRepositoryImpl;
import com.infix.phukiencongnghe.data.source.local.repo.ICartLocalRepository;

import java.util.List;

public class CartRepositoryImpl implements ICartRepository {

    private final ICartLocalRepository localRepository;

    public CartRepositoryImpl(Context context) {
        this.localRepository = new CartLocalRepositoryImpl(context);
    }

    @Override
    public void addItem(CartEntity item) {
        localRepository.addItem(item);
    }

    @Override
    public LiveData<List<CartEntity>> getCartItems() {
        return localRepository.getAll();
    }

    @Override
    public void updateItem(CartEntity item) {
        localRepository.updateItem(item);
    }

    @Override
    public void deleteItem(CartEntity item) {
        localRepository.deleteItem(item);
    }

    @Override
    public void deleteItems(List<Integer> ids) {
        localRepository.deleteById(ids);
    }

    @Override
    public void clearAll() {
        localRepository.clearAll();
    }
}
