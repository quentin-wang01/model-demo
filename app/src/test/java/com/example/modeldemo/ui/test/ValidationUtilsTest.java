package com.example.modeldemo.ui.test;

import org.junit.Test;

import com.example.modeldemo.utils.ValidationUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * ValidationUtils 单元测试
 */
public class ValidationUtilsTest {

    @Test
    public void testValidUsername() {
        // 有效用户名
        assertTrue(ValidationUtils.isValidUsername("user123"));
        assertTrue(ValidationUtils.isValidUsername("test_user"));
        
        // 无效用户名
        assertFalse(ValidationUtils.isValidUsername("ab")); // 太短
        assertFalse(ValidationUtils.isValidUsername("user@123")); // 包含特殊字符
        assertFalse(ValidationUtils.isValidUsername("")); // 空值
    }

    @Test
    public void testValidPassword() {
        // 有效密码
        assertTrue(ValidationUtils.isValidPassword("pass123"));
        assertTrue(ValidationUtils.isValidPassword("abc123xyz"));
        
        // 无效密码
        assertFalse(ValidationUtils.isValidPassword("12345")); // 太短且没有字母
        assertFalse(ValidationUtils.isValidPassword("abcdef")); // 没有数字
        assertFalse(ValidationUtils.isValidPassword("")); // 空值
    }

    @Test
    public void testUsernameErrorMessage() {
        // 空值
        assertNotNull(ValidationUtils.getUsernameErrorMessage(""));
        
        // 太短
        assertNotNull(ValidationUtils.getUsernameErrorMessage("ab"));
        
        // 有效用户名
        assertNull(ValidationUtils.getUsernameErrorMessage("user123"));
    }

    @Test
    public void testPasswordErrorMessage() {
        // 空值
        assertNotNull(ValidationUtils.getPasswordErrorMessage(""));
        
        // 太短
        assertNotNull(ValidationUtils.getPasswordErrorMessage("pass1"));
        
        // 有效密码
        assertNull(ValidationUtils.getPasswordErrorMessage("password123"));
    }

    @Test
    public void testValidEmail() {
        // 有效邮箱
        assertTrue(ValidationUtils.isValidEmail("user@example.com"));
        assertTrue(ValidationUtils.isValidEmail("test.user@gmail.com"));
        
        // 无效邮箱
        assertFalse(ValidationUtils.isValidEmail("invalid.email"));
        assertFalse(ValidationUtils.isValidEmail("@example.com"));
    }

    @Test
    public void testValidPhone() {
        // 有效手机号（中国）
        assertTrue(ValidationUtils.isValidPhone("13800138000"));
        assertTrue(ValidationUtils.isValidPhone("15900000000"));
        
        // 无效手机号
        assertFalse(ValidationUtils.isValidPhone("12345678901")); // 以1开头但第二位不对
        assertFalse(ValidationUtils.isValidPhone("1380013800")); // 长度不足
    }

    @Test
    public void testIsNotEmpty() {
        // 非空
        assertTrue(ValidationUtils.isNotEmpty("text"));
        
        // 空值或仅空格
        assertFalse(ValidationUtils.isNotEmpty(""));
        assertFalse(ValidationUtils.isNotEmpty("   "));
        assertFalse(ValidationUtils.isNotEmpty(null));
    }
}
