package com.infix.phukiencongnghe.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.infix.phukiencongnghe.data.source.local.entity.UserEntity;

@Dao
public interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserEntity user);
}
