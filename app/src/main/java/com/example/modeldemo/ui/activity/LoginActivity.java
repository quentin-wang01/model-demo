package com.example.modeldemo.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.modeldemo.R;
import com.example.modeldemo.data.api.TokenManager;
import com.example.modeldemo.databinding.ActivityLoginBinding;
import com.example.modeldemo.ui.viewmodel.LoginViewModel;

/**
 * 登录界面 Activity
 * 使用 ViewBinding 绑定布局，MVVM 架构模式
 */
public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 初始化 ViewBinding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 初始化 TokenManager
        TokenManager.init(this);

        // 初始化 ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // 设置登录按钮点击监听
        binding.loginButton.setOnClickListener(v -> performLogin());

        // 监听加载状态
        viewModel.getLoadingState().observe(this, isLoading -> {
            binding.loginButton.setEnabled(!isLoading);
            binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            binding.loginButton.setText(isLoading ? "登录中..." : "登录");
        });

        // 监听登录成功事件
        viewModel.getLoginSuccessEvent().observe(this, user -> {
            if (user != null) {
                Toast.makeText(this, "登录成功！欢迎 " + user.getUsername(), 
                        Toast.LENGTH_SHORT).show();
                // 跳转到主界面或其他界面
                goToMainActivity();
            }
        });

        // 监听错误信息
        viewModel.getErrorMessage().observe(this, errorMsg -> {
            if (!TextUtils.isEmpty(errorMsg)) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        // 监听用户名错误
        viewModel.getUsernameError().observe(this, error -> {
            binding.usernameInput.setError(error);
        });

        // 监听密码错误
        viewModel.getPasswordError().observe(this, error -> {
            binding.passwordInput.setError(error);
        });
    }

    /**
     * 执行登录操作
     */
    private void performLogin() {
        String username = binding.usernameInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();

        // 调用 ViewModel 进行登录
        viewModel.login(username, password);
    }

    /**
     * 跳转到主界面
     * （根据项目实际情况修改）
     */
    private void goToMainActivity() {
        // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        // startActivity(intent);
        // finish();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 清理 ViewBinding 引用
        binding = null;
    }
}
