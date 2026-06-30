package com.infix.phukiencongnghe.ui.admin.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.databinding.FragmentProductManageAdminBinding;
import com.infix.phukiencongnghe.ui.adapter.categories.CategoryAdapter;
import com.infix.phukiencongnghe.utils.InjectUtils;

import java.util.ArrayList;

public class ProductManageAdminFragment extends Fragment {
    private FragmentProductManageAdminBinding binding;

    private ProductManageAdminViewModel productManageAdminViewModel;

    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProductManageAdminBinding.inflate(
                inflater,
                container,
                false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRvCategory();
        initAndObserveProductManageAdminVM();
    }

    private void initRvCategory() {
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), category -> {

        });

        binding.rvCategory.setAdapter(categoryAdapter);
    }

    private void initAndObserveProductManageAdminVM() {
        //init view model
        ProductManageAdminViewModel.Factory factory =
                new ProductManageAdminViewModel.Factory(
                        InjectUtils.createCategoryRepository(),
                        InjectUtils.createProductAdminRepository()
                );

        productManageAdminViewModel = new ViewModelProvider(requireActivity(), factory)
                .get(ProductManageAdminViewModel.class);

        //observe categories
        productManageAdminViewModel.loadCategoriesAvailable();
        productManageAdminViewModel.categories.observe(
                getViewLifecycleOwner(),
                categoryDTOS -> {
                    if(categoryDTOS == null) return;

                    categoryAdapter.update(categoryDTOS);
        });

    }
}