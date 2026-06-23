package com.infix.phukiencongnghe.data.source.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.infix.phukiencongnghe.data.source.local.entity.RecentSearchProductEntity;

import java.util.List;

@Dao
public interface RecentSearchProductDAO {

    @Query("SELECT * FROM recent_search_product ORDER BY created_at DESC LIMIT 10")
    LiveData<List<RecentSearchProductEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RecentSearchProductEntity item);

    // CHỈ GIỮ 10 PRODUCT GẦN NHẤT
    @Query(
            "DELETE FROM recent_search_product " +
                    "WHERE productId NOT IN (" +
                    "SELECT productId FROM recent_search_product " +
                    "ORDER BY created_at DESC LIMIT 10)"
    )
    void keepOnly10();

    @Query("DELETE FROM recent_search_product")
    void clearAll();
}