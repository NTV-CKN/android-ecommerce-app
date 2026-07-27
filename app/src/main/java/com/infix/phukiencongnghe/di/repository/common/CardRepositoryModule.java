package com.infix.phukiencongnghe.di.repository.common;

import com.infix.phukiencongnghe.data.repository.cart.CartRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.cart.ICartRepository;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class CardRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract ICartRepository bindCardRepository(CartRepositoryImpl cartRepository);
}
