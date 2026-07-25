package com.infix.phukiencongnghe.di.repository.admin.product;

import com.infix.phukiencongnghe.data.repository.admin.product.IProductAdminRepository;
import com.infix.phukiencongnghe.data.repository.admin.product.ProductAdminRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class ProductAdminRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IProductAdminRepository bindProductAdminRepository(ProductAdminRepositoryImpl productAdminRepository);
}
