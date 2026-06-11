package com.example.modeldemo.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.modeldemo.data.model.User;
import com.example.modeldemo.data.repository.LoginRepository;
import com.example.modeldemo.utils.ValidationUtils;

/**
 * 登录 ViewModel
 * 使用 MVVM 架构，管理登录界面的状态和数据
 */
public class LoginViewModel extends ViewModel {
    
    private LoginRepository loginRepository;
    
    // 登录加载状态
    private MutableLiveData<Boolean> loadingState = new MutableLiveData<>(false);
    // 登录成功事件
    private MutableLiveData<User> loginSuccessEvent = new MutableLiveData<>();
    // 登录错误信息
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    // 用户名错误信息
    private MutableLiveData<String> usernameError = new MutableLiveData<>();
    // 密码错误信息
    private MutableLiveData<String> passwordError = new MutableLiveData<>();
    // 防止重复点击标志
    private boolean isLoginInProgress = false;

    public LoginViewModel() {
        this.loginRepository = new LoginRepository();
    }

    /**
     * 获取加载状态 LiveData
     */
    public LiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    /**
     * 获取登录成功事件 LiveData
     */
    public LiveData<User> getLoginSuccessEvent() {
        return loginSuccessEvent;
    }

    /**
     * 获取错误信息 LiveData
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * 获取用户名错误信息 LiveData
     */
    public LiveData<String> getUsernameError() {
        return usernameError;
    }

    /**
     * 获取密码错误信息 LiveData
     */
    public LiveData<String> getPasswordError() {
        return passwordError;
    }

    /**
     * 验证用户名和密码，返回是否通过验证
     */
    public boolean validateInput(String username, String password) {
        // 清除之前的错误提示
        usernameError.setValue(null);
        passwordError.setValue(null);
        errorMessage.setValue(null);

        boolean isValid = true;

        // 验证用户名
        String usernameErr = ValidationUtils.getUsernameErrorMessage(username);
        if (usernameErr != null) {
            usernameError.setValue(usernameErr);
            isValid = false;
        }

        // 验证密码
        String passwordErr = ValidationUtils.getPasswordErrorMessage(password);
        if (passwordErr != null) {
            passwordError.setValue(passwordErr);
            isValid = false;
        }

        return isValid;
    }

    /**
     * 执行登录操作
     * @param username 用户名
     * @param password 密码
     */
    public void login(String username, String password) {
        // 防止重复点击登录
        if (isLoginInProgress) {
            return;
        }

        // 验证输入
        if (!validateInput(username, password)) {
            return;
        }

        isLoginInProgress = true;
        loadingState.setValue(true);

        // 调用仓库层的登录方法
        loginRepository.login(username, password, new LoginRepository.LoginCallback() {
            @Override
            public void onSuccess(Object user) {
                loadingState.setValue(false);
                isLoginInProgress = false;
                loginSuccessEvent.setValue((User) user);
            }

            @Override
            public void onError(String msg) {
                loadingState.setValue(false);
                isLoginInProgress = false;
                errorMessage.setValue(msg);
            }
        });
    }

    /**
     * 清除错误提示
     */
    public void clearErrors() {
        usernameError.setValue(null);
        passwordError.setValue(null);
        errorMessage.setValue(null);
    }
}
