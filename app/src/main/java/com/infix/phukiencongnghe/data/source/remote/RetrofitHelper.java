package com.infix.phukiencongnghe.data.source.remote;


import androidx.lifecycle.ViewModelProvider;

import com.infix.phukiencongnghe.data.source.remote.admin.AdminOrderService;
import com.infix.phukiencongnghe.data.source.remote.admin.product.ProductAdminService;

import com.infix.phukiencongnghe.data.source.remote.auth.AuthService;
import com.infix.phukiencongnghe.data.source.remote.cart.CartService;
import com.infix.phukiencongnghe.data.source.remote.main.CategoryService;
import com.infix.phukiencongnghe.data.source.remote.main.FeatureProductService;
import com.infix.phukiencongnghe.data.source.remote.main.SliderShowService;
import com.infix.phukiencongnghe.data.source.remote.order.OrderSerivce;
import com.infix.phukiencongnghe.data.source.remote.payment.PaymentMethodService;
import com.infix.phukiencongnghe.data.source.remote.ship_fee.ShipFeeByAddressService;
import com.infix.phukiencongnghe.data.source.remote.user_manage.UserAddressManageService;
import com.infix.phukiencongnghe.data.source.remote.user_manage.UserProfileService;
import com.infix.phukiencongnghe.data.source.remote.voucher.VoucherService;
import com.infix.phukiencongnghe.utils.ApiClient;

import retrofit2.Retrofit;

public class RetrofitHelper {
    private RetrofitHelper(){}

    public static VoucherService getVoucherService() {
        return generateRetrofit().create(VoucherService.class);
    }

    public static SliderShowService getSliderShow() {
        return generateRetrofit().create(SliderShowService.class);
    }

    public static UserProfileService getProfileService() {
        return generateRetrofit().create(UserProfileService.class);
    }

    public static CartService getCartService() {
        return generateRetrofit().create(CartService.class);
    }

    public static FeatureProductService getFeatureProductService() {
        return generateRetrofit().create(FeatureProductService.class);
    }

    public static CategoryService getCategoryService() {
        return generateRetrofit().create(CategoryService.class);
    }

    public static AuthService getAuthService() {
        return generateRetrofit().create(AuthService.class);
    }

    public static UserAddressManageService getUserAddressManageService() {
        return generateRetrofit().create(UserAddressManageService.class);
    }

    public static ShipFeeByAddressService getShipFeeByAddressService() {
        return generateRetrofit().create(ShipFeeByAddressService.class);
    }
    public static PaymentMethodService getPaymentMethod(){
        return generateRetrofit().create(PaymentMethodService.class);
    }
    public static OrderSerivce getOrderService() {
        return generateRetrofit().create(OrderSerivce.class);
    }
    public static ProductAdminService getProductAdminService() {
        return generateRetrofit().create(ProductAdminService.class);
    }
    private static Retrofit generateRetrofit() {
        return ApiClient.getRetrofitClient();
    }
    public static AdminOrderService getAdminOrderService() {
        return generateRetrofit().create(AdminOrderService.class);
    }
}
