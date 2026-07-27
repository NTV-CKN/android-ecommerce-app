package com.infix.phukiencongnghe.di.repository.user.voucher;

import com.infix.phukiencongnghe.data.repository.voucher.IVoucherRepository;
import com.infix.phukiencongnghe.data.repository.voucher.VoucherRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class VoucherRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IVoucherRepository bindVoucherRepository(VoucherRepositoryImpl voucherRepository);
}
