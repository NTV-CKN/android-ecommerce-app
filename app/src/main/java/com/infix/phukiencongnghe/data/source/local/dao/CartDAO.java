package com.infix.phukiencongnghe.data.source.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.infix.phukiencongnghe.data.source.local.entity.CartEntity;

import java.util.List;

@Dao
public interface CartDAO {

    @Query("SELECT * FROM cart_items")
    LiveData<List<CartEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartEntity item);

    @Update
    void update(CartEntity item);

    @Delete
    void delete(CartEntity item);

    @Query("DELETE FROM cart_items")
    void clearAll();

    @Query("DELETE FROM cart_items WHERE id IN (:ids)")
    void deleteById(List<Integer> ids);
}
