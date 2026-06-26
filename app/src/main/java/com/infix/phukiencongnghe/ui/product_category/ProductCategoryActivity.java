package com.infix.phukiencongnghe.ui.product_category;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.infix.phukiencongnghe.R;

public class ProductCategoryActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_product_category
        );

        getSupportFragmentManager()
                .beginTransaction()

                .replace(
                        R.id.container,
                        new ProductCategoryFragment()
                )

                .commit();
    }
}
