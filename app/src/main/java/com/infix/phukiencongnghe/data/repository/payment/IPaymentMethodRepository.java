package com.infix.phukiencongnghe.data.repository.payment;

import com.infix.phukiencongnghe.data.dto.response.PaymentMethodDTO;

import java.util.List;

import retrofit2.Call;

public interface IPaymentMethodRepository {
    Call<List<PaymentMethodDTO>> getPaymentMethods();
}
