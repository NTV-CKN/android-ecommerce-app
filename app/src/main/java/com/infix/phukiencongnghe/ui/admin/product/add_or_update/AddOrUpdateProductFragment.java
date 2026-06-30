package com.infix.phukiencongnghe.ui.admin.product.add_or_update;

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
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;
import com.infix.phukiencongnghe.data.model.ImageUploadWrapper;
import com.infix.phukiencongnghe.data.repository.common.category.CategoryRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.common.product.FeatureProductRepositoryImpl;
import com.infix.phukiencongnghe.databinding.FragmentAddOrUpdateProductBinding;
import com.infix.phukiencongnghe.ui.adapter.categories.CategoryAdapter;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.ui.product_category.ProductCategoryViewModel;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AddOrUpdateProductFragment extends Fragment {

    private FragmentAddOrUpdateProductBinding binding;

    private AddOrUpdateProductViewModel viewModel;
    private ProductCategoryViewModel productCategoryViewModel;

    private VariantInputAdapter variantAdapter;
    private CategoryAdapter categoryAdapter;

    private LoadingDialog loadingDialog;

    private int targetingVariantPosition = -1;

    private final ActivityResultLauncher<PickVisualMediaRequest> pickMainImageLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    Glide.with(requireContext())
                            .load(uri)
                            .error(R.drawable.ic_launcher_background)
                            .into(binding.ivMainPhoto);
                    viewModel.setMainImageUri(uri);
                }
            });

    private final ActivityResultLauncher<PickVisualMediaRequest> pickVariantImageLauncher =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null && targetingVariantPosition != -1) {
                    viewModel.setVariantImageUri(targetingVariantPosition, uri);
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddOrUpdateProductBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AddOrUpdateProductViewModel.Factory factory =
                new AddOrUpdateProductViewModel.Factory(
                        InjectUtils.createProductAdminRepository()
                );
        loadingDialog = new LoadingDialog();
        viewModel = new ViewModelProvider(this, factory).get(AddOrUpdateProductViewModel.class);

        if (getArguments() != null) {
            boolean isEdit = getArguments().getBoolean("IS_UPDATE_MODE", false);
            viewModel.setUpdateMode(isEdit);

        }

        setupUIBehaviors();
        setupRecyclerView();
        observeAOUProductVM();
        setEvent();
        initProductCategoryVM();

        viewModel.getVariantsLiveData().observe(getViewLifecycleOwner(), list -> {
            variantAdapter.updateList(list, viewModel.isUpdate());
        });
    }

    private void initProductCategoryVM() {
        ProductCategoryViewModel.Factory factory =
                new ProductCategoryViewModel.Factory(
                        InjectUtils.createCategoryRepository(),
                        InjectUtils.createProductRepository()
                );
        productCategoryViewModel = new ViewModelProvider(this, factory).get(ProductCategoryViewModel.class);

        //category
        productCategoryViewModel.loadCategories();
        productCategoryViewModel.categories.observe(getViewLifecycleOwner(), categoryDTOS -> {
            if(categoryDTOS == null) return;
            categoryAdapter.update(categoryDTOS);
        });
    }

    private void observeAOUProductVM() {
        viewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if (msg == null) return;
            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    msg,
                    Snackbar.LENGTH_SHORT
            );
        });

        viewModel.isLoading.observe(getViewLifecycleOwner(), isLoading -> {
            if(isLoading == null) return;

            if(isLoading)
                loadingDialog.show(requireActivity().getSupportFragmentManager(), null);
            else
                loadingDialog.dismiss();
        });
    }

    private void setupUIBehaviors() {
        if (viewModel.isUpdate()) {
            binding.btnAddVariantField.setVisibility(View.GONE);
        } else {
            binding.btnAddVariantField.setVisibility(View.VISIBLE);
        }

        binding.btnAddVariantField.setOnClickListener(v -> viewModel.addNewVariantField());

        //save total
        binding.btnSaveTotal.setOnClickListener(v -> {
            if (validateBeforeSave()) {
                List<ImageUploadWrapper> readyUploads = viewModel.prepareAllUploadWrappers();
                ProductAdminPageDTO currentDto = viewModel.prepareProductDTO(
                        binding.edtNameInfo.getText().toString(),
                        binding.edtSubtitleInfo.getText().toString(),
                        binding.edtDescInfo.getText().toString(),
                        binding.edtWarrantyInfo.getText().toString()
                );

                viewModel.saveProduct(currentDto, readyUploads);
            }

        });

    }

    private void setupRecyclerView() {
        //variant
        variantAdapter = new VariantInputAdapter(new VariantInputAdapter.OnVariantActionListener() {
            @Override
            public void onDelete(int position) {
                viewModel.removeVariantField(position);
            }

            @Override
            public void onGenerateSkuRequested(int position, ProductVariantDTO item) {
                String mainProductName = binding.edtNameInfo.getText().toString().trim();
                String color = item.getColor() != null ? item.getColor().trim() : "";
                String size = item.getSize() != null ? item.getSize().trim() : "";

                if (mainProductName.isEmpty()) {
                    binding.edtNameInfo.setError("Vui lòng điền tên sản phẩm chính trước khi sinh mã SKU!");
                    binding.edtNameInfo.requestFocus();
                    return;
                }

                if (color.isEmpty() || size.isEmpty()) {
                    Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin 'Màu' và 'Size' của biến thể này!", Toast.LENGTH_LONG).show();
                    return;
                }

                viewModel.generateUniqueSku(mainProductName, color, size, s -> {
                    item.setSku(s);
                    variantAdapter.triggerNotifyItemChanged(position);
                });

            }

            @Override
            public void onSelectImage(int position, ProductVariantDTO item) {
                if (item.getSku() == null || item.getSku().trim().isEmpty()) {
                    Toast.makeText(requireContext(), "Vui lòng nhấn nút sinh mã SKU cho biến thể này trước khi chọn ảnh!", Toast.LENGTH_LONG).show();
                    return;
                }
                targetingVariantPosition = position;

                pickVariantImageLauncher.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        binding.rvInputVariants.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvInputVariants.setAdapter(variantAdapter);

        //category
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {
            viewModel.addCategory(category);
        });

        binding.rvCategory.setAdapter(categoryAdapter);
    }

    private void setEvent() {
        binding.ivMainPhoto.setOnClickListener(v -> {
            pickMainImageLauncher.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());
        });
    }

    private boolean validateBeforeSave() {
        String name = binding.edtNameInfo.getText().toString().trim();
        if (name.isEmpty()) {
            binding.edtNameInfo.setError("Tên sản phẩm chung không được để trống!");
            binding.edtNameInfo.requestFocus();
            return false;
        }

        String subtitle = binding.edtSubtitleInfo.getText().toString().trim();
        if (subtitle.isEmpty()) {
            binding.edtSubtitleInfo.setError("Tiêu đề phụ không được để trống!");
            binding.edtSubtitleInfo.requestFocus();
            return false;
        }

        String warranty = binding.edtWarrantyInfo.getText().toString().trim();
        if (warranty.isEmpty()) {
            binding.edtWarrantyInfo.setError("Vui lòng nhập thời gian bảo hành!");
            binding.edtWarrantyInfo.requestFocus();
            return false;
        }
        try {
            int months = Integer.parseInt(warranty);
            if (months < 0) {
                binding.edtWarrantyInfo.setError("Thời gian bảo hành không được là số âm!");
                binding.edtWarrantyInfo.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            binding.edtWarrantyInfo.setError("Thời gian bảo hành phải là một số nguyên hợp lệ!");
            binding.edtWarrantyInfo.requestFocus();
            return false;
        }

        String desc = binding.edtDescInfo.getText().toString().trim();
        if (desc.isEmpty()) {
            binding.edtDescInfo.setError("Mô tả sản phẩm không được để trống!");
            binding.edtDescInfo.requestFocus();
            return false;
        }

        if (!viewModel.isUpdate() && viewModel.mainImageUri.getValue() == null) {
            Toast.makeText(requireContext(), "Vui lòng chọn Ảnh chính cho sản phẩm!", Toast.LENGTH_LONG).show();
            binding.getRoot().findViewById(R.id.iv_main_photo).requestFocus();
            return false;
        }

        List<ProductVariantDTO> variants = viewModel.getVariantsLiveData().getValue();
        if (variants == null || variants.isEmpty()) {
            Toast.makeText(requireContext(), "Sản phẩm phải có ít nhất 1 biến thể!", Toast.LENGTH_LONG).show();
            return false;
        }

        for (int i = 0; i < variants.size(); i++) {
            ProductVariantDTO variant = variants.get(i);
            int itemIndex = i + 1;
            if (variant.getSku() == null || variant.getSku().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Biến thể dòng số " + itemIndex + " chưa được sinh mã SKU!", Toast.LENGTH_LONG).show();
                binding.rvInputVariants.scrollToPosition(i);
                return false;
            }

            if (variant.getName() == null || variant.getName().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Vui lòng nhập tên hiển thị cho biến thể dòng số " + itemIndex, Toast.LENGTH_LONG).show();
                binding.rvInputVariants.scrollToPosition(i);
                return false;
            }

            if (variant.getPrice() == null || variant.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                Toast.makeText(requireContext(), "Giá tiền biến thể dòng số " + itemIndex + " phải lớn hơn 0đ!", Toast.LENGTH_LONG).show();
                binding.rvInputVariants.scrollToPosition(i);
                return false;
            }

            if (variant.getStock() == null || variant.getStock() < 0) {
                Toast.makeText(requireContext(), "Số lượng kho biến thể dòng số " + itemIndex + " không hợp lệ!", Toast.LENGTH_LONG).show();
                binding.rvInputVariants.scrollToPosition(i);
                return false;
            }

            if (variant.getGram() == null || variant.getGram() <= 0) {
                Toast.makeText(requireContext(), "Trọng lượng dòng số " + itemIndex + " phải lớn hơn 0 gram để tính toán phí vận chuyển!", Toast.LENGTH_LONG).show();
                binding.rvInputVariants.scrollToPosition(i);
                return false;
            }

            if (!viewModel.isUpdate() && (variant.getImageUrl() == null || variant.getImageUrl().trim().isEmpty())) {
                Toast.makeText(requireContext(), "Vui lòng chọn hình ảnh minh họa cho biến thể dòng số " + itemIndex, Toast.LENGTH_LONG).show();
                binding.rvInputVariants.scrollToPosition(i);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        viewModel.resetAllState();
    }
}