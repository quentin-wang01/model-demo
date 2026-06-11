# 登录模块架构设计

## 🏗️ 整体架构

```
┌─────────────────────────────────────────────────┐
│           UI Layer (表现层)                      │
│   LoginActivity + LoginViewModel               │
├─────────────────────────────────────────────────┤
│           ViewModel + LiveData                  │
│   (状态管理、事件分发)                           │
├─────────────────────────────────────────────────┤
│           Repository Layer (数据层)             │
│   LoginRepository                              │
├─────────────────────────────────────────────────┤
│           Network Layer (网络层)                │
│   ApiService + RetrofitClient                  │
├─────────────────────────────────────────────────┤
│           Local Storage Layer (本地存储层)      │
│   TokenManager (SharedPreferences)             │
├─────────────────────────────────────────────────┤
│           Utility Layer (工具层)                │
│   ValidationUtils                              │
└─────────────────────────────────────────────────┘
```

## 📊 数据流

```
用户输入
  ↓
ValidationUtils 验证
  ↓
验证通过 ✓
  ↓
LoginViewModel.login()
  ↓
LoginRepository.login()
  ↓
ApiService.login() (Retrofit)
  ↓
RetrofitClient (OkHttp)
  ↓
后端服务器
  ↓
返回 LoginResponse
  ↓
TokenManager.saveToken()
  ↓
LiveData 通知 UI 更新
  ↓
显示登录成功/失败提示
```

## 🔄 类关系图

```
┌──────────────────┐
│  LoginActivity   │
│  (UI)            │
└────────┬─────────┘
         │ uses
         ↓
┌──────────────────────────┐
│  LoginViewModel          │
│  - login()               │
│  - validateInput()       │
│  - loadingState          │
│  - errorMessage          │
└────────┬─────────────────┘
         │ uses
         ↓
┌──────────────────────────┐
│  LoginRepository         │
│  - login()               │
│  - LoginCallback         │
└────────┬─────────────────┘
         │ uses
         ↓
┌──────────────────────────┐
│  ApiService              │
│  - login()               │
│  - refreshToken()        │
└────────┬─────────────────┘
         │ uses
         ↓
┌──────────────────────────┐
│  RetrofitClient          │
│  - getRetrofitInstance() │
│  - createOkHttpClient()  │
└────────┬─────────────────┘
         │
         ├──→ TokenManager
         │    - saveToken()
         │    - getToken()
         │
         └──→ ValidationUtils
              - isValidUsername()
              - isValidPassword()
```

## 🔐 安全架构

```
User Input
    ↓
╔═══════════════════════╗
║  Client Validation    ║  ← ValidationUtils
╚═════════╦═════════════╝
          ↓
╔═══════════════════════╗
║  HTTPS Encryption     ║  ← OkHttp + SSL/TLS
╚═════════╦═════════════╝
          ↓
╔═══════════════════════╗
║  Backend Server       ║
║  (Server Validation)  ║
╚═════════╦═════════════╝
          ↓
╔═══════════════════════╗
║  Token Response       ║
╚═════════╦═════════════╝
          ↓
╔═══════════════════════╗
║  Client Storage       ║  ← SharedPreferences (Encrypted)
╚═══════════════════════╝
```

## 💾 数据存储结构

### SharedPreferences 结构
```
login_prefs {
    auth_token: "xxx-xxx-xxx",
    user_id: "123",
    username: "user123"
}
```

### 模型关系

```
LoginRequest
├── username: String
└── password: String

LoginResponse
├── code: int
├── message: String
└── data: LoginData
    ├── token: String
    └── user: User
        ├── id: String
        ├── username: String
        ├── email: String
        ├── avatar: String
        └── nickname: String
```

## 🧵 线程模型

```
Main Thread (UI)
    ↑
    │ (观察 LiveData)
    │
┌───┴─────────────────┐
│  ViewModel          │
│  (处理 UI 逻辑)      │
└───┬─────────────────┘
    │
    ↓ (回调)
┌─────────────────────────────────┐
│  Repository                     │
│  (调用 API)                     │
└────┬────────────────────────────┘
     │
     ↓ (异步请求)
   OkHttp Thread Pool
     │
     ↓
   HTTP Request
     │
     ↓
   HTTP Response
     │
     ↓ (回调到主线程)
   Handler/Message
     │
     ↓
┌──────────────────┐
│  Main Thread     │
│  (更新 UI)       │
└──────────────────┘
```

## 🔄 生命周期管理

```
LoginActivity
│
├── onCreate()
│   ├── 初始化 ViewBinding
│   ├── 初始化 ViewModel
│   ├── 注册 LiveData 观察者
│   └── 设置监听器
│
├── onStart()
│   └── 可见状态
│
├── onResume()
│   └── 活跃状态（可交互）
│
├── onPause()
│   └── 暂停状态
│
├── onStop()
│   └── 停止状态
│
├── onDestroy()
│   └── 清理 ViewBinding
│
└── onSaveInstanceState()
    └── 保存临时状态
```

## 🧪 测试架构

```
测试金字塔
┌───────────────┐
│  UI Tests     │  ← Espresso (少数)
│  (集成)        │
├─���─────────────┤
│  Widget Tests │  ← Mockito (部分)
│  (单个组件)    │
├───────────────┤
│  Unit Tests   │  ← JUnit (大多数)
│  (工具方法)    │
└───────────────┘
```

## 📈 性能优化

### 网络优化
- ✅ 连接池复用
- ✅ 请求/响应拦截器
- ✅ 超时控制（30秒）
- ✅ 异步请求

### UI 优化
- ✅ ViewBinding 减少反射
- ✅ LiveData 处理内存泄漏
- ✅ 防止重复网络请求
- ✅ 及时清理资源

### 存储优化
- ✅ SharedPreferences 原子操作
- ✅ 最小化序列化数据
- ✅ 定期清理过期数据

## 🚀 可扩展性设计

### 添加新功能示例：刷新 Token

```java
// 1. 在 ApiService 中添加接口
@POST("api/auth/refresh")
Call<LoginResponse> refreshToken(@Body RefreshTokenRequest request);

// 2. 在 LoginRepository 中添加方法
public void refreshToken(String token, RefreshTokenCallback callback) {
    // 实现刷新逻辑
}

// 3. 在 TokenManager 中添加支持
public static void refreshTokenIfNeeded() {
    // 自动刷新逻辑
}
```

### 添加第三方登录示例

```java
// 1. 新建 WeChatLoginRepository
public class WeChatLoginRepository {
    public void login(WeChatLoginCallback callback) {
        // 微信登录实现
    }
}

// 2. 复用现有的 TokenManager
TokenManager.saveToken(wechatToken);

// 3. 在 LoginActivity 中添加按钮
WeChat_LoginButton.setOnClickListener(...);
```

## 📝 关键设计模式

| 模式 | 使用位置 | 优势 |
|------|---------|------|
| MVVM | LoginActivity + LoginViewModel | 解耦 UI 和逻辑 |
| Repository | LoginRepository | 数据层抽象 |
| Singleton | RetrofitClient、TokenManager | 单一实例 |
| Observer | LiveData | 响应式更新 |
| Callback | LoginRepository.LoginCallback | 异步通信 |
| Builder | OkHttp 配置 | 灵活构建 |

---

**版本**: 1.0.0  
**最后更新**: 2026-06-11
