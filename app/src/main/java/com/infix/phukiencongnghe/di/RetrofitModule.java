package com.infix.phukiencongnghe.di;

import com.infix.phukiencongnghe.data.source.remote.admin.AdminOrderService;
import com.infix.phukiencongnghe.data.source.remote.admin.product.ProductAdminService;
import com.infix.phukiencongnghe.data.source.remote.admin.voucher.AdminVoucherService;
import com.infix.phukiencongnghe.data.source.remote.ai.ChatBotService;
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

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityRetainedComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(ActivityRetainedComponent.class)
public class RetrofitModule {
    @Provides
    public static Retrofit generateRetrofit() {
        return ApiClient.getRetrofitClient();
    }
    @Provides
    public static AdminOrderService getAdminOrderService(Retrofit retrofit) {
        return retrofit.create(AdminOrderService.class);
    }

    @Provides
    public static AdminVoucherService getAdminVoucherService(Retrofit retrofit) {
        return retrofit.create(AdminVoucherService.class);
    }

    @Provides
    public static VoucherService getVoucherService(Retrofit retrofit) {
        return retrofit.create(VoucherService.class);
    }

    @Provides
    public static SliderShowService getSliderShow(Retrofit retrofit) {
        return retrofit.create(SliderShowService.class);
    }

    @Provides
    public static UserProfileService getProfileService(Retrofit retrofit) {
        return retrofit.create(UserProfileService.class);
    }

    @Provides
    public static CartService getCartService(Retrofit retrofit) {
        return retrofit.create(CartService.class);
    }

    @Provides
    public static FeatureProductService getFeatureProductService(Retrofit retrofit) {
        return retrofit.create(FeatureProductService.class);
    }

    @Provides
    public static CategoryService getCategoryService(Retrofit retrofit) {
        return retrofit.create(CategoryService.class);
    }

    @Provides
    public static AuthService getAuthService(Retrofit retrofit) {
        return retrofit.create(AuthService.class);
    }

    @Provides
    public static UserAddressManageService getUserAddressManageService(Retrofit retrofit) {
        return retrofit.create(UserAddressManageService.class);
    }

    @Provides
    public static ShipFeeByAddressService getShipFeeByAddressService(Retrofit retrofit) {
        return retrofit.create(ShipFeeByAddressService.class);
    }

    @Provides
    public static PaymentMethodService getPaymentMethod(Retrofit retrofit) {
        return retrofit.create(PaymentMethodService.class);
    }

    @Provides
    public static OrderSerivce getOrderService(Retrofit retrofit) {
        return retrofit.create(OrderSerivce.class);
    }

    @Provides
    public static ProductAdminService getProductAdminService(Retrofit retrofit) {
        return retrofit.create(ProductAdminService.class);
    }

    @Provides
    public static ChatBotService getChatBotService(Retrofit retrofit) {
        return retrofit.create(ChatBotService.class);
    }
}
