package com.infix.phukiencongnghe.di.source.remote.admin;

import com.infix.phukiencongnghe.data.source.remote.admin.product.IProductAdminRemoteDataSource;
import com.infix.phukiencongnghe.data.source.remote.admin.product.ProductAdminRemoteDataSourceImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class ProductAdminRemoteDataSourceModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IProductAdminRemoteDataSource bindProductAdminRemoteDS(ProductAdminRemoteDataSourceImpl productAdminRemoteDataSource);
}
