package com.infix.phukiencongnghe.ui.searchadvance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import com.infix.phukiencongnghe.data.repository.common.category.CategoryRepositoryImpl;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.repository.common.product.FeatureProductRepositoryImpl;
import com.infix.phukiencongnghe.data.source.local.source.search.SearchLocalRepositoryImpl;
import com.infix.phukiencongnghe.ui.adapter.search.SearchAdvanceAdapter;
import com.infix.phukiencongnghe.ui.main.product_detail.ProductDetailsFragment;
import com.infix.phukiencongnghe.utils.paging.custom_view.PaginationBarView;

public class SearchAdvanceFragment extends Fragment {

    private static final String KEYWORD = "keyword";

    private SearchAdvanceViewModel viewModel;

    private RecyclerView rvProduct;

    private PaginationBarView paginationBar;

    private EditText edtSearch;

    private Button btnNewest;
    private Spinner spCategory;
    private Integer selectedCategoryId = null;
    private EditText edtMinPrice;
    private EditText edtMaxPrice;
    private Button btnApplyFilter;
    private Button btnToggleFilter;
    private Button btnClearFilter;
    private LinearLayout filterLayout;
    private AppCompatButton btnPrice;
    private AppCompatButton btnRating;
    private boolean isFilterExpanded = false;

    public static SearchAdvanceFragment
    newInstance(String keyword) {

        SearchAdvanceFragment fragment =
                new SearchAdvanceFragment();

        Bundle args = new Bundle();

        args.putString(
                KEYWORD,
                keyword
        );

        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        View view =
                inflater.inflate(
                        R.layout.fragment_search_advance,
                        container,
                        false
                );

        initView(view);

        setupViewModel();

        setupRecyclerView();

        setupBackToSearch();

        observeData();

        observeCategory();

        viewModel.loadCategories();

        setupPagination();

        setupSortButton();

        setupToggleFilter();

        setupApplyFilter();

        setupClearFilter();

        loadInitialKeyword();

        return view;
    }


    // ===== INIT VIEW =====

    private void initView(
            View view
    ) {

        rvProduct =
                view.findViewById(
                        R.id.rv_product
                );

        paginationBar =
                view.findViewById(
                        R.id.paginationBar
                );

        edtSearch =
                view.findViewById(
                        R.id.edt_search
                );

        btnNewest =
                view.findViewById(
                        R.id.btn_newest
                );

        btnPrice =
                view.findViewById(
                        R.id.btn_price
                );

        btnRating =
                view.findViewById(
                        R.id.btn_rating
                );

        spCategory =
                view.findViewById(
                        R.id.sp_category
                );

        edtMinPrice =
                view.findViewById(
                        R.id.edt_min_price
                );

        edtMaxPrice =
                view.findViewById(
                        R.id.edt_max_price
                );

        btnApplyFilter =
                view.findViewById(
                        R.id.btn_apply_filter
                );

        btnToggleFilter =
                view.findViewById(
                        R.id.btn_toggle_filter
                );

        filterLayout =
                view.findViewById(
                        R.id.filter_layout
                );
        btnClearFilter =
                view.findViewById(
                        R.id.btn_clear_filter
                );
    }


    // ===== VIEW MODEL =====

    private void setupViewModel() {

        SearchAdvanceViewModel.Factory factory =
                new SearchAdvanceViewModel.Factory(
                        new FeatureProductRepositoryImpl(),
                        new CategoryRepositoryImpl(),
                        new SearchLocalRepositoryImpl(
                                requireContext()
                        )
                );

        viewModel =
                new ViewModelProvider(
                        this,
                        factory
                ).get(
                        SearchAdvanceViewModel.class
                );
    }


    // ===== RECYCLER =====
    private void setupRecyclerView() {

        rvProduct.setLayoutManager(
                new GridLayoutManager(
                        requireContext(),
                        2
                )
        );

        if(rvProduct.getItemDecorationCount() == 0){

            rvProduct.addItemDecoration(
                    new GridSpacingItemDecoration(
                            2,
                            12
                    )
            );
        }
    }


