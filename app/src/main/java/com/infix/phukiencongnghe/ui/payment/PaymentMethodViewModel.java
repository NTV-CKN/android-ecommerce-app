package com.infix.phukiencongnghe.ui.payment;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.response.PaymentMethodDTO;
import com.infix.phukiencongnghe.data.repository.payment.IPaymentMethodRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class PaymentMethodViewModel extends ViewModel {
    private final IPaymentMethodRepository repository;
    private final MutableLiveData<List<PaymentMethodDTO>> _paymentMethods = new MutableLiveData<>();
    public final LiveData<List<PaymentMethodDTO>> paymentMethods = _paymentMethods;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public final LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public final LiveData<Boolean> isLoading = _isLoading;

    @Inject
    public PaymentMethodViewModel(IPaymentMethodRepository repository) {
        this.repository = repository;
    }

    public void getPaymentMethods(){
        _isLoading.setValue(true);
        Call<List<PaymentMethodDTO>> pmt = repository.getPaymentMethods();
        pmt.enqueue(new Callback<List<PaymentMethodDTO>>() {
            @Override
            public void onResponse(Call<List<PaymentMethodDTO>> call, Response<List<PaymentMethodDTO>> response) {
                if(response.isSuccessful() && response.body()!=null){
                    _paymentMethods.setValue(response.body());
                }else {
                    _notifyMsg.setValue("Không thể lấy danh sách phương thức thanh toán.");
                }
                _isLoading.setValue(false);
            }

            @Override
            public void onFailure(Call<List<PaymentMethodDTO>> call, Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue(throwable.getMessage());
            }
        });
    }
    public void resetStates() {
        _paymentMethods.setValue(null);
        _isLoading.setValue(null);
        _notifyMsg.setValue(null);
    }
}

