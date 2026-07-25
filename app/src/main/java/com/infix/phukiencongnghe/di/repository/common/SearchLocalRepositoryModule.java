package com.infix.phukiencongnghe.di.repository.common;

import com.infix.phukiencongnghe.data.source.local.source.search.ISearchLocalRepository;
import com.infix.phukiencongnghe.data.source.local.source.search.SearchLocalRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class SearchLocalRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract ISearchLocalRepository bindSearchLocalRepository(SearchLocalRepositoryImpl searchLocalRepository);
}
