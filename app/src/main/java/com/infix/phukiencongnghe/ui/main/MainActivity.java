package com.infix.phukiencongnghe.ui.main;

import static com.infix.phukiencongnghe.R.id.main;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycleViewCategory;
    private RecyclerView recyclerViewProduct;
    private MainViewModel mainViewModel;
    private FeatureProductAdapter featureProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        recycleViewCategory = findViewById(R.id.recycleView_category);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycleViewCategory.setLayoutManager(layoutManager);

        recyclerViewProduct = findViewById(R.id.recycleView_product);
        GridLayoutManager productManager = new GridLayoutManager(this, 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerViewProduct.setLayoutManager(productManager);
        recyclerViewProduct.setNestedScrollingEnabled(false);


        featureProductAdapter = new FeatureProductAdapter(new ArrayList<>());
        recyclerViewProduct.setAdapter(featureProductAdapter);

        MainViewModel.Factory factory = new MainViewModel.Factory(
                new CategoryRepositoryImpl(),
                new FeatureProductRepositoryImpl()
        );
        mainViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        mainViewModel.categoryLiveData.observe(this, categoryList -> {
            if(categoryList != null) {
                CategoryAdapter adapter = new CategoryAdapter(categoryList);
                recycleViewCategory.setAdapter(adapter);
            }
        });

        mainViewModel.ftProdLiveData.observe(this, featureProductList -> {
            if(featureProductList != null) {
                featureProductAdapter.setData(featureProductList);
            }
        });

        mainViewModel.notifyMsg.observe(this, errorMsg -> {
            if(errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
                mainViewModel.resetStates();
            }
        });

        mainViewModel.loadParentCategories();
        mainViewModel.loadFeatureProduct(0, 22);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}