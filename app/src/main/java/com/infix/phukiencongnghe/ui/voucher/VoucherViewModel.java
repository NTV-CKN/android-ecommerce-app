package com.infix.phukiencongnghe.ui.voucher;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.infix.phukiencongnghe.common.DiscountType;
import com.infix.phukiencongnghe.data.dto.response.VoucherDTO;
import com.infix.phukiencongnghe.data.repository.voucher.IVoucherRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherViewModel extends ViewModel {
    private final IVoucherRepository voucherRepository;

    private final MutableLiveData<List<VoucherDTO>> _userVoucher = new MutableLiveData<>();
    public final LiveData<List<VoucherDTO>> userVoucher = _userVoucher;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    // Token của request gần nhất được ViewModel chấp nhận kết quả
    private long latestRequestToken = 0;

    public VoucherViewModel(IVoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final IVoucherRepository voucherRepository;

        public Factory(IVoucherRepository voucherRepository) {
            this.voucherRepository = voucherRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass, @NonNull CreationExtras extras) {
            if (modelClass.isAssignableFrom(VoucherViewModel.class)) {
                //noinspection unchecked
                return (T) new VoucherViewModel(voucherRepository);
            }
            throw new IllegalArgumentException("Model class illegal");
        }
    }

    /**
     * @param requestToken token do Fragment cấp, dùng để bỏ qua kết quả của request
     *                     đã cũ (bị một request mới hơn ghi đè trong lúc đang chờ).
     */
    public void getVouchers(String typeCode, DiscountType discountType, String keyword, long requestToken) {
        latestRequestToken = requestToken;
        _isLoading.setValue(true);

        voucherRepository.getVouchers(typeCode, discountType, keyword).enqueue(new Callback<List<VoucherDTO>>() {
            @Override
            public void onResponse(Call<List<VoucherDTO>> call, Response<List<VoucherDTO>> response) {
                // Nếu đã có request mới hơn được gọi sau đó -> bỏ qua kết quả trễ này
                if (requestToken != latestRequestToken) return;

                _isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    _userVoucher.setValue(response.body());
                } else {
                    _notifyMsg.setValue("Không thể load danh sách voucher");
                }
            }

            @Override
            public void onFailure(Call<List<VoucherDTO>> call, Throwable throwable) {
                if (requestToken != latestRequestToken) return;

                _isLoading.setValue(false);
                _notifyMsg.setValue("Lỗi kết nối: " + throwable.getMessage());
            }
        });
    }
}