package com.infix.phukiencongnghe.ui.product_category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.dto.response.CategoryDTO;
import com.infix.phukiencongnghe.data.model.Page;
import com.infix.phukiencongnghe.data.repository.main.category.CategoryRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.main.product.FeatureProductRepositoryImpl;
import com.infix.phukiencongnghe.ui.adapter.categories.CategoryAdapter;
import com.infix.phukiencongnghe.ui.adapter.feature_product.FeatureProductAdapter;

import java.util.ArrayList;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.appcompat.widget.AppCompatEditText;
import com.infix.phukiencongnghe.utils.paging.custom_view.PaginationBarView;
import android.os.Handler;
import android.os.Looper;

public class ProductCategoryFragment extends Fragment {
    private RecyclerView rvCategory;
    private NestedScrollView nestedScrollView;
    private RecyclerView rvProduct;
    private AppCompatEditText edtSearch;
    private ProductCategoryViewModel viewModel;
    private FeatureProductAdapter productAdapter;
    private PaginationBarView paginationBar;
    private Button btnAllPrice;
    private Button btnPrice1;
    private Button btnPrice2;
    private Button btnPrice3;
    private Button btnSort;
    private Runnable searchRunnable;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        return inflater.inflate(
                R.layout.fragment_product_category,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupRecyclerView();
        setupViewModel();
        observeData();
        setupPriceFilter();
        setupSort();
        setupSearch();
        setupPagination();

        btnAllPrice.setSelected(true);
        viewModel.loadCategories();

        Integer categoryId = null;
        if(getArguments() != null &&
                getArguments().containsKey("categoryId")){
            categoryId =
                    getArguments().getInt("categoryId");
        }
        viewModel.changeCategory(categoryId);
    }

    private void initViews(View view) {
        rvCategory = view.findViewById(R.id.rvCategory);
        rvProduct = view.findViewById(R.id.rvProduct);
        edtSearch = view.findViewById(R.id.edtSearch);
        btnAllPrice = view.findViewById(R.id.btnAllPrice);
        btnPrice1 = view.findViewById(R.id.btnPrice1);
        btnPrice2 = view.findViewById(R.id.btnPrice2);
        btnPrice3 = view.findViewById(R.id.btnPrice3);
        nestedScrollView = view.findViewById(R.id.main_layout);
        paginationBar = view.findViewById(R.id.paginationBar);
        btnSort = view.findViewById(R.id.btnSort);
    }

    private void setupRecyclerView() {
        rvCategory.setLayoutManager(
                new LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                )
        );
        rvProduct.setLayoutManager(
                new GridLayoutManager(
                        requireContext(),
                        2
                )
        );
        productAdapter =
                new FeatureProductAdapter(
                        new ArrayList<>()
                );

        productAdapter.setOnItemClickListener(product -> {

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()

                    .replace(
                            R.id.fcv_main_content,
                            com.infix.phukiencongnghe
                                    .ui.main.product_detail
                                    .ProductDetailsFragment
                                    .newInstance(
                                            product.getId()
                                    )
                    )

                    .addToBackStack(null)

                    .commit();
        });

