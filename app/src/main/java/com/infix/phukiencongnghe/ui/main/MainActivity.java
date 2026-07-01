package com.infix.phukiencongnghe.ui.main;

import static com.infix.phukiencongnghe.R.id.main;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.infix.phukiencongnghe.R;
import com.infix.phukiencongnghe.data.source.local.entity.UserEntity;
import com.infix.phukiencongnghe.databinding.ActivityMainBinding;
import com.infix.phukiencongnghe.ui.infoshop.InfoShopFragment;
import com.infix.phukiencongnghe.ui.main.home.HomeFragment;
import com.infix.phukiencongnghe.ui.product_category.ProductCategoryFragment;
import com.infix.phukiencongnghe.ui.setting.SettingFragment;
import com.infix.phukiencongnghe.ui.voucher.VoucherFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    //AuthActivity sẽ gửi đối tượng User sang MainActivity khi login thành công
    public static final String KEY_USER_HEADER = "com.infix.phukiencongnghe.ui.main.MainActivity.KEY_USER_HEADER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            findViewById(R.id.fcv_header_home).setPadding(0, systemBars.top, 0, 0);
            findViewById(R.id.bottom_navigation).setPadding(0, 0, 0, systemBars.bottom);
            return insets;
        });

        setEventBottomNav();
    }

    private void setEventBottomNav() {
        binding.bottomNavigation.setOnItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.nav_categories)
                handleNavCategoryProduct();
            if(id == R.id.nav_voucher) handleNavVoucher();
            else if (id == R.id.nav_home)
                handleNavHome();
            else if (id == R.id.nav_info) {
                handleNavInfo();
            }else if(id == R.id.nav_setting)
                handleNavSetting();
            return true;
        });
    }

    private void handleNavSetting() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fcv_main_content, new SettingFragment())
                .addToBackStack(null)
                .commit();
    }

    private void handleNavVoucher() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fcv_main_content, new VoucherFragment())
                .addToBackStack(null)
                .commit();
    }

    private void handleNavHome() {
      getSupportFragmentManager().beginTransaction()
              .replace(R.id.fcv_main_content, new HomeFragment())
              .addToBackStack(null)
              .commit();
    }

    private void handleNavCategoryProduct() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fcv_main_content, new ProductCategoryFragment())
                .addToBackStack(null)
                .commit();
    }

    private void handleNavInfo() {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fcv_main_content,
                        new InfoShopFragment()
                )
                .commit();
    }
}