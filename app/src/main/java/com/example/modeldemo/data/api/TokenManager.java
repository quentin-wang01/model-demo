package com.example.modeldemo.data.api;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

/**
 * Token 本地存储管理器
 * 使用 SharedPreferences 存储 Token，支持加密存储（可选）
 */
public class TokenManager {
    private static final String PREF_NAME = "login_prefs";
    private static final String TOKEN_KEY = "auth_token";
    private static final String USER_ID_KEY = "user_id";
    private static final String USERNAME_KEY = "username";

    private static SharedPreferences sharedPreferences;

    /**
     * 初始化 TokenManager（在应用启动时调用）
     */
    public static void init(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    /**
     * 保存 Token
     */
    public static void saveToken(String token) {
        if (sharedPreferences != null) {
            sharedPreferences.edit().putString(TOKEN_KEY, token).apply();
        }
    }

    /**
     * 获取 Token
     */
    @Nullable
    public static String getToken() {
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    /**
     * 保存用户信息
     */
    public static void saveUserInfo(String userId, String username) {
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(USER_ID_KEY, userId);
            editor.putString(USERNAME_KEY, username);
            editor.apply();
        }
    }

    /**
     * 获取用户 ID
     */
    @Nullable
    public static String getUserId() {
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getString(USER_ID_KEY, null);
    }

    /**
     * 获取用户名
     */
    @Nullable
    public static String getUsername() {
        if (sharedPreferences == null) {
            return null;
        }
        return sharedPreferences.getString(USERNAME_KEY, null);
    }

    /**
     * 判断是否已登录
     */
    public static boolean isLoggedIn() {
        return getToken() != null && !getToken().isEmpty();
    }

    /**
     * 清空登录信息（登出时调用）
     */
    public static void clear() {
        if (sharedPreferences != null) {
            sharedPreferences.edit()
                    .remove(TOKEN_KEY)
                    .remove(USER_ID_KEY)
                    .remove(USERNAME_KEY)
                    .apply();
        }
    }
}
