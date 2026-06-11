package com.example.modeldemo.data.api;

import com.example.modeldemo.data.model.LoginRequest;
import com.example.modeldemo.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 登录相关的网络接口定义
 * 使用 Retrofit 注解方式定义 HTTP 请求
 */
public interface ApiService {
    /**
     * 用户登录接口
     * @param request 登录请求参数（username、password）
     * @return Call 对象，用于执行异步网络请求
     */
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    /**
     * 刷新 Token 接口（可选）
     * @param token 旧的 token
     * @return 返回新的 token
     */
    @POST("api/auth/refresh")
    Call<LoginResponse> refreshToken(@Body RefreshTokenRequest request);

    /**
     * Token 刷新请求
     */
    class RefreshTokenRequest {
        public String token;

        public RefreshTokenRequest(String token) {
            this.token = token;
        }
    }
}
