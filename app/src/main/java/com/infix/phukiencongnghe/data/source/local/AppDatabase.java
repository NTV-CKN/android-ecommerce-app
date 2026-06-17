package com.infix.phukiencongnghe.data.source.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.infix.phukiencongnghe.data.source.local.dao.CartDAO;
import com.infix.phukiencongnghe.data.source.local.entity.CartEntity;

@Database(entities = {CartEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;
    public abstract CartDAO cartDAO();

    public static AppDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "pkcn_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