    // ===== OBSERVE =====

    private void observeData() {

        viewModel.products.observe(
                getViewLifecycleOwner(),

                products -> {

                    rvProduct.setAdapter(
                            new SearchAdvanceAdapter(
                                    products,
                                    product -> {

                                        viewModel.saveRecentProduct(product);

                                        ProductDetailsFragment fragment =
                                                ProductDetailsFragment
                                                        .newInstance(
                                                                product.getId()
                                                        );

                                        if (isAdded()) {

                                            requireActivity()
                                                    .getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(
                                                            R.id.fcv_main_content,
                                                            fragment
                                                    )
                                                    .addToBackStack(null)
                                                    .commit();
                                        }
                                    }
                            )
                    );
                }
        );


        viewModel.getPaginationManager()
                .totalPages
                .observe(
                        getViewLifecycleOwner(),

                        total -> {

                            if (total == null)
                                return;

                            Integer current = 1;

                            if (viewModel.getPaginationManager()
                                    .currentPage.getValue() != null) {

                                current =
                                        viewModel
                                                .getPaginationManager()
                                                .currentPage
                                                .getValue()
                                                .getPage();
                            }

                            if (total <= 1) {

                                paginationBar.setVisibility(
                                        View.GONE
                                );

                            } else {

                                paginationBar.setVisibility(
                                        View.VISIBLE
                                );

                                paginationBar.updatePagination(
                                        current,
                                        total
                                );
                            }
                        }
                );
    }


    // ===== PAGINATION =====

    private void setupPagination() {

        paginationBar
                .setOnPageChangeListener(

                        page -> {

                            viewModel
                                    .getPaginationManager()
                                    .setCurrentPage(
                                            page
                                    );
                        }
                );
    }


    // ===== INITIAL SEARCH =====

    private void loadInitialKeyword() {

        if(getArguments() == null)
            return;

        String keyword =
                getArguments()
                        .getString(
                                KEYWORD
                        );

        if(keyword != null){

            edtSearch.setText(
                    keyword
            );

            viewModel.setKeyword(
                    keyword
            );
        }
    }


    // ===== SORT =====

    private void setupSortButton() {

        btnNewest.setOnClickListener(v -> {

            resetSortButtons();

            btnNewest.setSelected(true);

            viewModel.setSort(
                    "date",
                    "desc"
            );
        });


        btnPrice.setOnClickListener(v -> {

            PopupMenu popup =
                    new PopupMenu(
                            requireContext(),
                            btnPrice
                    );

            popup.getMenu().add(
                    "Giá thấp → cao"
            );

            popup.getMenu().add(
                    "Giá cao → thấp"
            );

            popup.setOnMenuItemClickListener(
                    item -> {

                        resetSortButtons();

                        btnPrice.setSelected(true);

                        if(item.getTitle()
                                .equals("Giá thấp → cao")) {

                            viewModel.setSort(
                                    "price",
                                    "asc"
                            );

                        } else {

                            viewModel.setSort(
                                    "price",
                                    "desc"
                            );
                        }

                        return true;
                    }
            );

            popup.show();
        });


        btnRating.setOnClickListener(v -> {

            PopupMenu popup =
                    new PopupMenu(
                            requireContext(),
                            btnRating
                    );

            popup.getMenu().add(
                    "Đánh giá thấp → cao"
            );

            popup.getMenu().add(
                    "Đánh giá cao → thấp"
            );

            popup.setOnMenuItemClickListener(
                    item -> {

                        resetSortButtons();

                        btnRating.setSelected(true);

                        if(item.getTitle()
                                .equals("Đánh giá thấp → cao")) {

                            viewModel.setSort(
                                    "star",
                                    "asc"
                            );

                        } else {

                            viewModel.setSort(
                                    "star",
                                    "desc"
                            );
                        }

                        return true;
                    }
            );

            popup.show();
        });
    }

    private void resetSortButtons() {

        btnNewest.setSelected(false);
        btnPrice.setSelected(false);
        btnRating.setSelected(false);
    }

