package com.infix.phukiencongnghe.ui.search;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.infix.phukiencongnghe.R;

public class SearchProductActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_search_product
        );

        if(savedInstanceState == null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.search_fragment_container,
                            new SearchFragment()
                    )
                    .commit();
        }
    }
}