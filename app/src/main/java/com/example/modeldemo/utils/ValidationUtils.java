package com.example.modeldemo.utils;

import java.util.regex.Pattern;

/**
 * 输入验证工具类
 * 提供用户名、密码、邮箱等正则校验方法
 */
public class ValidationUtils {
    
    // 用户名规则：3-16 个字符，只能包含字母、数字、下划线
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,16}$");
    
    // 密码规则：6-20 个字符，至少包含字母和数字
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9]).{6,20}$");
    
    // 邮箱规则
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Z|a-z]{2,})$"
    );
    
    // 手机号规则（中国）
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^1[3-9]\\d{9}$"
    );

    /**
     * 验证用户名格式
     * @param username 用户名
     * @return 是否符合规则
     */
    public static boolean isValidUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * 获取用户名验证错误提示
     */
    public static String getUsernameErrorMessage(String username) {
        if (username == null || username.isEmpty()) {
            return "用户名不能为空";
        }
        if (username.length() < 3) {
            return "用户名至少 3 个字符";
        }
        if (username.length() > 16) {
            return "用户名最多 16 个字符";
        }
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            return "用户名只能包含字母、数字和下划线";
        }
        return null;
    }

    /**
     * 验证密码格式
     * @param password 密码
     * @return 是否符合规则
     */
    public static boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    /**
     * 获取密码验证错误提示
     */
    public static String getPasswordErrorMessage(String password) {
        if (password == null || password.isEmpty()) {
            return "密码不能为空";
        }
        if (password.length() < 6) {
            return "密码至少 6 个字符";
        }
        if (password.length() > 20) {
            return "密码最多 20 个字符";
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            return "密码必须包含字母和数字";
        }
        return null;
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 验证手机号格式（中国）
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * 验证非空字符串
     */
    public static boolean isNotEmpty(String text) {
        return text != null && !text.trim().isEmpty();
    }
}
