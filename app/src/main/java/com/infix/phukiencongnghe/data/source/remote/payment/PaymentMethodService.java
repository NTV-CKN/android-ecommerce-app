package com.infix.phukiencongnghe.data.source.remote.payment;

import com.infix.phukiencongnghe.data.dto.response.PaymentMethodDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PaymentMethodService {
    @GET("api/v1/payment-methods")
    Call<List<PaymentMethodDTO>> getPaymentMethods();
}
