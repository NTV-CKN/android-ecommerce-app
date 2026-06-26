package com.infix.phukiencongnghe.ui.user_manage;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.databinding.ActivityUserManagerBinding;
import com.infix.phukiencongnghe.ui.auth.AuthActivity;
import com.infix.phukiencongnghe.ui.main.MainActivity;
import com.infix.phukiencongnghe.ui.share_viewmodel.UserEntityViewModel;
import com.infix.phukiencongnghe.ui.user_manage.address.UserAddressManageFragment;
import com.infix.phukiencongnghe.utils.ApiClient;

public class UserManagerActivity extends AppCompatActivity {
    private ActivityUserManagerBinding binding;
    private ActionBarDrawerToggle toggle;
    private UserEntityViewModel userEntityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUserManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupMyToolbar();
        setOnLogoutForApiClient();
    }

    private void setOnLogoutForApiClient() {
        ApiClient.setOnLogoutListener(() -> {
            Intent intent = new Intent(getBaseContext(), AuthActivity.class);
            startActivity(intent);
            finish();
        });
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
        binding.navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_address_user) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fcv_user_manage, new UserAddressManageFragment())
                        .commit();
            } else if (id == R.id.nav_logout) {

            }

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }
}