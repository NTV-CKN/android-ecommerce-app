package com.infix.phukiencongnghe.data.source.remote.admin.product;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.infix.phukiencongnghe.common.OnCallbackListener;
import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.PageResponseDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;
import com.infix.phukiencongnghe.data.dto.response.SuccessBasicDTO;
import com.infix.phukiencongnghe.data.model.ImageUploadWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

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
        if (productDTO.getImages() == null) {
            productDTO.setImages(new ArrayList<>());
        } else {
            productDTO.getImages().clear();
        }

        List<Task<Uri>> uploadTasks = new ArrayList<>();

        for (ImageUploadWrapper item : uploadWrappers) {
            if (item == null || item.getLocalUri() == null) continue;

            StorageReference storageRef = firebaseStorage.getReference().child(item.getStoragePath());

            Task<Uri> pipelineTask = storageRef.putFile(item.getLocalUri())
                    .continueWithTask(task -> {
                        if (!task.isSuccessful()) {
                            Exception e = task.getException();
                            throw (e != null) ? e : new IllegalStateException("Tải ảnh lên thất bại: " + item.getStoragePath());
                        }
                        return storageRef.getDownloadUrl();
                    })
                    .addOnSuccessListener(downloadUrl -> applyUploadedUrl(productDTO, item, downloadUrl.toString()));

            uploadTasks.add(pipelineTask);
        }

        if (uploadTasks.isEmpty()) {
            callSaveProductApi(productDTO, callback);
            return;
        }

        Tasks.whenAllSuccess(uploadTasks)
                .addOnSuccessListener(results -> callSaveProductApi(productDTO, callback))
                .addOnFailureListener(e -> callback.accept("Lỗi tải ảnh lên: " + e.getMessage()));
    }

    private void applyUploadedUrl(ProductAdminPageDTO productDTO, ImageUploadWrapper item, String firebaseUrl) {
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
    }

    private void callSaveProductApi(ProductAdminPageDTO productDTO, Consumer<String> callback) {
        productAdminService.saveProduct(productDTO).enqueue(new Callback<SuccessBasicDTO>() {
            @Override
            public void onResponse(Call<SuccessBasicDTO> call, Response<SuccessBasicDTO> response) {
                if (response.isSuccessful()) {
                    SuccessBasicDTO succ = response.body();
                    callback.accept(succ != null ? succ.getMessage() : "Cập nhật sản phẩm thành công");
                } else {
                    callback.accept("Lỗi máy chủ (" + response.code() + "), vui lòng thử lại!");
                }
            }

            @Override
            public void onFailure(Call<SuccessBasicDTO> call, Throwable throwable) {
                callback.accept("Lỗi kết nối: " + throwable.getMessage());
            }
        });
    }

    @Override
    public Call<Map<String, String>> generateUniqueSku(String productName, String color, String size) {
        return productAdminService.generateUniqueSku(productName, color, size);
    }

    @Override
    public void removeVariant(String path, Context context, ProductVariantDTO productVariantDTO, OnCallbackListener callbackListener) throws Exception {
        StorageReference ref = firebaseStorage.getReference().child(path);
        ref.delete()
                .addOnSuccessListener(unused -> {
                    productAdminService.removeVariant(productVariantDTO).enqueue(new Callback<SuccessBasicDTO>() {
                        @Override
                        public void onResponse(Call<SuccessBasicDTO> call, Response<SuccessBasicDTO> response) {
                            if (response.isSuccessful()) {
                                SuccessBasicDTO successBasicDTO = response.body();
                                Toast.makeText(
                                        context,
                                        successBasicDTO.getMessage(),
                                        Toast.LENGTH_SHORT
                                ).show();
                                callbackListener.onRevoke();
                            }
                        }

                        @Override
                        public void onFailure(Call<SuccessBasicDTO> call, Throwable throwable) {
                            Toast.makeText(
                                    context,
                                    throwable.getMessage(),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });
                }).addOnFailureListener(e -> {
                    Log.e("SVU", e.getMessage());
                });
    }
}