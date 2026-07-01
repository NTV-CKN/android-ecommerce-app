package com.infix.phukiencongnghe.ui.admin.voucher;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.viewmodel.CreationExtras;

import com.infix.phukiencongnghe.common.DiscountType;
import com.infix.phukiencongnghe.data.dto.request.VoucherReqDTO;
import com.infix.phukiencongnghe.data.dto.response.VoucherAdminDTO;
import com.infix.phukiencongnghe.data.dto.response.VoucherDTO;
import com.infix.phukiencongnghe.data.repository.admin.voucher.IAdminVoucherRepository;
import com.infix.phukiencongnghe.ui.voucher.VoucherViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoucherAdminViewModel extends ViewModel {
    private final IAdminVoucherRepository adminVoucherRepository;

    private final MutableLiveData<List<VoucherAdminDTO>> _adminVoucher = new MutableLiveData<>();
    public final LiveData<List<VoucherAdminDTO>> adminVoucher = _adminVoucher;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<Boolean> _actionSuccess = new MutableLiveData<>();
    public final LiveData<Boolean> actionSuccess = _actionSuccess;

    private long latestRequestToken = 0;

    public VoucherAdminViewModel(IAdminVoucherRepository adminVoucherRepository) {
        this.adminVoucherRepository = adminVoucherRepository;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final IAdminVoucherRepository adminVoucherRepository;

        public Factory(IAdminVoucherRepository adminVoucherRepository) {
            this.adminVoucherRepository = adminVoucherRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass, @NonNull CreationExtras extras) {
            if(modelClass.isAssignableFrom(VoucherAdminViewModel.class)) {
                //noinspection unchecked
                return (T) new VoucherAdminViewModel(adminVoucherRepository);
            }
            throw new IllegalArgumentException("Model class illegal");
        }
    }

    public void getVoucher(String typeCode, DiscountType discountType, String keyword, long reqToken) {
        latestRequestToken = reqToken;
        _isLoading.setValue(true);
        adminVoucherRepository.getVoucher(typeCode, discountType, keyword).enqueue(new Callback<List<VoucherAdminDTO>>() {
            @Override
            public void onResponse(Call<List<VoucherAdminDTO>> call, Response<List<VoucherAdminDTO>> response) {
                if(reqToken != latestRequestToken) return;
                _isLoading.setValue(false);
                if(response.isSuccessful() && response.body() != null) {
                    _adminVoucher.setValue(response.body());
                } else {
                    _notifyMsg.setValue("Không thể load danh sách voucher");
                }
            }

            @Override
            public void onFailure(Call<List<VoucherAdminDTO>> call, Throwable throwable) {
                if (reqToken != latestRequestToken) return;
                _isLoading.setValue(false);
                _notifyMsg.setValue("Lỗi kết nối: " + throwable.getMessage());
            }
        });
    }

    public void createVoucher(VoucherReqDTO req) {
        _isLoading.setValue(true);
        adminVoucherRepository.createVoucher(req).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    _actionSuccess.setValue(true);
                } else {
                    _notifyMsg.setValue("Xóa voucher thất bại (Mã lỗi: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue("Lỗi kết nối: " + throwable.getMessage());
            }
        });
    }

    public void updateVoucher(Integer id, VoucherReqDTO req) {
        _isLoading.setValue(true);
        adminVoucherRepository.updateVoucher(id, req).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful()) {
                    _actionSuccess.setValue(true);
                } else {
                    _notifyMsg.setValue("Cập nhật voucher thất bại (Mã lỗi: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue("Lỗi kết nối: " + throwable.getMessage());
            }
        });
    }

    public void deleteVoucher(Integer id) {
        _isLoading.setValue(true);
        adminVoucherRepository.deleteVoucher(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                _isLoading.setValue(false);
                if (response.isSuccessful()) {
                    _actionSuccess.setValue(true);
                } else {
                    _notifyMsg.setValue("Xóa voucher thất bại (Mã lỗi: " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue("Lỗi kết nối: " + throwable.getMessage());
            }
        });
    }

    public void resetActionStatus() {
        _actionSuccess.setValue(false);
    }

}
