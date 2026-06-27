package com.infix.phukiencongnghe.ui.user_manage.address.update_or_add;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.infix.phukiencongnghe.data.dto.response.ExceptionResponseDTO;
import com.infix.phukiencongnghe.data.dto.response.ShipFeeByAddressDTO;
import com.infix.phukiencongnghe.data.dto.response.UserAddressDTO;
import com.infix.phukiencongnghe.data.repository.ship_fee.IShipFeeByAddressRepository;
import com.infix.phukiencongnghe.data.repository.user_manage.address.IUserAddressManageRepository;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Lớp này chứa dữ liệu về UserAddressDTO cho phiên làm việc của AddOrUpdateUserAddressFragmen
//Có nhiệm vụ kéo data ship fee từ server để hiển thị cho người dùng chọn
public class AddOrUpdateUserAddressViewModel extends ViewModel {
    private IUserAddressManageRepository userAddressManageRepository;
    private IShipFeeByAddressRepository shipFeeByAddressRepository;

    private boolean isUpdate;
    //Khi tạo giao diện AddOrUpdate fragment thì nó có logic check và tạo lại spinner + set select,
    //ta sử dụng biến này để tránh trường hợp dữ liệu từ AddressDeliveryPicker trả về nhưng
    //vô tình bị refresh theo logic AddOrUpdate Fragment
    //Nếu là false tức nó chưa đi qua Picker
    private boolean isReturnFromAddressDelivery = false;

    private final MutableLiveData<UserAddressDTO> _userAddress = new MutableLiveData<>();
    public final LiveData<UserAddressDTO> userAddress = _userAddress;

    //Nếu là thêm địa chỉ mới thì sẽ gọi dữ liệu từ server sau đó tạo ra đối tượng UserAddressDTO và truyền lat/lng vào
    private final MutableLiveData<List<ShipFeeByAddressDTO>> _shipFeeByAddresses = new MutableLiveData<>();
    public final LiveData<List<ShipFeeByAddressDTO>> shipFeeByAddresses = _shipFeeByAddresses;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    public AddOrUpdateUserAddressViewModel(
            IUserAddressManageRepository userAddressManageRepository,
            IShipFeeByAddressRepository shipFeeByAddressRepository
    ) {
        this.userAddressManageRepository = userAddressManageRepository;
        this.shipFeeByAddressRepository = shipFeeByAddressRepository;
    }

    //Trong trường hợp UserAddressManage có truyền UserAddressDTO tức là nó sẽ có dữ liệu cập nhật
    //Nếu là thêm địa chỉ thì nó sẽ tự đi tạo đối tượng mới trong AddOrUpdateUserAddressDTO
    public void setUserAddressState(UserAddressDTO userAddressDTO) {
        _userAddress.setValue(userAddressDTO);
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(boolean isUpdate) {
        this.isUpdate = isUpdate;
    }

    public void setLoadingState(Boolean bool) {
        _isLoading.setValue(bool);
    }

    public boolean isReturnFromAddressDelivery() {
        return isReturnFromAddressDelivery;
    }

    public void setReturnFromAddressDelivery(boolean returnFromAddressDelivery) {
        isReturnFromAddressDelivery = returnFromAddressDelivery;
    }

    //Nếu trạng thái là thêm địa chỉ thì đi kéo data và gọi để tạo UserAddressDTO mới
    //Nếu trạng thái là cập nhật thì bỏ qua
    public void getShipFeeByAddresses() {
        if(isUpdate) return;
        _isLoading.setValue(true);

        shipFeeByAddressRepository.getShipFeeByAddresses().enqueue(new Callback<List<ShipFeeByAddressDTO>>() {
            @Override
            public void onResponse(
                    @NonNull Call<List<ShipFeeByAddressDTO>> call,
                    @NonNull Response<List<ShipFeeByAddressDTO>> response
            ) {
                if (response.isSuccessful()) {
                    List<ShipFeeByAddressDTO> succ = response.body();
                    if (succ != null && !succ.isEmpty())
                        _shipFeeByAddresses.setValue(succ);
                } else {
                    Gson gson = new Gson();
                    ResponseBody responseBody = response.errorBody();
                    if (responseBody == null) {
                        _notifyMsg.setValue("Không thể hiểu lỗi");
                        return;
                    }
                    ExceptionResponseDTO exc = null;
                    try {
                        exc = gson.fromJson(responseBody.string(), ExceptionResponseDTO.class);
                        if(exc == null) return;
                        _notifyMsg.setValue(exc.getMessage());
                    } catch (IOException e) {
                        _notifyMsg.setValue(e.getMessage());
                    }
                }
                _isLoading.setValue(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<ShipFeeByAddressDTO>> call, @NonNull Throwable throwable) {
                _isLoading.setValue(false);
                _notifyMsg.setValue(throwable.getMessage());
            }
        });
    }

    public void resetAllStates() {
        _isLoading.setValue(null);
        _userAddress.setValue(null);
        _notifyMsg.setValue(null);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private IUserAddressManageRepository userAddressManageRepository;
        private IShipFeeByAddressRepository shipFeeByAddressRepository;

        public Factory(
                IUserAddressManageRepository userAddressManageRepository,
                IShipFeeByAddressRepository shipFeeByAddressRepository
        ) {
            this.userAddressManageRepository = userAddressManageRepository;
            this.shipFeeByAddressRepository = shipFeeByAddressRepository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AddOrUpdateUserAddressViewModel.class))
                return (T) new AddOrUpdateUserAddressViewModel(userAddressManageRepository, shipFeeByAddressRepository);
            throw new IllegalArgumentException("Model class illegal");
        }
    }
}
