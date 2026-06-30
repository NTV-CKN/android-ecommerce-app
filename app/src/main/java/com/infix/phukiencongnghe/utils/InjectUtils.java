package com.infix.phukiencongnghe.utils;

import android.content.Context;

import com.infix.phukiencongnghe.data.repository.admin.product.IProductAdminRepository;
import com.infix.phukiencongnghe.data.repository.admin.product.ProductAdminRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.auth.AuthRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.auth.IAuthRepository;
import com.infix.phukiencongnghe.data.repository.cart.CartRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.cart.ICartRepository;
import com.infix.phukiencongnghe.data.repository.common.category.CategoryRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.common.category.ICategoryRepository;
import com.infix.phukiencongnghe.data.repository.ship_fee.IShipFeeByAddressRepository;
import com.infix.phukiencongnghe.data.repository.ship_fee.ShipFeeByAddressRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.user_manage.address.IUserAddressManageRepository;
import com.infix.phukiencongnghe.data.repository.user_manage.address.UserAddressManageRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.user_manage.profile.IUserProfileRepository;
import com.infix.phukiencongnghe.data.repository.user_manage.profile.UserProfileRepositoryImpl;
import com.infix.phukiencongnghe.data.repository.voucher.IVoucherRepository;
import com.infix.phukiencongnghe.data.repository.voucher.VoucherRepositoryImpl;
import com.infix.phukiencongnghe.data.source.local.AppDatabase;
import com.infix.phukiencongnghe.data.source.local.source.user.UserLocalDataSourceImpl;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.data.source.remote.admin.product.ProductAdminRemoteDataSourceImpl;
import com.infix.phukiencongnghe.data.source.remote.auth.AuthRemoteDataSourceImpl;

public class InjectUtils {

    public static IVoucherRepository createVoucherRepository() {
        return new VoucherRepositoryImpl(
                RetrofitHelper.getVoucherService()
        );
    }

    public static IAuthRepository createAuthRepository(Context context) {
        return new AuthRepositoryImpl(
                RetrofitHelper.getAuthService(),
                new UserLocalDataSourceImpl(
                        AppDatabase.getInstance(context).userDAO()
                ),
                new AuthRemoteDataSourceImpl()
        );
    }

    public static IShipFeeByAddressRepository createShipFeeByAddressRepository() {
        return new ShipFeeByAddressRepositoryImpl(
                RetrofitHelper.getShipFeeByAddressService()
        );
    }

    public static ICartRepository createCartRepository() {
        return new CartRepositoryImpl(
                RetrofitHelper.getCartService()
        );
    }

    public static IUserAddressManageRepository createUserAddressManageRepository(Context context) {
        return new UserAddressManageRepositoryImpl(
                RetrofitHelper.getUserAddressManageService()
        );
    }

    public static IUserProfileRepository createUserProfileRepository() {
        return new UserProfileRepositoryImpl(
                RetrofitHelper.getProfileService()
        );
    }

    public static IProductAdminRepository createProductAdminRepository() {
        return new ProductAdminRepositoryImpl(
                new ProductAdminRemoteDataSourceImpl(
                        RetrofitHelper.getProductAdminService()
                )
        );
    }

    public static ICategoryRepository createCategoryRepository() {
        return new CategoryRepositoryImpl();
    }
}