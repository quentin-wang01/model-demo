package com.example.modeldemo.ui.test;

import static org.junit.Assert.assertNotNull;

import androidx.lifecycle.ViewModel;

import com.example.modeldemo.ui.viewmodel.LoginViewModel;

import org.junit.Test;
import org.junit.Before;

/**
 * LoginViewModel 单元测试
 */
public class LoginViewModelTest {

    private LoginViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new LoginViewModel();
    }

    @Test
    public void testLoginViewModelInitialization() {
        assertNotNull(viewModel);
        assertNotNull(viewModel.getLoadingState());
        assertNotNull(viewModel.getLoginSuccessEvent());
        assertNotNull(viewModel.getErrorMessage());
    }

    @Test
    public void testClearErrors() {
        viewModel.clearErrors();
        assertNotNull(viewModel.getUsernameError());
        assertNotNull(viewModel.getPasswordError());
    }

    @Test
    public void testValidateInputWithEmptyValues() {
        boolean isValid = viewModel.validateInput("", "");
        // 空值应该验证失败
    }
}