    private void observeCategory() {

        viewModel.categories.observe(
                getViewLifecycleOwner(),

                categories -> {

                    if(categories == null){
                        return;
                    }

                    List<String> names =
                            new ArrayList<>();

                    names.add("Tất cả");

                    for(int i = 0; i < categories.size(); i++){

                        names.add(
                                categories
                                        .get(i)
                                        .getName()
                        );
                    }

                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<>(
                                    requireContext(),
                                    android.R.layout.simple_spinner_item,
                                    names
                            );

                    adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                    );

                    spCategory.setAdapter(adapter);


                    spCategory.setOnItemSelectedListener(

                            new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(
                                        AdapterView<?> parent,
                                        View view,
                                        int position,
                                        long id
                                ) {

                                    if(position == 0){

                                        selectedCategoryId = null;

                                    } else {

                                        selectedCategoryId =
                                                categories
                                                        .get(position - 1)
                                                        .getId();
                                    }
                                }

                                @Override
                                public void onNothingSelected(
                                        AdapterView<?> parent
                                ) {

                                }
                            }
                    );
                }
        );
    }

    private void setupApplyFilter() {

        btnApplyFilter.setOnClickListener(

                v -> {

                    Double minPrice = null;

                    String minText =
                            edtMinPrice
                                    .getText()
                                    .toString()
                                    .trim();

                    try {

                        if(!minText.isEmpty()){

                            minPrice =
                                    Double.parseDouble(
                                            minText
                                    );
                        }

                    } catch (Exception e){

                        Toast.makeText(
                                requireContext(),
                                "Giá tối thiểu không hợp lệ",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }


                    Double maxPrice = null;

                    String maxText =
                            edtMaxPrice
                                    .getText()
                                    .toString()
                                    .trim();

                    try {

                        if(!maxText.isEmpty()){

                            maxPrice =
                                    Double.parseDouble(
                                            maxText
                                    );
                        }

                    } catch (Exception e){

                        Toast.makeText(
                                requireContext(),
                                "Giá tối đa không hợp lệ",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }


                    if(minPrice != null
                            && maxPrice != null
                            && minPrice > maxPrice){

                        Toast.makeText(
                                requireContext(),
                                "Giá tối thiểu phải nhỏ hơn giá tối đa",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    viewModel.applyFilter(
                            selectedCategoryId,
                            minPrice,
                            maxPrice
                    );

                    filterLayout.setVisibility(
                            View.GONE
                    );

                    btnToggleFilter.setText(
                            "Lọc ▼"
                    );

                    isFilterExpanded = false;
                }
        );
    }

    private void setupToggleFilter() {

        btnToggleFilter.setOnClickListener(

                v -> {

                    if(!isFilterExpanded){

                        filterLayout.setVisibility(
                                View.VISIBLE
                        );

                        btnToggleFilter.setText(
                                "Ẩn bộ lọc ▲"
                        );

                        isFilterExpanded = true;

                    }else{

                        filterLayout.setVisibility(
                                View.GONE
                        );

                        btnToggleFilter.setText(
                                "Lọc ▼"
                        );

                        isFilterExpanded = false;
                    }
                }
        );
    }

    private void setupClearFilter() {

        btnClearFilter.setOnClickListener(

                v -> {

                    // reset spinner
                    spCategory.setSelection(0);
                    selectedCategoryId = null;

                    // clear price
                    edtMinPrice.setText("");
                    edtMaxPrice.setText("");

                    // reset sort button UI
                    resetSortButtons();

                    // reset sort state ViewModel
                    viewModel.setSort(
                            null,
                            null
                    );

                    // call API reset filter
                    viewModel.applyFilter(
                            null,
                            null,
                            null
                    );

                    // hide filter
                    filterLayout.setVisibility(
                            View.GONE
                    );

                    btnToggleFilter.setText(
                            "Lọc ▼"
                    );

                    isFilterExpanded = false;
                }
        );
    }

    private void setupBackToSearch() {

        edtSearch.setCursorVisible(false);

        edtSearch.setOnClickListener(v -> {

            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        });
    }
}