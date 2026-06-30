package com.infix.phukiencongnghe.ui.admin.product.add_or_update;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.data.dto.ProductAdminPageDTO;
import com.infix.phukiencongnghe.data.dto.response.ProductVariantDTO;
import com.infix.phukiencongnghe.databinding.FragmentAddOrUpdateProductBinding;
import com.infix.phukiencongnghe.ui.dialog.LoadingDialog;
import com.infix.phukiencongnghe.utils.InjectUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

import java.util.function.Consumer;

public class AddOrUpdateProductFragment extends Fragment {

    private FragmentAddOrUpdateProductBinding binding;
    private AddOrUpdateProductViewModel viewModel;
    private VariantInputAdapter variantAdapter;
    private LoadingDialog loadingDialog;

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
        viewModel = new ViewModelProvider(requireActivity(), factory).get(AddOrUpdateProductViewModel.class);

        if (getArguments() != null) {
            boolean isEdit = getArguments().getBoolean("IS_UPDATE_MODE", false);
            viewModel.setUpdateMode(isEdit);

        }

        setupUIBehaviors();
        setupRecyclerView();
        observeAOUProductVM();

        viewModel.getVariantsLiveData().observe(getViewLifecycleOwner(), list -> {
            variantAdapter.updateList(list, viewModel.isUpdate());
        });
    }

    private void observeAOUProductVM() {
        viewModel.notifyMsg.observe(getViewLifecycleOwner(), msg -> {
            if(msg == null) return;
            SnackbarUtils.showBaseSnackbar(
                    binding.getRoot(),
                    msg,
                    Snackbar.LENGTH_SHORT
            );
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
            ProductAdminPageDTO finalDTO = viewModel.prepareProductDTO(
                    binding.edtNameInfo.getText().toString(),
                    binding.edtSubtitleInfo.getText().toString(),
                    binding.edtDescInfo.getText().toString(),
                    binding.edtWarrantyInfo.getText().toString()
            );

        });
    }

    private void setupRecyclerView() {
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
            }
        });

        binding.rvInputVariants.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvInputVariants.setAdapter(variantAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}