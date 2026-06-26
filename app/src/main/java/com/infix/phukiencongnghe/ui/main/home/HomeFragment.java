package com.infix.phukiencongnghe.ui.main.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.repository.main.category.CategoryRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.main.product.FeatureProductRepositoryImpl;
import com.infix.phukiencongnghe.ui.adapter.categories.CategoryAdapter;
import com.infix.phukiencongnghe.ui.adapter.feature_product.FeatureProductAdapter;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recycleViewCategory;
    private RecyclerView recyclerViewProduct;
    private RecyclerView recyclerViewMouse;
    private RecyclerView recyclerViewKeyboard;
    private HomeViewModel homeViewModel;
    private FeatureProductAdapter featureProductAdapter;
    private FeatureProductAdapter mouseAdapter;
    private FeatureProductAdapter keyboardAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Khởi tạo và trả về View của Fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Ánh xạ các View từ layout fragment_home
        recycleViewCategory = view.findViewById(R.id.recycleView_category);
        recyclerViewProduct = view.findViewById(R.id.recycleView_product);
        recyclerViewMouse = view.findViewById(R.id.recycleView_mouse);
        recyclerViewKeyboard = view.findViewById(R.id.recycleView_keyboard);

        // 2. Setup RecyclerView Category (Nằm ngang)
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recycleViewCategory.setLayoutManager(layoutManager);

        // 3. Setup RecyclerView Product (Grid 2 cột)
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerViewProduct.setNestedScrollingEnabled(false);

        recyclerViewMouse.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerViewMouse.setNestedScrollingEnabled(false);

        recyclerViewKeyboard.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        recyclerViewKeyboard.setNestedScrollingEnabled(false);

        // 4. Khởi tạo Adapter cho Product
        featureProductAdapter = new FeatureProductAdapter(new ArrayList<>());
        mouseAdapter = new FeatureProductAdapter(new ArrayList<>());
        keyboardAdapter = new FeatureProductAdapter(new ArrayList<>());

        recyclerViewProduct.setAdapter(featureProductAdapter);
        recyclerViewMouse.setAdapter(mouseAdapter);
        recyclerViewKeyboard.setAdapter(keyboardAdapter);

        // 5. Cấu hình ViewModel (Gắn với 'this' tức là Vòng đời của chính Fragment này)
        HomeViewModel.Factory factory = new HomeViewModel.Factory(
                new CategoryRepositoryImpl(),
                new FeatureProductRepositoryImpl()
        );
        homeViewModel = new ViewModelProvider(this, factory).get(HomeViewModel.class);

        // 6. Quan sát (Observe) các nguồn dữ liệu LiveData
        observeViewModel();

        // 7. Kích hoạt gọi API lấy dữ liệu lần đầu
        homeViewModel.loadParentCategories();
        homeViewModel.loadFeatureProduct(8);
        homeViewModel.loadMouseProduct(8);
        homeViewModel.loadKeyboardProduct(8);
    }

    private void observeViewModel() {
        // Thao tác với UI trong Fragment sử dụng getViewLifecycleOwner() để tránh leak memory
        homeViewModel.categoryLiveData.observe(getViewLifecycleOwner(), categoryList -> {
            if (categoryList != null) {
                CategoryAdapter adapter =
                        new CategoryAdapter(
                                categoryList,
                                category -> {
                                    // tạm thời chưa làm gì
                                    // sau này bấm category chuyển màn product category

                                }
                        );
                recycleViewCategory.setAdapter(adapter);
            }
        });

        homeViewModel.ftProdLiveData.observe(getViewLifecycleOwner(), featureProductList -> {
            if (featureProductList != null) {
                featureProductAdapter.setData(featureProductList);
            }
        });

        homeViewModel.mouseLiveData.observe(getViewLifecycleOwner(), mouseList -> {
            if (mouseList != null) mouseAdapter.setData(mouseList);
        });

        homeViewModel.keyboardLiveData.observe(getViewLifecycleOwner(), keyboardList -> {
            if (keyboardList != null) keyboardAdapter.setData(keyboardList);
        });

        homeViewModel.notifyMsg.observe(getViewLifecycleOwner(), errorMsg -> {
            if (errorMsg != null) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show();
                homeViewModel.resetStates();
            }
        });
    }
}