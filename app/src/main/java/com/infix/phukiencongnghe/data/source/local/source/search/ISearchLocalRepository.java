package com.infix.phukiencongnghe.data.source.local.source.search;

import androidx.lifecycle.LiveData;

import com.infix.phukiencongnghe.data.source.local.entity.RecentSearchProductEntity;
import com.infix.phukiencongnghe.data.source.local.entity.SearchKeywordEntity;

import java.util.List;

public interface ISearchLocalRepository {

    LiveData<List<SearchKeywordEntity>> getSearchKeywords();

    LiveData<List<RecentSearchProductEntity>> getRecentProducts();

    void saveKeyword(SearchKeywordEntity item);

    void saveRecentProduct(RecentSearchProductEntity item);

    void clearKeywords();

    void clearRecentProducts();
}