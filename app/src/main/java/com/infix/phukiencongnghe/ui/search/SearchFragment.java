package com.infix.phukiencongnghe.ui.search;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.infix.phukiencongnghe.data.repository.main.product.FeatureProductRepositoryImpl;
import com.infix.phukiencongnghe.data.source.local.source.search.SearchLocalRepositoryImpl;
import com.infix.phukiencongnghe.databinding.FragmentSearchBinding;
import com.infix.phukiencongnghe.ui.adapter.search.RecentSearchProductAdapter;
import com.infix.phukiencongnghe.ui.adapter.search.SearchKeywordAdapter;
import com.infix.phukiencongnghe.ui.adapter.search.SearchResultAdapter;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private SearchViewModel viewModel;

    private Handler handler =
            new Handler(Looper.getMainLooper());

    private Runnable searchRunnable;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        binding = FragmentSearchBinding.inflate(
                inflater,
                container,
                false
        );

        setupViewModel();

        binding.btnClearKeyword
                .setOnClickListener(v ->
                        viewModel.clearKeywordHistory()
                );

        binding.btnClearRecent
                .setOnClickListener(v ->
                        viewModel.clearRecentProducts()
                );

        setupRecyclerView();
        observeData();
        setupSearchListener();

        showHistoryLayout();

        return binding.getRoot();
    }

    private void setupViewModel() {

        SearchViewModel.Factory factory =
                new SearchViewModel.Factory(
                        new FeatureProductRepositoryImpl(),
                        new SearchLocalRepositoryImpl(
                                requireContext()
                        )
                );

        viewModel =
                new ViewModelProvider(
                        this,
                        factory
                ).get(SearchViewModel.class);
    }

    private void setupRecyclerView() {

        binding.rvKeywordHistory
                .setLayoutManager(
                        new LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                        )
                );

        binding.rvRecentProduct
                .setLayoutManager(
                        new LinearLayoutManager(
                                requireContext()
                        )
                );

        binding.rvSearchResult
                .setLayoutManager(
                        new LinearLayoutManager(
                                requireContext()
                        )
                );
    }

    private void observeData() {

        // SEARCH RESULT

        viewModel.products.observe(
                getViewLifecycleOwner(),

                products -> {

                    binding.rvSearchResult
                            .setAdapter(
                                    new SearchResultAdapter(
                                            products,

                                            product -> {

                                                viewModel
                                                        .saveRecentProduct(
                                                                product
                                                        );

                                                // mở Product Detail sau
                                            }
                                    )
                            );
                }
        );


        // KEYWORD HISTORY

        viewModel.searchKeywords.observe(
                getViewLifecycleOwner(),

                keywords -> {

                    binding.rvKeywordHistory
                            .setAdapter(
                                    new SearchKeywordAdapter(

                                            keywords,

                                            keyword -> {

                                                binding.edtSearch
                                                        .setText(
                                                                keyword
                                                        );

                                                binding.edtSearch
                                                        .setSelection(
                                                                keyword.length()
                                                        );

                                                // KHÔNG gọi searchProduct ở đây
                                                // vì setText sẽ trigger TextWatcher
                                            }
                                    )
                            );
                }
        );


        // RECENT PRODUCT

        viewModel.recentProducts.observe(
                getViewLifecycleOwner(),

                products -> {

                    binding.rvRecentProduct
                            .setAdapter(
                                    new RecentSearchProductAdapter(

                                            products,

                                            item -> {

                                                // mở Product Detail sau
                                            }
                                    )
                            );
                }
        );
    }

    private void setupSearchListener() {

        binding.edtSearch.addTextChangedListener(
                new TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after
                    ) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count
                    ) {

                        String keyword =
                                s.toString().trim();

                        if (searchRunnable != null) {

                            handler.removeCallbacks(
                                    searchRunnable
                            );
                        }

                        if (keyword.isEmpty()) {

                            showHistoryLayout();

                        } else {

                            showSearchResult();

                            searchRunnable = () ->
                                    viewModel.searchProduct(
                                            keyword
                                    );

                            handler.postDelayed(
                                    searchRunnable,
                                    500
                            );
                        }
                    }

                    @Override
                    public void afterTextChanged(
                            Editable s
                    ) {
                    }
                });
    }

    private void showHistoryLayout() {

        binding.txtHistoryTitle
                .setVisibility(View.VISIBLE);

        binding.btnClearKeyword
                .setVisibility(View.VISIBLE);

        binding.rvKeywordHistory
                .setVisibility(View.VISIBLE);

        binding.txtRecentTitle
                .setVisibility(View.VISIBLE);

        binding.btnClearRecent
                .setVisibility(View.VISIBLE);

        binding.rvRecentProduct
                .setVisibility(View.VISIBLE);

        binding.rvSearchResult
                .setVisibility(View.GONE);
    }

    private void showSearchResult() {

        binding.txtHistoryTitle
                .setVisibility(View.GONE);

        binding.btnClearKeyword
                .setVisibility(View.GONE);

        binding.rvKeywordHistory
                .setVisibility(View.GONE);

        binding.txtRecentTitle
                .setVisibility(View.GONE);

        binding.btnClearRecent
                .setVisibility(View.GONE);

        binding.rvRecentProduct
                .setVisibility(View.GONE);

        binding.rvSearchResult
                .setVisibility(View.VISIBLE);
    }
}