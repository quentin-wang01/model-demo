# 登录模块实现指南

## 📋 项目结构

```
login-module/
├── data/
│   ├── api/
│   │   ├── ApiService.java           # Retrofit 接口定义
│   │   ├── RetrofitClient.java       # Retrofit 单例配置
│   │   └── TokenManager.java         # Token 本地存储管理
│   ├── model/
│   │   ├── LoginRequest.java         # 登录请求 DTO
│   │   ├── LoginResponse.java        # 登录响应 DTO
│   │   └── User.java                 # 用户信息实体
│   └── repository/
│       └── LoginRepository.java      # 数据仓库层
├── ui/
│   ├── activity/
│   │   └── LoginActivity.java        # 登录界面
│   └── viewmodel/
│       └── LoginViewModel.java       # 登录 ViewModel
├── utils/
│   └── ValidationUtils.java          # 正则校验工具
└── test/
    └── ValidationUtilsTest.java      # 单元测试
```

## ✨ 核心功能

### 1. 用户名/密码验证 ✓
- **用户名规则**：3-16 个字符，只能包含字母、数字、下划线
- **密码规则**：6-20 个字符，至少包含字母和数字
- **实时错误提示**：输入框错误信息实时显示

### 2. 网络请求 (Retrofit) ✓
```java
// 基础配置
BASE_URL = "https://api.example.com/"  // 修改为实际后端地址
连接超时：30 秒
读超时：30 秒
写超时：30 秒
```

### 3. Token 管理 ✓
- 登录成功后自动保存 Token 到 SharedPreferences
- 支持 Token 查询、更新、清空
- 网络请求自动添加 Authorization 请求头

### 4. 加载状态处理 ✓
- 登录期间按钮禁用，显示加载进度条
- 防止重复点击登录

### 5. 错误处理 ✓
- 网络异常处理
- HTTP 错误状态处理
- 业务异常处理

## 🚀 使用方法

### 1. 初始化（在 Application 或 MainActivity 中）
```java
// 初始化 TokenManager
TokenManager.init(context);

// 初始化 Retrofit（在 RetrofitClient 中自动完成）
```

### 2. 启动登录界面
```java
Intent intent = new Intent(MainActivity.this, LoginActivity.class);
startActivity(intent);
```

### 3. 登录流程
```
用户输入用户名/密码
  ↓
客户端验证格式
  ↓
验证通过 → 发送网络请求
  ↓
后端验证 → 返回 Token
  ↓
保存 Token 到本地
  ↓
登录成功 → 跳转主界面
```

## 🔒 安全建议

1. **密码传输**：使用 HTTPS 加密传输
2. **Token 存储**：考虑使用 Android Keystore 加密 Token
3. **会话管理**：实现 Token 过期刷新机制
4. **API 安全**：后端需要验证 Token 有效性

## 📝 后端 API 约定

### 登录接口
```
POST /api/auth/login

请求参数：
{
  "username": "user123",
  "password": "pass123"
}

成功响应 (200)：
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "xxx-xxx-xxx",
    "user": {
      "id": "1",
      "username": "user123",
      "email": "user@example.com",
      "avatar": "https://...",
      "nickname": "用户昵称"
    }
  }
}

失败响应：
{
  "code": 401,
  "message": "用户名或密码错误",
  "data": null
}
```

## 🧪 单元测试

运行验证工具测试：
```bash
./gradlew test
```

测试覆盖：
- ✓ 用户名格式验证
- ✓ 密码格式验证
- ✓ 邮箱格式验证
- ✓ 手机号格式验证
- ✓ 错误消息提示

## ⚙️ 配置修改

### 修改后端地址
**文件**：`RetrofitClient.java`
```java
private static final String BASE_URL = "https://your-api.com/";
```

### 修改超时时间
**文件**：`RetrofitClient.java`
```java
private static final long TIMEOUT_SECONDS = 30; // 修改为需要的秒数
```

### 修改验证规则
**文件**：`ValidationUtils.java`
```java
private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{3,16}$");
private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9]).{6,20}$");
```

## 🔄 扩展功能建议

1. **记住密码**：使用 SharedPreferences 加密存储
2. **自动登录**：使用 Token 自动登录
3. **第三方登录**：集成微信、支付宝等
4. **生物识别**：指纹/面容识别登录
5. **OTP 验证**：一次性密码校验

## 📚 技术栈

- **网络框架**：Retrofit 2.9.0 + OkHttp 4.11.0
- **JSON 处理**：Gson 2.10.1
- **ViewModel**：AndroidX Lifecycle 2.6.1
- **UI 框架**：Material Design 3
- **本地存储**：SharedPreferences

## 🐛 常见问题

### Q: Token 存储在哪里？
A: 默认存储在 SharedPreferences（`login_prefs` 文件），建议生产环境使用 Android Keystore 加密。

### Q: 如何处理 Token 过期？
A: 在网络请求时，如果返回 401 状态码，触发 Token 刷新流程。

### Q: 支持离线登录吗？
A: 当前实现不支持。建议在网络不可用时显示缓存用户信息（无重新登录）。

### Q: 如何防止 XSS 攻击？
A: 确保所有用户输入都经过验证和转义，不要在 WebView 中加载不可信内容。

## 📞 集成步骤

1. ✅ 复制所有文件到对应目录
2. ✅ 更新 `build.gradle` 添加依赖
3. ✅ 修改后端 API 地址
4. ✅ 在 `AndroidManifest.xml` 中注册 LoginActivity
5. ✅ 添加网络权限
6. ✅ 运行并测试

## 📄 许可证

MIT License

---

**版本**：1.0.0  
**最后更新**：2026-06-11
