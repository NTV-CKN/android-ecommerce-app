package com.infix.phukiencongnghe.ui.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.databinding.ActivityAdminBinding;
import com.infix.phukiencongnghe.ui.admin.order.OrderManageAdminFragment;
import com.infix.phukiencongnghe.ui.admin.product.ProductManageAdminFragment;
import com.infix.phukiencongnghe.ui.admin.voucher.VoucherManageAdminFragment;
import com.infix.phukiencongnghe.ui.auth.AuthActivity;
import com.infix.phukiencongnghe.ui.main.MainActivity;
import com.infix.phukiencongnghe.utils.ApiClient;
import com.infix.phukiencongnghe.utils.AppUtils;

public class AdminActivity extends AppCompatActivity {
    private ActivityAdminBinding binding;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAdminToolbar();
        setOnLogoutForApiClient();

        if (savedInstanceState == null) {
            setDefaultNavigationItem(1);
        }
    }

    private void setOnLogoutForApiClient() {
        ApiClient.setOnLogoutListener(() -> {
            AppUtils.startNewTaskWithClearStack(getBaseContext(), AuthActivity.class);
        });
    }

    private void setupAdminToolbar() {
        setSupportActionBar(binding.toolbarAdminManage);
        toggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayoutAdmin,
                binding.toolbarAdminManage,
                R.string.open,
                R.string.close
        );
        binding.drawerLayoutAdmin.addDrawerListener(toggle);
        toggle.syncState();
        binding.navigationViewAdmin.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_product_admin) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcv_admin_manage, new ProductManageAdminFragment())
                        .commit();
            } else if (id == R.id.nav_voucher_admin) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcv_admin_manage, new VoucherManageAdminFragment())
                        .commit();
            }else if(id == R.id.nav_order_admin) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fcv_admin_manage, new OrderManageAdminFragment())
                        .commit();
            }

            binding.drawerLayoutAdmin.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void setDefaultNavigationItem(int index) {
        Menu menu = binding.navigationViewAdmin.getMenu();
        if (menu.size() > index) {
            MenuItem firstItem = menu.getItem(index);
            firstItem.setChecked(true);
            binding.navigationViewAdmin.getMenu().performIdentifierAction(firstItem.getItemId(), 0);
        }
    }
}