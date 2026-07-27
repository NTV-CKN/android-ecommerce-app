package com.infix.phukiencongnghe.di.repository.admin.order;

import com.infix.phukiencongnghe.data.repository.admin.order.AdminOrderRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.admin.order.IAdminOrderRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class AdminOrderRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IAdminOrderRepository bindOrderRepository(AdminOrderRepositoryImpl adminOrderRepository);
}
