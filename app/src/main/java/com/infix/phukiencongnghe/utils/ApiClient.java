package com.infix.phukiencongnghe.utils;

import static com.infix.phukiencongnghe.EvnUtils.BASE_URL;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.infix.phukiencongnghe.data.dto.request.RefreshTokenDTO;
import com.infix.phukiencongnghe.data.dto.response.JwtFromLoginDTO;
import com.infix.phukiencongnghe.data.source.local.AppDatabase;
import com.infix.phukiencongnghe.data.source.remote.RetrofitHelper;
import com.infix.phukiencongnghe.data.source.remote.auth.AuthService;
import com.infix.phukiencongnghe.ui.auth.AuthActivity;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static boolean alreadyLogout = false;
    private static OnLogoutListener onLogoutListener;
    private static OkHttpClient okHttpClient = null;
    private static Retrofit retrofit = null;
    private static Context context;
    private static final Object tokenLock = new Object();

    private static String accessToken = "";
    private static String refreshToken = "";

    //Khởi tạo OkHttpClient tự lấy refresh token
    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            synchronized (ApiClient.class) {
                if (okHttpClient == null) {
                    okHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(chain -> {
                                Request originalRequest = chain.request();
                                if (accessToken != null && !accessToken.isEmpty()) {

                                    Request authenticatedRequest = originalRequest.newBuilder().header(
                                                            "Authorization",
                                                            "Bearer " + accessToken
                                                    )
                                                    .build();
                                    Log.d("ApiClient", authenticatedRequest.header("Authorization"));

                                    Response response = chain.proceed(authenticatedRequest);
                                    Log.d("ApiClient", "CODE = " + response.code());

                                    return response;
                                }
                                return chain.proceed(originalRequest);
                            })
                            .authenticator((route, response) -> {
                                Log.d("ApiClient", "Need Auth: " + response.code());
                                if (responseCount(response) >= 2) {
                                    handleLogout();
                                    return null;
                                }

                                synchronized (tokenLock) {
                                    String tokenInRequest = response.request().header("Authorization");

                                    if (tokenInRequest != null && !tokenInRequest.equals("Bearer " + accessToken)) {
                                        return response.request().newBuilder()
                                                .header("Authorization", "Bearer " + accessToken)
                                                .build();
                                    }

                                    String newAccessToken = executeRefreshApi();
                                    if (newAccessToken != null) {
                                        accessToken = newAccessToken;
                                        SharePrefUtils.saveStringToPrefFile(
                                                AuthActivity.USER_AUTH_FILE,
                                                AuthActivity.KEY_ACCESS_TOKEN,
                                                accessToken,
                                                context
                                        );
                                        return response.request().newBuilder()
                                                .header("Authorization", "Bearer " + accessToken)
                                                .build();
                                    } else {
                                        handleLogout();
                                        return null;
                                    }
                                }
                            })
                            .build();
                }
            }
        }
        return okHttpClient;
    }

    public static  Retrofit getRetrofitClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    //Khi đăng nhập thành công tiến hành thiết lập lại giá trị của access/refresh Token trong ApiClient
    public static void setAccessTokenAndRefreshToken(String accessToken, String refreshToken) {
        ApiClient.accessToken = accessToken;
        ApiClient.refreshToken = refreshToken;
    }

    public static String getAccessToken() {
        return accessToken;
    }

    public static String getRefreshToken() {
        return refreshToken;
    }

    //Hàm này có nhiệm vụ thiết lập hàm kích hoạt onLogout() mỗi khi chuyển qua 1 activity mới.
    //Các activity sẽ tự đi kiểm soát logic logout tương ứng
    public static void setOnLogoutListener(OnLogoutListener onLogoutListener) {
        ApiClient.onLogoutListener = onLogoutListener;
        alreadyLogout = false;
    }

    //Phương thức này sẽ lưu lại context của application, lấy dữ liệu access/refresh trong file shared
    public static void initAccessRefreshToken(Context context) {
        ApiClient.context = context;
        //Trả về mảng có size = 2
        //index0: access token
        //index1: refresh token
        String[] strs = SharePrefUtils.getAccessRefreshTokenFromPrefFile(
                AuthActivity.USER_AUTH_FILE,
                AuthActivity.KEY_ACCESS_TOKEN,
                AuthActivity.KEY_REFRESH_TOKEN,
                context
        );
        ApiClient.accessToken = strs[0];
        ApiClient.refreshToken = strs[1];
    }

    //Phương thức này sẽ tạo ra 1 request để xin access token từ refresh token,
    //trong trường hợp refresh token bị hết hạn hoặc lỗi thì sẽ bị vòng lặp nếu dùng Ok cũ
    private static String executeRefreshApi() {
        try {
            OkHttpClient cleanHttpClient = new OkHttpClient.Builder().build();

            Retrofit cleanRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(cleanHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            AuthService cleanAuthService = cleanRetrofit.create(AuthService.class);

            RefreshTokenDTO bodyDto = new RefreshTokenDTO(refreshToken);
            Call<JwtFromLoginDTO> call = cleanAuthService.refreshToken(bodyDto);
            Log.d("SVU", "Call refresh");
            retrofit2.Response<JwtFromLoginDTO> response = call.execute();
            if (response.isSuccessful() && response.body() != null) {
                JwtFromLoginDTO jwtFromLoginDTO = response.body();
                Log.d("SVU", "Call refresh access: " + jwtFromLoginDTO.getAccessToken());

                return jwtFromLoginDTO.getAccessToken();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Hàm đếm số lần chạy lại của 1 request để tránh lặp vô tận khi server lỗi lầm
    private static int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }

    //Hàm này xử lí gọi lại listener OnLogoutListener để các activity chuyển đến AuthActivity
    //Gọi và set lại access/refresh token
    private static void handleLogout() {
        accessToken = "";
        refreshToken = "";
        SharePrefUtils.saveAccessTokenAndRefreshTokenToPrefFile(
                AuthActivity.USER_AUTH_FILE,
                AuthActivity.KEY_ACCESS_TOKEN,
                AuthActivity.KEY_REFRESH_TOKEN,
                accessToken,
                refreshToken,
                context
        );

        AppExecutors.getInstance().diskIO().execute(() -> {
            AppDatabase.getInstance(context)
                    .userDAO().clear();
        });
        if(onLogoutListener != null) {
            onLogoutListener.onLogout();
            alreadyLogout = true;
        }
    }

    public interface OnLogoutListener {
        void onLogout();
    }
}