package com.infix.phukiencongnghe.ui.admin.product.add_or_update;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;
import com.infix.phukiencongnghe.data.model.ImageUploadWrapper;
import com.infix.phukiencongnghe.data.repository.admin.product.IProductAdminRepository;
import com.infix.phukiencongnghe.utils.AppExecutors;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductViewModel extends ViewModel {
    private final IProductAdminRepository productAdminRepository;

    private boolean isUpdate = false;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    //Quản lí ảnh từ user
    private final MutableLiveData<Uri> _mainImageUri = new MutableLiveData<>();
    public final LiveData<Uri> mainImageUri = _mainImageUri;

    private final MutableLiveData<List<Uri>> subImagesList = new MutableLiveData<>(new ArrayList<>());
    //key là vị trí
    private final Map<Integer, Uri> variantImagesMap = new HashMap<>();

    private final ProductAdminPageDTO productDTO = new ProductAdminPageDTO();
    private final MutableLiveData<List<ProductVariantDTO>> variantsLiveData = new MutableLiveData<>(new ArrayList<>());

    public AddProductViewModel(IProductAdminRepository productAdminRepository
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

    public List<ImageUploadWrapper> prepareAllUploadWrappers() {
        List<ImageUploadWrapper> allUploads = new ArrayList<>();
        String folderId = productDTO.getFolderId();

        if (_mainImageUri.getValue() != null) {
            String mainPath = "products/" + folderId + "/main_image.jpg";
            allUploads.add(new ImageUploadWrapper(_mainImageUri.getValue(), mainPath, "MAIN", null));
        }

        if (subImagesList.getValue() != null) {
            List<Uri> subs = subImagesList.getValue();
            for (int i = 0; i < subs.size(); i++) {
                String subPath = "products/" + folderId + "/sub_images/sub_" + i + ".jpg";
                allUploads.add(new ImageUploadWrapper(subs.get(i), subPath, "SUB", null));
            }
        }

        List<ProductVariantDTO> currentVariants = variantsLiveData.getValue();
        if (currentVariants != null) {
            for (int i = 0; i < currentVariants.size(); i++) {
                Uri variantUri = variantImagesMap.get(i);
                if (variantUri != null) {
                    ProductVariantDTO variantDTO = currentVariants.get(i);
                    String sku = variantDTO.getSku();
                    String variantPath = "products/" + folderId + "/variants/" + sku + ".jpg";
                    allUploads.add(new ImageUploadWrapper(variantUri, variantPath, "VARIANT", sku));
                }
            }
        }

        return allUploads;
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

    public void setVariantImageUri(int position, Uri uri) {
        variantImagesMap.put(position, uri);
        List<ProductVariantDTO> currentList = variantsLiveData.getValue();
        if (currentList != null && position >= 0 && position < currentList.size()) {
            currentList.get(position).setImageUrl(uri.toString());
            variantsLiveData.setValue(currentList);
        }
    }

    public void setMainImageUri(Uri uri) { _mainImageUri.setValue(uri); }

    public void addSubImages(List<Uri> uris) {
        List<Uri> current = subImagesList.getValue();
        if (current == null) current = new ArrayList<>();
        current.addAll(uris);
        subImagesList.setValue(current);
    }

    public void addCategory(CategoryDTO categoryDTO) {
        if(this.productDTO.getCategoriesDTOS()==null) this.productDTO.setCategoriesDTOS(new ArrayList<>());

        this.productDTO.getCategoriesDTOS().clear();
        this.productDTO.getCategoriesDTOS().add(categoryDTO);
    }

    public void saveProduct(ProductAdminPageDTO productAdminPageDTO, List<ImageUploadWrapper> uploadWrappers) {
        _isLoading.setValue(true);
        productAdminRepository.uploadImagesAndSaveProduct(
                uploadWrappers,
                productAdminPageDTO,
                s -> {
                    AppExecutors.getInstance().mainThread().execute(() -> {
                        _notifyMsg.setValue(s);
                        _isLoading.setValue(false);
                    });
                }
        );
    }

    public void resetAllState() {
        _isLoading.setValue(null);
        _notifyMsg.setValue(null);
        _mainImageUri.setValue(null);
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
            if (modelClass.isAssignableFrom(AddProductViewModel.class)) {
                return (T) new AddProductViewModel(productAdminRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }

}