package com.infix.phukiencongnghe.utils;

import static com.infix.phukiencongnghe.EvnUtils.BASE_URL;

import android.content.Context;

import com.google.gson.Gson;
import com.infix.phukiencongnghe.data.dto.response.JwtFromLoginDTO;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static boolean alreadyLogout = false;
    private static OnLogoutListener onLogoutListener;
    private static OkHttpClient okHttpClient = null;
    private static Retrofit retrofit = null;
    private static Context context;

    private static String accessToken = "";
    private static String refreshToken = "";

    //Khởi tạo OkHttpClient tự lấy refresh token
    public static synchronized OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {//Chặn request để đính header
                        Request originalRequest = chain.request();

                        if (accessToken != null && !accessToken.isEmpty()) {
                            Request authenticatedRequest = originalRequest.newBuilder()
                                    .header("Authorization", "Bearer " + accessToken)
                                    .build();
                            return chain.proceed(authenticatedRequest);
                        }
                        return chain.proceed(originalRequest);
                    })
                    //Nếu server trả 401 thì gọi hàm này
                    .authenticator(new Authenticator() {
                        @Override
                        public Request authenticate(Route route, Response response) throws IOException {
                            //Trường hợp refresh token hết hạn thì xử lí đăng xuất
                            if (responseCount(response) >= 2) {
                                handleLogout();
                                return null;//hủy request
                            }

                            //đồng bộ luồng để tránh tranh chấp gọi mạng
                            synchronized (this) {
                                String tokenInRequest = response.request().header("Authorization");

                                //nếu request trước đã xin access mới thì tận dụng lại token đó
                                if (tokenInRequest != null && !tokenInRequest.equals("Bearer " + accessToken)) {
                                    return response.request().newBuilder()
                                            .header("Authorization", "Bearer " + accessToken)
                                            .build();
                                }

                                //kích hoạt access token mơi
                                String newAccessToken = executeRefreshApi();
                                if (newAccessToken != null) {
                                    accessToken = newAccessToken;
                                    return response.request().newBuilder()
                                            .header("Authorization", "Bearer " + accessToken)
                                            .build();
                                } else {
                                    handleLogout();
                                    return null;
                                }
                            }
                        }
                    })
                    .build();
        }
        return okHttpClient;
    }

    public static synchronized Retrofit getRetrofitClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    //Hàm này có nhiệm vụ thiết lập hàm kích hoạt onLogout() mỗi khi chuyển qua 1 activity mới.
    //Các activity sẽ tự đi kiểm soát logic logout tương ứng
    public static void setOnLogoutListener(OnLogoutListener onLogoutListener) {
        ApiClient.onLogoutListener = onLogoutListener;
        alreadyLogout = false;
    }

    public static void setContext(Context context) {
        ApiClient.context = context;
    }

    //Phương thức này sẽ tạo ra 1 request để xin access token từ refresh token,
    //trong trường hợp refresh token bị hết hạn hoặc lỗi thì sẽ bị vòng lặp nếu dùng Ok cũ
    private static String executeRefreshApi() {
        OkHttpClient cleanClient = new OkHttpClient();
        RequestBody body = new FormBody.Builder()
                .add("refreshToken", refreshToken)
                .build();
        Request request = new Request.Builder()
                .url(BASE_URL + "/api/v1/auth/refresh-token")
                .post(body)
                .build();

        try (Response response = cleanClient.newCall(request).execute()) { // .execute() để chạy đồng bộ (Synchronous)
            if (response.isSuccessful() && response.body() != null) {
                JwtFromLoginDTO jwtFromLoginDTO = new Gson().fromJson(response.body().string(), JwtFromLoginDTO.class);

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

    private static void handleLogout() {
        accessToken = "";
        refreshToken = "";
        onLogoutListener.onLogout();
        alreadyLogout = true;
    }

    public interface OnLogoutListener {
        void onLogout();
    }
}