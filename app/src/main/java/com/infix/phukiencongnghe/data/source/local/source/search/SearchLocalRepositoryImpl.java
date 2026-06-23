package com.infix.phukiencongnghe.data.source.local.source.search;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.infix.phukiencongnghe.data.source.local.AppDatabase;
import com.infix.phukiencongnghe.data.source.local.dao.RecentSearchProductDAO;
import com.infix.phukiencongnghe.data.source.local.dao.SearchKeywordDAO;
import com.infix.phukiencongnghe.data.source.local.entity.RecentSearchProductEntity;
import com.infix.phukiencongnghe.data.source.local.entity.SearchKeywordEntity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchLocalRepositoryImpl
        implements ISearchLocalRepository {

    private final SearchKeywordDAO keywordDAO;
    private final RecentSearchProductDAO productDAO;
    private final ExecutorService executors;

    public SearchLocalRepositoryImpl(Context context) {

        AppDatabase db =
                AppDatabase.getInstance(context);

        this.keywordDAO = db.searchKeywordDAO();
        this.productDAO = db.recentSearchProductDAO();

        this.executors =
                Executors.newSingleThreadExecutor();
    }

    @Override
    public LiveData<List<SearchKeywordEntity>>
    getSearchKeywords() {

        return keywordDAO.getAll();
    }

    @Override
    public LiveData<List<RecentSearchProductEntity>>
    getRecentProducts() {

        return productDAO.getAll();
    }

    @Override
    public void saveKeyword(
            SearchKeywordEntity item
    ) {
        executors.execute(() -> {

            keywordDAO.insert(item);

            keywordDAO.keepOnly10();
        });
    }

    @Override
    public void saveRecentProduct(
            RecentSearchProductEntity item
    ) {
        executors.execute(() -> {

            productDAO.insert(item);

            productDAO.keepOnly10();
        });
    }

    @Override
    public void clearKeywords() {
        executors.execute(
                keywordDAO::clearAll
        );
    }

    @Override
    public void clearRecentProducts() {
        executors.execute(
                productDAO::clearAll
        );
    }
}