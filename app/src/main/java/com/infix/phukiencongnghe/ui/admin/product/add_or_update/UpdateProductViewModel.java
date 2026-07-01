package com.infix.phukiencongnghe.ui.admin.product.add_or_update;

import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.data.model.ImageUploadWrapper;
import com.infix.phukiencongnghe.data.repository.admin.product.IProductAdminRepository;
import com.infix.phukiencongnghe.utils.AppExecutors;
import java.util.ArrayList;
import java.util.List;

public class UpdateProductViewModel extends ViewModel {
    private final IProductAdminRepository productAdminRepository;

    private final MutableLiveData<String> _notifyMsg = new MutableLiveData<>();
    public LiveData<String> notifyMsg = _notifyMsg;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>();
    public LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<Uri> _mainImageUri = new MutableLiveData<>();
    public LiveData<Uri> mainImageUri = _mainImageUri;

    private final List<CategoryDTO> selectedCategories = new ArrayList<>();

    private final MutableLiveData<ProductAdminPageDTO> _productAdminPageDTO = new MutableLiveData<>();
    public LiveData<ProductAdminPageDTO> productAdminPageDTO = _productAdminPageDTO;

    public UpdateProductViewModel(IProductAdminRepository productAdminRepository) {
        this.productAdminRepository = productAdminRepository;
    }

    public LiveData<Uri> getMainImageUri() {
        return _mainImageUri;
    }

    public void setMainImageUri(Uri uri) {
        _mainImageUri.setValue(uri);
    }

    public List<CategoryDTO> getSelectedCategories() {
        return selectedCategories;
    }

    public void addCategory(CategoryDTO categoryDTO) {
        if (!selectedCategories.contains(categoryDTO)) {
            selectedCategories.add(categoryDTO);
        }
    }

    public void removeCategory(CategoryDTO categoryDTO) {
        selectedCategories.remove(categoryDTO);
    }

    public void updateProduct(ProductAdminPageDTO productAdminPageDTO, List<ImageUploadWrapper> uploadWrappers) {
        _isLoading.setValue(true);
        productAdminRepository.uploadImagesAndSaveProduct(
                uploadWrappers,
                productAdminPageDTO,
                msg -> AppExecutors.getInstance().mainThread().execute(() -> {
                    _notifyMsg.setValue(msg);
                    _isLoading.setValue(false);
                })
        );
    }

    public void resetAllState() {
        _isLoading.setValue(null);
        _notifyMsg.setValue(null);
        _mainImageUri.setValue(null);
        _productAdminPageDTO.setValue(null);
        selectedCategories.clear();
    }

    public void setProductAdminPageDTOState(ProductAdminPageDTO productAdminPageDTO) {
        _productAdminPageDTO.setValue(productAdminPageDTO);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final IProductAdminRepository productAdminRepository;

        public Factory(IProductAdminRepository productAdminRepository) {
            this.productAdminRepository = productAdminRepository;
        }

        @NonNull
        @Override
        @SuppressWarnings("unchecked")
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(UpdateProductViewModel.class)) {
                return (T) new UpdateProductViewModel(productAdminRepository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
        }
    }
}