        rvProduct.setAdapter(productAdapter);
    }

    private void setupViewModel() {
        ProductCategoryViewModel.Factory factory =
                new ProductCategoryViewModel.Factory(
                        new CategoryRepositoryImpl(),
                        new FeatureProductRepositoryImpl()
                );
        viewModel =
                new ViewModelProvider(
                        this,
                        factory
                ).get(ProductCategoryViewModel.class);
    }

    private void observeData(){

        // observe category
        viewModel.categories.observe(
                getViewLifecycleOwner(),
                categoryList -> {
                    if(categoryList != null){
                        CategoryDTO all = new CategoryDTO(
                                null,
                                "Tất cả"
                        );
                        if(categoryList.isEmpty()
                                || !"Tất cả".equals(categoryList.get(0).getName())){
                            categoryList.add(
                                    0,
                                    all
                            );
                        }
                        CategoryAdapter adapter =
                                new CategoryAdapter(
                                        categoryList,
                                        category -> {
                                            resetPriceButtons();
                                            btnAllPrice.setSelected(true);
                                            edtSearch.setText("");
                                            viewModel.changeCategory(category.getId());
                                            rvProduct.scrollToPosition(0);
                                        }
                                );
                        rvCategory.setAdapter(
                                adapter
                        );
                    }
                }
        );
        // observe product
        viewModel.products.observe(
                getViewLifecycleOwner(),
                productList -> {
                    productAdapter.setData(
                            productList != null
                                    ? productList
                                    : new ArrayList<>()
                    );
                }
        );
        // observe notify
        viewModel.notify.observe(
                getViewLifecycleOwner(),
                msg -> {
                    if(msg != null){
                        Toast.makeText(
                                requireContext(), msg, Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
        viewModel
                .getPaginationManager()
                .currentPage
                .observe(
                        getViewLifecycleOwner(),
                        currentPage -> {

                            Integer totalPages =
                                    viewModel
                                            .getPaginationManager()
                                            .totalPages
                                            .getValue();

                            if(currentPage != null && totalPages != null){

                                paginationBar.updatePagination(
                                        currentPage.getPage(),
                                        totalPages
                                );
                            }
                        }
                );
        viewModel
                .getPaginationManager()
                .totalPages
                .observe(
                        getViewLifecycleOwner(),
                        totalPages -> {

                            Page currentPage =
                                    viewModel
                                            .getPaginationManager()
                                            .currentPage
                                            .getValue();

                            if(currentPage != null){

                                paginationBar.updatePagination(
                                        currentPage.getPage(),
                                        totalPages
                                );
                            }
                        }
                );
    }

    private void setupPagination(){
        paginationBar.setOnPageChangeListener(
                page -> {
                    viewModel
                            .getPaginationManager()
                            .setCurrentPage(page);

                    nestedScrollView.smoothScrollTo(0, 0);
                }
        );
    }
    private void resetPriceButtons(){
        btnAllPrice.setSelected(false);
        btnPrice1.setSelected(false);
        btnPrice2.setSelected(false);
        btnPrice3.setSelected(false);
    }

    private void setupPriceFilter(){
        btnAllPrice.setOnClickListener(v -> {
            resetPriceButtons();
            btnAllPrice.setSelected(true);
            viewModel.clearPriceFilter();
        });
        btnPrice1.setOnClickListener(v -> {
            resetPriceButtons();
            btnPrice1.setSelected(true);
            viewModel.setPriceFilter(
                    null,
                    100000.0
            );
        });

        btnPrice2.setOnClickListener(v -> {
            resetPriceButtons();
            btnPrice2.setSelected(true);
            viewModel.setPriceFilter(
                    100000.0,
                    500000.0
            );
        });

        btnPrice3.setOnClickListener(v -> {
            resetPriceButtons();
            btnPrice3.setSelected(true);
            viewModel.setPriceFilter(
                    500000.0,
                    null
            );
        });
    }

    private final Handler handler =
            new Handler(Looper.getMainLooper());

    private void setupSearch(){
        if(edtSearch == null){
            return;
        }
        edtSearch.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {}
                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        if(searchRunnable != null){

                            handler.removeCallbacks(
                                    searchRunnable
                            );
                        }

                        searchRunnable = () -> {

                            viewModel.setKeyword(
                                    s.toString().trim()
                            );
                            nestedScrollView.smoothScrollTo(0,0);
                        };

                        handler.postDelayed(
                                searchRunnable,
                                350
                        );
                    }
                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {}
                }
        );
    }

    private void setupSort(){

        btnSort.setOnClickListener(v -> {

            android.widget.PopupMenu popupMenu =
                    new android.widget.PopupMenu(
                            requireContext(),
                            btnSort
                    );

            popupMenu.getMenu().add("Giá thấp → cao");
            popupMenu.getMenu().add("Giá cao → thấp");
            popupMenu.getMenu().add("Sao thấp → cao");
            popupMenu.getMenu().add("Sao cao → thấp");

            popupMenu.setOnMenuItemClickListener(item -> {

                String text =
                        item.getTitle().toString();

                btnSort.setText(text);

                switch (text){

                    case "Giá thấp → cao":

                        viewModel.setSort(
                                "price",
                                "asc"
                        );
                        break;

                    case "Giá cao → thấp":

                        viewModel.setSort(
                                "price",
                                "desc"
                        );
                        break;

                    case "Sao thấp → cao":

                        viewModel.setSort(
                                "star",
                                "asc"
                        );
                        break;

                    case "Sao cao → thấp":

                        viewModel.setSort(
                                "star",
                                "desc"
                        );
                        break;
                }

                nestedScrollView.smoothScrollTo(0,0);

                return true;
            });

            popupMenu.show();
        });
    }

}