package com.infix.phukiencongnghe.ui.admin.product.add_or_update;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;
import com.infix.phukiencongnghe.data.repository.admin.product.IProductAdminRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOrUpdateProductViewModel extends ViewModel {
    private final IProductAdminRepository productAdminRepository;

    private boolean isUpdate = false;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    private final ProductAdminPageDTO productDTO = new ProductAdminPageDTO();
    private final MutableLiveData<List<ProductVariantDTO>> variantsLiveData = new MutableLiveData<>(new ArrayList<>());

    public AddOrUpdateProductViewModel(IProductAdminRepository productAdminRepository
) {
        this.productAdminRepository = productAdminRepository;
        productDTO.setFolderId(UUID.randomUUID().toString());
    }

    public boolean isUpdate() { return isUpdate; }
    public void setUpdateMode(boolean isUpdate) { this.isUpdate = isUpdate; }

    public ProductAdminPageDTO getProductDTO() { return productDTO; }
    public LiveData<List<ProductVariantDTO>> getVariantsLiveData() { return variantsLiveData; }

    public void addNewVariantField() {
        if (isUpdate) return;

        List<ProductVariantDTO> currentList = variantsLiveData.getValue();
        if (currentList == null) currentList = new ArrayList<>();

        currentList.add(new ProductVariantDTO(null, "", "", BigDecimal.ZERO, 0, "", "", 0, null));
        variantsLiveData.setValue(currentList);
    }

    public void generateUniqueSku(String productName, String color, String size, Consumer<String> onSkuCompleted) {
        productAdminRepository.generateUniqueSku(productName, color, size).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                if(response.isSuccessful()) {
                    Map<String, String> map = response.body();
                    if(map != null)
                        onSkuCompleted.accept(map.get("sku"));
                    else
                        _notifyMsg.setValue("Body is null");
                }else
                    _notifyMsg.setValue("Lỗi không thể sinh sku");
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable throwable) {
                _notifyMsg.setValue(throwable.getMessage());
            }
        });
    }

    public void removeVariantField(int position) {
        if (isUpdate) return;
        List<ProductVariantDTO> currentList = variantsLiveData.getValue();
        if (currentList != null && position >= 0 && position < currentList.size()) {
            currentList.remove(position);
            variantsLiveData.setValue(currentList);
        }
    }

    public ProductAdminPageDTO prepareProductDTO(String name, String subtitle, String desc, String warranty) {
        productDTO.setName(name);
        productDTO.setSubtitle(subtitle);
        productDTO.setDescription(desc);
        try {
            productDTO.setWarrantyPeriod(Integer.parseInt(warranty));
        } catch (Exception e) {
            productDTO.setWarrantyPeriod(0);
        }
        productDTO.setProductVariantDTOS(variantsLiveData.getValue());
        return productDTO;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final IProductAdminRepository productAdminRepository;

        public Factory(
                       IProductAdminRepository productAdminRepository) {
            this.productAdminRepository = productAdminRepository;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AddOrUpdateProductViewModel.class)) {
                return (T) new AddOrUpdateProductViewModel(productAdminRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }

}