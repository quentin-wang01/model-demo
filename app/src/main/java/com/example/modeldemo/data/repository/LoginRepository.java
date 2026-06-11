package com.example.modeldemo.data.repository;

import com.example.modeldemo.data.api.ApiService;
import com.example.modeldemo.data.api.RetrofitClient;
import com.example.modeldemo.data.api.TokenManager;
import com.example.modeldemo.data.model.LoginRequest;
import com.example.modeldemo.data.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录数据仓库层
 * 封装网络请求逻辑，提供给 ViewModel 调用
 */
public class LoginRepository {
    private ApiService apiService;

    public LoginRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    /**
     * 执行登录操作
     * @param username 用户名
     * @param password 密码
     * @param callback 登录结果回调
     */
    public void login(String username, String password, LoginCallback callback) {
        LoginRequest request = new LoginRequest(username, password);

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess() && loginResponse.getData() != null) {
                        // 登录成功，保存 Token 和用户信息
                        String token = loginResponse.getData().getToken();
                        TokenManager.saveToken(token);

                        if (loginResponse.getData().getUser() != null) {
                            TokenManager.saveUserInfo(
                                    loginResponse.getData().getUser().getId(),
                                    loginResponse.getData().getUser().getUsername()
                            );
                        }

                        callback.onSuccess(loginResponse.getData().getUser());
                    } else {
                        // 登录失败（服务器返回失败状态）
                        callback.onError(loginResponse.getMessage() != null ? 
                                loginResponse.getMessage() : "登录失败");
                    }
                } else {
                    // HTTP 响应错误
                    callback.onError("登录请求失败：" + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // 网络请求异常
                callback.onError("网络错误：" + (t.getMessage() != null ? t.getMessage() : "未知错误"));
            }
        });
    }

    /**
     * 登录结果回调接口
     */
    public interface LoginCallback {
        /**
         * 登录成功
         */
        void onSuccess(Object user);

        /**
         * 登录失败
         */
        void onError(String errorMessage);
    }
}
