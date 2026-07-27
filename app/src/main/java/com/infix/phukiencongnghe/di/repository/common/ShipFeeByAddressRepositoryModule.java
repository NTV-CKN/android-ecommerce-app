package com.infix.phukiencongnghe.di.repository.common;

import com.infix.phukiencongnghe.data.repository.ship_fee.IShipFeeByAddressRepository;
import com.infix.phukiencongnghe.data.repository.ship_fee.ShipFeeByAddressRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class ShipFeeByAddressRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IShipFeeByAddressRepository bindShipFeeByAddressRepository(ShipFeeByAddressRepositoryImpl shipFeeByAddressRepository);
}
