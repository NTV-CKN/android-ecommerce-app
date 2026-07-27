package com.infix.phukiencongnghe.di.repository.user.order;

import com.infix.phukiencongnghe.data.repository.order.IOrderRepository;
import com.infix.phukiencongnghe.data.repository.order.OrderRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class OrderRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IOrderRepository bindOrderRepository(OrderRepositoryImpl orderRepository);
}
