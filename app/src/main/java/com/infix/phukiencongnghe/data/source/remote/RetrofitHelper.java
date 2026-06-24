package com.infix.phukiencongnghe.data.source.remote;

import com.infix.phukiencongnghe.utils.EvnUtils;
import com.infix.phukiencongnghe.data.source.remote.auth.AuthService;
import com.infix.phukiencongnghe.data.source.remote.main.CategoryService;
import com.infix.phukiencongnghe.data.source.remote.main.FeatureProductService;
import com.infix.phukiencongnghe.data.source.remote.user_manage.UserAddressManageService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private RetrofitHelper(){}

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

    private static Retrofit generateRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(EvnUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
