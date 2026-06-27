package com.infix.phukiencongnghe.ui.searchadvance;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.infix.phukiencongnghe.R;

public class SearchAdvanceActivity
        extends AppCompatActivity {

    @Override
    protected void onCreate(
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_search_advance
        );

        String keyword =
                getIntent().getStringExtra(
                        "keyword"
                );

        if (savedInstanceState == null) {

            SearchAdvanceFragment fragment =
                    SearchAdvanceFragment
                            .newInstance(keyword);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.search_advance_container,
                            fragment
                    )
                    .commit();
        }
    }
}