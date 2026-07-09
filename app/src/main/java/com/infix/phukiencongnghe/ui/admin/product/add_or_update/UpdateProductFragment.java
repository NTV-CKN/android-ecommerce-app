package com.infix.phukiencongnghe.ui.admin.product.add_or_update;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;
import com.infix.phukiencongnghe.data.model.ImageUploadWrapper;
import com.infix.phukiencongnghe.data.repository.common.category.CategoryRepositoryImpl;
import com.infix.phukiencongnghe.databinding.FragmentAddOrUpdateProductBinding;
import com.infix.phukiencongnghe.ui.adapter.categories.CategoryAdapter;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.ui.product_category.ProductCategoryViewModel;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class UpdateProductFragment extends Fragment {
    private FragmentAddOrUpdateProductBinding binding;
    private UpdateProductViewModel viewModel;
    private ProductCategoryViewModel productCategoryViewModel;

    private CategoryAdapter categoryAdapter;
    private UpdateVariantInputAdapter variantInputAdapter;
    private LoadingDialog loadingDialog;

    private ProductAdminPageDTO currentProduct;
    private final Map<String, Uri> variantLocalUriMap = new HashMap<>();
    private int currentSelectingVariantPosition = -1;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMainImageLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    viewModel.setMainImageUri(uri);
                    binding.ivMainPhoto.setImageURI(uri);
                }
            });

    private final ActivityResultLauncher<PickVisualMediaRequest> pickVariantImageLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null && currentSelectingVariantPosition != -1) {
                    ProductVariantDTO variant = variantInputAdapter.getVariants().get(currentSelectingVariantPosition);
                    variantLocalUriMap.put(variant.getSku(), uri);
                    variantInputAdapter.triggerNotifyItemChanged(currentSelectingVariantPosition);
                }
            });

    public static UpdateProductFragment newInstance(ProductAdminPageDTO product) {
        UpdateProductFragment fragment = new UpdateProductFragment();
        fragment.currentProduct = product;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddOrUpdateProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModels();
        setupUI();
        observeViewModel();
        binding.btnSaveTotal.setText("CẬP NHẬT");
    }

    private void initViewModels() {
        viewModel = new ViewModelProvider(requireActivity(), new UpdateProductViewModel.Factory(
                InjectUtils.createProductAdminRepository()
        )).get(UpdateProductViewModel.class);

        productCategoryViewModel = new ViewModelProvider(this, new ProductCategoryViewModel.Factory(
                new CategoryRepositoryImpl(),
                InjectUtils.createProductRepository()
        )).get(ProductCategoryViewModel.class);
    }

    private void setupUI() {
        loadingDialog = new LoadingDialog();
        binding.btnAddVariantField.setVisibility(View.GONE);

        categoryAdapter = new CategoryAdapter(new ArrayList<>(), item -> {
            if (viewModel.getSelectedCategories().contains(item)) {
                viewModel.removeCategory(item);
            } else {
                viewModel.addCategory(item);
            }
            categoryAdapter.notifyDataSetChanged();
        });

        binding.rvCategory.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvCategory.setAdapter(categoryAdapter);

        // Setup RecyclerView Biến thể (dùng adapter riêng cho Update: khoá SKU, ẩn nút xoá,
        // ưu tiên hiển thị ảnh local vừa chọn qua PhotoPicker)
        variantInputAdapter = new UpdateVariantInputAdapter(new UpdateVariantInputAdapter.OnVariantActionListener() {
            @Override
            public void onSelectImage(int position, ProductVariantDTO item) {
                currentSelectingVariantPosition = position;
                pickVariantImageLauncher.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }

            @Override
            public void onDeleteVariant(int position, ProductVariantDTO item) {
                SnackbarUtils.showSnackbarWithAction(
                        binding.getRoot(),
                        "Bạn có chắc muốn xóa biến thể: " + item.getName() + " này không?",
                        Snackbar.LENGTH_LONG,
                        () -> {
                            viewModel.removeVariant(item, binding.getRoot().getContext(), () -> {
                                //chi goi khi xoa thanh cong
                                variantInputAdapter.getVariants().removeIf(variantDTO ->
                                        variantDTO.getId().equals(item.getId()));
                                variantInputAdapter.notifyDataSetChanged();
                            });
                        });
            }
        });

        variantInputAdapter.setLocalImageResolver(this::getLocalVariantUri);
        variantInputAdapter.updateList(new ArrayList<>());

        binding.rvInputVariants.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvInputVariants.setAdapter(variantInputAdapter);

        binding.ivMainPhoto.setOnClickListener(v -> pickMainImageLauncher.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build()));

        binding.btnSaveTotal.setOnClickListener(v -> handleUpdateLogic());
    }

    @SuppressLint("NotifyDataSetChanged")
    private void observeViewModel() {
        viewModel.isLoading.observe(getViewLifecycleOwner(), loading -> {
            if (loading != null && loading) {
                loadingDialog.show(requireActivity().getSupportFragmentManager(), null);
            } else if (loading != null) {
                loadingDialog.dismiss();
            }
        });

        viewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) {
                SnackbarUtils.showBaseSnackbar(binding.getRoot(), msg, Snackbar.LENGTH_LONG);
                if (msg.contains("thành công")) {
                    getParentFragmentManager().popBackStack();
                }
            }
        });

        productCategoryViewModel.categories.observe(getViewLifecycleOwner(), resource -> {
            if (resource != null) {
                categoryAdapter.update(resource);
                trySelectCategoriesForCurrentProduct(resource);
            }
        });
        productCategoryViewModel.loadCategories();

        viewModel.productAdminPageDTO.observe(getViewLifecycleOwner(), productAdminPageDTO -> {
            if (productAdminPageDTO == null) return;

            currentProduct = productAdminPageDTO;
            initDataToViews(productAdminPageDTO);
            // Phòng trường hợp danh sách category đã load xong TRƯỚC khi có currentProduct
            trySelectCategoriesForCurrentProduct(productCategoryViewModel.categories.getValue());
        });
    }

    private void initDataToViews(@NonNull ProductAdminPageDTO product) {
        binding.edtNameInfo.setText(product.getName());
        binding.edtSubtitleInfo.setText(product.getSubtitle());
        binding.edtWarrantyInfo.setText(product.getWarrantyPeriod() != null ? String.valueOf(product.getWarrantyPeriod()) : "");
        binding.edtDescInfo.setText(product.getDescription());

        if (product.getMainImage() != null && !product.getMainImage().trim().isEmpty()) {
            Glide.with(this)
                    .load(product.getMainImage())
                    .placeholder(R.drawable.ic_add_24px)
                    .error(R.drawable.ic_add_24px)
                    .into(binding.ivMainPhoto);
        } else {
            binding.ivMainPhoto.setImageResource(R.drawable.ic_add_24px);
        }

        // Việc chọn category theo đúng instance được xử lý ở trySelectCategoriesForCurrentProduct()
        // (không add trực tiếp category từ product ở đây để tránh lệch tham chiếu với categoryAdapter)

        if (product.getProductVariantDTOS() != null) {
            variantInputAdapter.updateList(new ArrayList<>(product.getProductVariantDTOS()));
        } else {
            variantInputAdapter.updateList(new ArrayList<>());
        }
    }

    private void trySelectCategoriesForCurrentProduct(@Nullable List<CategoryDTO> fullCategoryList) {
        if (currentProduct == null || currentProduct.getCategoriesDTOS() == null || fullCategoryList == null) {
            return;
        }

        viewModel.getSelectedCategories().clear();
        for (CategoryDTO productCat : currentProduct.getCategoriesDTOS()) {
            for (CategoryDTO fullCat : fullCategoryList) {
                if (Objects.equals(fullCat.getId(), productCat.getId())) {
                    viewModel.addCategory(fullCat);
                    break;
                }
            }
        }
        if (categoryAdapter != null) {
            categoryAdapter.notifyDataSetChanged();
        }
    }

    private void handleUpdateLogic() {
        if (!validateData()) return;

        currentProduct.setName(binding.edtNameInfo.getText().toString().trim());
        currentProduct.setSubtitle(binding.edtSubtitleInfo.getText().toString().trim());
        currentProduct.setWarrantyPeriod(Integer.parseInt(binding.edtWarrantyInfo.getText().toString().trim()));
        currentProduct.setDescription(binding.edtDescInfo.getText().toString().trim());
        currentProduct.setCategoriesDTOS(viewModel.getSelectedCategories());
        currentProduct.setImages(new ArrayList<>());

        List<ImageUploadWrapper> wrappers = new ArrayList<>();
        String folderId = currentProduct.getFolderId();

        if (viewModel.getMainImageUri().getValue() != null) {
            wrappers.add(new ImageUploadWrapper(
                    viewModel.getMainImageUri().getValue(),
                    "products/" + folderId + "/main_image.jpg",
                    "MAIN",
                    null
            ));
        }

        List<ProductVariantDTO> adapterVariants = variantInputAdapter.getVariants();
        currentProduct.setProductVariantDTOS(adapterVariants);

        for (ProductVariantDTO variant : adapterVariants) {
            Uri localUri = variantLocalUriMap.get(variant.getSku());
            if (localUri != null) {
                // Nếu biến thể được chọn ảnh cục bộ mới thông qua PhotoPicker
                wrappers.add(new ImageUploadWrapper(
                        localUri,
                        "products/" + folderId + "/variants/" + variant.getSku() + ".jpg",
                        "VARIANT",
                        variant.getSku()
                ));
            }
        }

        viewModel.updateProduct(currentProduct, wrappers);
    }

    private boolean validateData() {
        if (binding.edtNameInfo.getText().toString().trim().isEmpty() ||
                binding.edtSubtitleInfo.getText().toString().trim().isEmpty() ||
                binding.edtWarrantyInfo.getText().toString().trim().isEmpty() ||
                binding.edtDescInfo.getText().toString().trim().isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin sản phẩm chung!", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (viewModel.getSelectedCategories().isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng chọn ít nhất 1 danh mục!", Toast.LENGTH_SHORT).show();
            return false;
        }

        List<ProductVariantDTO> list = variantInputAdapter.getVariants();
        if (list.isEmpty()) {
            Toast.makeText(requireContext(), "Sản phẩm phải có ít nhất 1 biến thể!", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (int i = 0; i < list.size(); i++) {
            ProductVariantDTO variant = list.get(i);
            int itemIndex = i + 1;

            if (variant.getSku() == null || variant.getSku().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Biến thể dòng số " + itemIndex + " thiếu SKU!", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (variant.getName() == null || variant.getName().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập tên biến thể dòng số " + itemIndex, Toast.LENGTH_SHORT).show();
                return false;
            }
            if (variant.getPrice() == null || variant.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                Toast.makeText(requireContext(), "Giá biến thể dòng số " + itemIndex + " phải lớn hơn 0đ!", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (variant.getStock() == null || variant.getStock() < 0) {
                Toast.makeText(requireContext(), "Số lượng kho biến thể dòng số " + itemIndex + " không hợp lệ!", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (variant.getGram() == null || variant.getGram() <= 0) {
                Toast.makeText(requireContext(), "Trọng lượng dòng số " + itemIndex + " phải lớn hơn 0 gram!", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    public Uri getLocalVariantUri(String sku) {
        return variantLocalUriMap.get(sku);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel.resetAllState();
    }
}