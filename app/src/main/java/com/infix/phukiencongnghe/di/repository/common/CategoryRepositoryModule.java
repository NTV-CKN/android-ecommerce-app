package com.infix.phukiencongnghe.di.repository.common;

import com.infix.phukiencongnghe.data.repository.common.category.CategoryRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.common.category.ICategoryRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class CategoryRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract ICategoryRepository bindCategoryRepository(CategoryRepositoryImpl categoryRepository);
}
