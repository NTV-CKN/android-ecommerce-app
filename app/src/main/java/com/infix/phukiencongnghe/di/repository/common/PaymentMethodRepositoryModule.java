package com.infix.phukiencongnghe.di.repository.common;

import com.infix.phukiencongnghe.data.repository.payment.IPaymentMethodRepository;
import com.infix.phukiencongnghe.data.repository.payment.PaymentMethodRepositoryImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.scopes.ActivityRetainedScoped;

@Module
@InstallIn(ActivityRetainedComponent.class)
public abstract class PaymentMethodRepositoryModule {
    @Binds
    @ActivityRetainedScoped
    public abstract IPaymentMethodRepository bindPaymentMethodRepository(PaymentMethodRepositoryImpl paymentMethodRepository);
}
