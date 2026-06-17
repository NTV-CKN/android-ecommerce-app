package com.infix.phukiencongnghe.ui.user_manage;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.navigation.NavigationView;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.databinding.ActivityUserManagerBinding;
import com.infix.phukiencongnghe.ui.user_manage.address.UserAddressManageFragment;

import java.util.Objects;

public class UserManagerActivity extends AppCompatActivity {
    private ActivityUserManagerBinding binding;
    private ActionBarDrawerToggle toggle;
    //Ai gọi đến UserManagerActivity sẽ truyền userId vào
    private Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUserManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userId = 1;//để tạm
        setupMyToolbar();
    }

    private void setupMyToolbar() {
        setSupportActionBar(binding.toolbarUserManage);

        toggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.toolbarUserManage,
                R.string.open,
                R.string.close
        );
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {

                } else if (id == R.id.nav_address_user) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fcv_user_manage, UserAddressManageFragment.newInstance(userId))
                            .commit();
                } else if (id == R.id.nav_logout) {

                }

                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }
}