package com.infix.phukiencongnghe.data.repository.payment;

import com.infix.phukiencongnghe.data.dto.response.PaymentMethodDTO;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.data.source.remote.payment.PaymentMethodService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;

public class PaymentMethodRepositoryImpl implements IPaymentMethodRepository{
    PaymentMethodService paymentMethodService;

    public PaymentMethodRepositoryImpl() {
        this.paymentMethodService = RetrofitHelper.getPaymentMethod();
    }

    public PaymentMethodRepositoryImpl(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @Override
    public Call<List<PaymentMethodDTO>> getPaymentMethods() {
        return paymentMethodService.getPaymentMethods();
    }
}
