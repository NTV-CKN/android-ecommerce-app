package com.infix.phukiencongnghe.data.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.infix.phukiencongnghe.data.source.local.dao.CartDAO;
import com.infix.phukiencongnghe.data.source.local.dao.UserDAO;
import com.infix.phukiencongnghe.data.source.local.dao.RecentSearchProductDAO;
import com.infix.phukiencongnghe.data.source.local.dao.SearchKeywordDAO;
import com.infix.phukiencongnghe.data.source.local.entity.CartEntity;
import com.infix.phukiencongnghe.data.source.local.entity.UserEntity;
import com.infix.phukiencongnghe.data.source.local.entity.RecentSearchProductEntity;
import com.infix.phukiencongnghe.data.source.local.entity.SearchKeywordEntity;

@Database(entities = {CartEntity.class, SearchKeywordEntity.class,
        RecentSearchProductEntity.class,  UserEntity.class}, version = 10, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    public abstract CartDAO cartDAO();
    public abstract UserDAO userDAO();
    public abstract SearchKeywordDAO searchKeywordDAO();
    public abstract RecentSearchProductDAO recentSearchProductDAO();
    public static AppDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "pkcn_db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
