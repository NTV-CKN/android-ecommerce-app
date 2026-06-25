package com.infix.phukiencongnghe.data.source.local.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.infix.phukiencongnghe.data.source.local.entity.SearchKeywordEntity;

import java.util.List;

import retrofit2.http.DELETE;

@Dao
public interface SearchKeywordDAO {

    @Query("SELECT * FROM search_keyword ORDER BY created_at DESC LIMIT 10")
    LiveData<List<SearchKeywordEntity>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SearchKeywordEntity item);

    @Query("DELETE FROM search_keyword")
    void clearAll();

    @Query(
            "DELETE FROM search_keyword " +
                    "WHERE keyword NOT IN (" +
                    "SELECT keyword FROM search_keyword " +
                    "ORDER BY created_at DESC LIMIT 10)"
    )
    void keepOnly10();
}