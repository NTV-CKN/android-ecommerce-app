package com.infix.phukiencongnghe.ui.user_manage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import com.google.android.material.snackbar.Snackbar;
import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.databinding.ActivityUserManagerBinding;
import com.infix.phukiencongnghe.ui.auth.AuthActivity;
import com.infix.phukiencongnghe.ui.main.MainActivity;
import com.infix.phukiencongnghe.ui.share_viewmodel.UserEntityViewModel;
import com.infix.phukiencongnghe.ui.user_manage.address.UserAddressManageFragment;
import com.infix.phukiencongnghe.ui.user_manage.profile.UserProfileFragment;
import com.infix.phukiencongnghe.utils.ApiClient;
import com.infix.phukiencongnghe.utils.AppUtils;
import com.infix.phukiencongnghe.utils.SharePrefUtils;
import com.infix.phukiencongnghe.utils.SnackbarUtils;

public class UserManagerActivity extends AppCompatActivity {
    private ActivityUserManagerBinding binding;
    private ActionBarDrawerToggle toggle;
    private UserEntityViewModel userEntityViewModel;
    public static final String EXTRA_SELECTED_ADDRESS = "SELECTED_ADDRESS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityUserManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupMyToolbar();
        setOnLogoutForApiClient();
        handleIntent();
    }

    private void handleIntent() {
        boolean isFromPayment = getIntent().getBooleanExtra("IS_FROM_PAYMENT",false);
        if(isFromPayment){
            getSupportFragmentManager().beginTransaction().replace(R.id.fcv_user_manage,UserAddressManageFragment.newInstance(true)).commit();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.fcv_user_manage,UserAddressManageFragment.newInstance(false)).commit();
        if (savedInstanceState == null) {
            setDefaultNavigationItem(1);
        }
    }

    private void setOnLogoutForApiClient() {
        ApiClient.setOnLogoutListener(() -> AppUtils.startNewTaskWithClearStack(getBaseContext(), AuthActivity.class));
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
            }else if(id == R.id.nav_profile_user){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fcv_user_manage, new UserProfileFragment())
                        .commit();
            }else if (id == R.id.nav_address_user) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fcv_user_manage, new UserAddressManageFragment())
                        .commit();
            } else if (id == R.id.nav_logout) {
                SnackbarUtils.showSnackbarWithAction(
                        binding.getRoot(),
                        "Bạn có chắc muốn đăng xuất không?",
                        Snackbar.LENGTH_LONG,
                        () -> {
                            SharePrefUtils.saveAccessTokenAndRefreshTokenToPrefFile(
                                    AuthActivity.USER_AUTH_FILE,
                                    AuthActivity.KEY_ACCESS_TOKEN,
                                    AuthActivity.KEY_REFRESH_TOKEN,
                                    null,
                                    null,
                                    getBaseContext()
                            );
                            AppUtils.startNewTaskWithClearStack(getBaseContext(), AuthActivity.class);
                        }
                );
            }

            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void setDefaultNavigationItem(int index) {
        Menu menu = binding.navigationView.getMenu();
        if (menu.size() > index) {
            MenuItem firstItem = menu.getItem(index);
            firstItem.setChecked(true);

            binding.navigationView.getMenu().performIdentifierAction(firstItem.getItemId(), 0);
        }
    }
}