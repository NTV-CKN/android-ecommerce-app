package com.infix.phukiencongnghe.data.source.remote.admin.product;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;
import com.infix.phukiencongnghe.data.model.ImageUploadWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdminRemoteDataSourceImpl implements IProductAdminRemoteDataSource {
    private final ProductAdminService productAdminService;
    private final FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    public ProductAdminRemoteDataSourceImpl(ProductAdminService productAdminService) {
        this.productAdminService = productAdminService;
    }

    @Override
    public Call<PageResponseDTO<ProductAdminPageDTO>> loadProducts(String keyWord, String nameCategory, int page, int pageSize) {
        return productAdminService.loadProducts(keyWord, nameCategory, page, pageSize);
    }

    @Override
    public void uploadImagesAndSaveProduct(List<ImageUploadWrapper> uploadWrappers, ProductAdminPageDTO productDTO, Consumer<String> callback) {
        List<Task<Uri>> uploadTasks = new ArrayList<>();

        if (productDTO.getImages() == null) {
            productDTO.setImages(new ArrayList<>());
        } else {
            productDTO.getImages().clear();
        }

        for (ImageUploadWrapper item : uploadWrappers) {
            if (item.getLocalUri() == null) continue;

            StorageReference storageRef = firebaseStorage.getReference().child(item.getStoragePath());

            Task<Uri> pipelineTask = storageRef.putFile(item.getLocalUri())
                    .continueWithTask(task -> {
                        if (!task.isSuccessful() && task.getException() != null) {
                            callback.accept("Lỗi: " + task.getException().getMessage());
                            return null;
                        }
                        return storageRef.getDownloadUrl();
                    })
                    .addOnSuccessListener(downloadUrl -> {
                        String firebaseUrl = downloadUrl.toString();
                        switch (item.getType()) {
                            case "MAIN":
                                productDTO.setMainImage(firebaseUrl);
                                break;
                            case "SUB":
                                productDTO.getImages().add(firebaseUrl);
                                break;
                            case "VARIANT":
                                if (productDTO.getProductVariantDTOS() != null) {
                                    for (ProductVariantDTO variantDTO : productDTO.getProductVariantDTOS()) {
                                        if (variantDTO.getSku() != null && variantDTO.getSku().equals(item.getVariantSku())) {
                                            variantDTO.setImageUrl(firebaseUrl);
                                            break;
                                        }
                                    }
                                }
                                break;
                        }
                    });

            uploadTasks.add(pipelineTask);
        }

        if (uploadTasks.isEmpty()) {
            callback.accept("Ảnh không có");
            return;
        }

        Tasks.whenAllSuccess(uploadTasks)
                .addOnSuccessListener(results -> {
                    productAdminService.saveProduct(productDTO).enqueue(new Callback<SuccessBasicDTO>() {
                        @Override
                        public void onResponse(Call<SuccessBasicDTO> call, Response<SuccessBasicDTO> response) {
                            if (response.isSuccessful()) {
                                SuccessBasicDTO succ = response.body();
                                if (succ != null)
                                    callback.accept(succ.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<SuccessBasicDTO> call, Throwable throwable) {
                            callback.accept(throwable.getMessage());
                        }
                    });
                })
                .addOnFailureListener(e -> {
                    callback.accept(e.getMessage());
                });
    }

    @Override
    public Call<Map<String, String>> generateUniqueSku(String productName, String color, String size) {
        return productAdminService.generateUniqueSku(productName, color, size);
    }
}
