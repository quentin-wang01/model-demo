package com.example.modeldemo.data.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Retrofit 客户端单例
 * 统一管理 HTTP 配置、拦截器、超时设置等
 */
public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.example.com/"; // 替换为实际的后端地址
    private static final long TIMEOUT_SECONDS = 30;

    private RetrofitClient() {
    }

    /**
     * 获取 Retrofit 单例实例
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            synchronized (RetrofitClient.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(createOkHttpClient())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    /**
     * 创建并配置 OkHttpClient
     * 包含日志拦截器、超时设置、请求/响应拦截等
     */
    private static OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        // 仅在 Debug 模式下输出详细日志
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                // 设置连接超时
                .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                // 设置读超时
                .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                // 设置写超时
                .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                // 添加日志拦截器（仅用于调试）
                .addInterceptor(loggingInterceptor)
                // 添加请求头拦截器
                .addInterceptor(new HeaderInterceptor())
                .build();
    }

    /**
     * 获取 ApiService 实例
     */
    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }

    /**
     * 请求头拦截器 - 为所有请求添加通用请求头
     */
    static class HeaderInterceptor implements okhttp3.Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws java.io.IOException {
            okhttp3.Request originalRequest = chain.request();
            okhttp3.Request.Builder builder = originalRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "Android");

            // 如果已登录，添加 Authorization 请求头
            String token = TokenManager.getToken();
            if (token != null && !token.isEmpty()) {
                builder.header("Authorization", "Bearer " + token);
            }

            okhttp3.Request newRequest = builder.build();
            return chain.proceed(newRequest);
        }
    }
}
