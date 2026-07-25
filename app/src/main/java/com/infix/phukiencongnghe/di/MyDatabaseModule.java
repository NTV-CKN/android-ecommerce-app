package com.infix.phukiencongnghe.di;

import android.content.Context;

import com.infix.phukiencongnghe.data.source.local.AppDatabase;
import com.infix.phukiencongnghe.data.source.local.dao.RecentSearchProductDAO;
import com.infix.phukiencongnghe.data.source.local.dao.SearchKeywordDAO;
import com.infix.phukiencongnghe.data.source.local.dao.UserDAO;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class MyDatabaseModule {
    @Provides
    @Singleton
    public static AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return AppDatabase.getInstance(context);
    }

    @Provides
    @Singleton
    public static UserDAO provideUserDAO(AppDatabase appDatabase) {
        return appDatabase.userDAO();
    }

    @Provides
    @Singleton
    public static SearchKeywordDAO provideSearchKeywordDAO(AppDatabase appDatabase) {
        return appDatabase.searchKeywordDAO();
    }

    @Provides
    @Singleton
    public static RecentSearchProductDAO provideRecentSearchProductDAO(AppDatabase appDatabase) {
        return appDatabase.recentSearchProductDAO();
    }
}
