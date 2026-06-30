package com.infix.phukiencongnghe.ui.admin.product;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.databinding.FragmentProductManageAdminBinding;
import com.infix.phukiencongnghe.ui.adapter.admin.product.ProductAdminAdapter;
import com.infix.phukiencongnghe.ui.adapter.categories.CategoryAdapter;
import com.infix.phukiencongnghe.utils.InjectUtils;

import java.util.ArrayList;
import java.util.List;

public class ProductManageAdminFragment extends Fragment {
    private FragmentProductManageAdminBinding binding;

    private ProductManageAdminViewModel productManageAdminViewModel;
    private ProductAdminAdapter productAdminAdapter;

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
        initRvProductsAdmin();
        initAndObserveProductManageAdminVM();
        observeCurrentPageAndPageSize();
        setEventForViewPaginationBar();
        setSearchViewChange();
    }

    private void setEventForViewPaginationBar() {
        binding.paginationBarView.setOnPageChangeListener(newPage -> {
            String keyWord = binding.svSearchProduct.getText().toString();

            loadProducts(
                    keyWord,
                    newPage
            );
        });
    }

    private void initRvProductsAdmin() {
        productAdminAdapter = new ProductAdminAdapter(product -> {

        });

        binding.rvAdminProducts.setAdapter(productAdminAdapter);
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

        loadProducts(
                "", 1
        );

        //observe product admin
        productManageAdminViewModel.productsAdmin.observe(getViewLifecycleOwner(),
                prodAdmin -> {
                    if (prodAdmin == null) return;

                    productAdminAdapter.updateList(prodAdmin.getItems());
                    binding.rvAdminProducts.scrollTo(0,0);
                    productManageAdminViewModel.setCurrentPageAndPageSizeState(
                            prodAdmin.getCurrentPage(),
                            prodAdmin.getTotalPages()
                    );
                });
    }

    private void setSearchViewChange() {
        binding.svSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadProducts(
                        charSequence.toString(),
                        productManageAdminViewModel.getPaginationManager().currentPage.getValue() != null
                                ? productManageAdminViewModel.getPaginationManager().currentPage.getValue().getPage()
                                : 1
                );
            }
        });
    }

    private void observeCurrentPageAndPageSize() {
        //Page
        productManageAdminViewModel.getPaginationManager()
                .currentPage.observe(getViewLifecycleOwner(), page -> {
                    binding.paginationBarView.updatePagination(page.getPage(),
                            productManageAdminViewModel.getPaginationManager().totalPages.getValue() != null
                                    ? productManageAdminViewModel.getPaginationManager().totalPages.getValue()
                                    : 1);
                });

        //Total page
        productManageAdminViewModel.getPaginationManager()
                .totalPages.observe(getViewLifecycleOwner(), totalPage -> {
                    binding.paginationBarView.updatePagination(
                            productManageAdminViewModel.getPaginationManager().currentPage.getValue() != null
                                    ? productManageAdminViewModel.getPaginationManager().currentPage.getValue().getPage()
                                    : 1,
                            totalPage);
                });
    }

    private void loadProducts(
            String keyWord,
            int page
    ) {
        productManageAdminViewModel.loadProducts(
                keyWord,
                "",
                page,
                productManageAdminViewModel.getPageSize());
    }
}