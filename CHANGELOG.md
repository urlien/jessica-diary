# 杰西卡日记 — 更新日志

## v3.3.0 (2026-06-08)

### 修复
- **知识库上传手机端无响应**：将 `<label for="">` 改为 `<div onclick="">` 直接触发文件选择，兼容 Android WebView
- **通知权限检查显示浏览器设置**：重写 `checkNotificationPermission()`，优先使用原生桥接（JessicaBridge）请求系统通知权限，不再走 Web Notification API
- **通知开关 HTML 属性**：移除 `checked` 硬编码，由 JS 动态控制初始状态

### 优化
- **后台省电模式**：页面不可见时自动释放 WakeLock，暂停动态背景、时钟等非关键定时器，恢复可见时自动重启
- **定时器优化**：时钟/动态背景/心情刷新等定时器增加 `document.hidden` 检查，后台不执行
- **心情刷新间隔**：从 15 秒调整为 30 秒，减少 CPU 占用
- **后台运行提示**：更新说明文案，注明已优化省电

### 说明
- APP_VERSION 升级到 3.3.0
- APK 安装失败(-7) 是签名不一致导致，需先卸载旧版再安装新版
- APK 启动图标（mipmap）在 Android 项目中修改，不在 HTML 中

---

## v3.2.0 (2026-06-07)

### 修复
- **通知开关自动取消**：去掉 `checked` 硬编码，toggle 切换时立即保存到 localStorage（不再需要点"保存"按钮）
- **移动端数据导出**：重写 `fallbackDownload`，移动端优先走 Web Share API 分享文件，失败再降级到下载
- **知识库上传**：移动端 `<label for="">` 已正确绑定，点击可触发文件选择

### 新增功能
- **📦 导出压缩包**：新增 ZIP 格式导出（使用 JSZip 压缩），移动端优先走 Web Share API 分享 .zip 文件
- **表情包批量添加**：表情包上传支持多选图片，拖拽也支持多文件
- **版本号**：APP_VERSION 升级到 3.2.0，build.gradle versionName 统一为 3.2.0

### 改动
- **build-apk.sh**：打包时自动将 strings.xml 的 app_name 从"杰西卡日记"改为"杰西卡"
- **JSZip 依赖**：新增 `jszip@3.10.1` CDN 引用

---

## 移动端全屏适配 (2026-06-07)

### 适配目标
- **Redmi K60** (23013RK75C)
- 屏幕：6.67" / 3200×1440
- 系统：Android 15 / HyperOS 3.0.5.0
- SoC：骁龙 8+ Gen 1 / 16GB RAM / 1TB 存储

### HTML 修改
1. `html, body` — 添加 `overscroll-behavior: none` + `-webkit-overflow-scrolling: auto`，防止弹性滚动
2. `body` — `height: 100vh` → `height: 100%`（100vh 在 Android WebView 中不稳定，可能包含状态栏/导航栏区域）
3. `#chatView` — `height: 100vh` → `height: 100%`
4. `#homepage` — 添加 `overscroll-behavior: contain`，防止滚动穿透
5. `.chat-area` — 添加 `overscroll-behavior: contain`
6. `.sidebar` — 添加 `overscroll-behavior: contain`

### Android 修改 (MainActivity.java)
1. `setLoadWithOverviewMode(true)` — 概览模式加载，内容适配屏幕宽度
2. `setUseWideViewPort(true)` — 使用宽视口，避免缩放
3. `setBuiltInZoomControls(false)` — 禁用系统缩放控件
4. `setSupportZoom(false)` — 完全禁止缩放
5. `setDisplayZoomControls(false)` — 不显示缩放按钮

### 原理
- `100vh` 在 Android WebView 中表现不稳定（可能包含状态栏/导航栏区域），改用 `100%` + `position: fixed; inset: 0` 更可靠
- `overscroll-behavior: none` 防止弹性滚动/下拉刷新干扰
- `overscroll-behavior: contain` 防止滚动穿透到父元素
- WebView 禁用缩放后，HTML 完全控制布局，不会被系统缩放机制干扰
- 全屏沉浸模式（`SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION` + `FLAG_LAYOUT_NO_LIMITS`）让内容延伸到状态栏和导航栏后面，配合 `env(safe-area-inset-*)` 适配安全区域

---

## GitHub 仓库整理 (2026-06-07)

### 保留的仓库
| 仓库 | 用途 | 地址 |
|------|------|------|
| urlien/jessica-diary | HTML 源码 + APK | https://github.com/urlien/jessica-diary |
| urlien/jessica-build-env | 编译环境 (JDK17 + Gradle 8.4 + Android SDK 34) | https://github.com/urlien/jessica-build-env |

### 已删除的仓库
| 仓库 | 原因 |
|------|------|
| urlien/- | 空仓库，无内容，误创建 |
| urlien/files | OpenClaw 工作区旧备份，与杰西卡项目无关 |

---

## v3.1.0 (2026-06-07)

### 新增功能
- **版本号系统**：APP_VERSION = 3.1.0，设置页底部显示版本号，localStorage 记录版本便于后续数据迁移
- **压缩包导出**：新增"📦 导出压缩包"按钮，使用 JSZip 生成 .zip 文件，移动端优先走 Web Share API 分享
- **表情包批量添加**：上传表情包支持多选图片，一次添加多张
- **首页日记编辑+重新生成**：日记卡片底部新增"✏️ 编辑"和"🔄 重新生成"按钮
- **动态人设显示**：设置页新增"📅 当前人设"区块，显示杰西卡和用户当前的学业状态
- **地图系统重做**：引入 Leaflet 地图库，中心固定📍图钉，拖动地图选位置，实时反向地理编码

### 修复
- **移动端数据导出**：修复手机端点击"导出数据"只显示提示但无实际操作的问题，改用 Web Share API
- **知识库上传**：移动端点击"选择文件"无反应的问题，改用 `<label for="">` 替代 `onclick`
- **消息通知自动取消**：去掉硬编码 `checked`，toggle 切换时立即保存到 localStorage
- **天气系统纯中文**：城市名使用用户输入的中文名，单位改为千米/时、百帕、毫米
- **主动消息捏造记忆**：新增规则，杰西卡主动找话题时不会虚构不存在的对话

### 改动
- **顶部安全区域**：移动端 header 增加 `env(safe-area-inset-top)` 防止与手机状态栏重合
- **固定界面缩放**：viewport 增加 `maximum-scale=1.0, user-scalable=no`，html 增加 `overscroll-behavior: none`
- **用户名默认值**：不再默认填"杰西卡"，改为"你"
- **首页计数**：区分"条"（单条消息）和"篇"（日记），显示"X 条对话 · Y 篇日记"
- **心情/状态标签**：删除文字标签，仅保留 emoji 标签
- **位置回复**：改用正常消息流程（typing indicator + 错误处理）
- **APP 图标**：改为 J 字母头像（SVG favicon）
- **APK 名称**：打包时 strings.xml 的 app_name 改为"杰西卡"

### 动态人设时间轴
| 时间段 | 杰西卡 | 用户 |
|--------|--------|------|
| 2026年6月前 | 苏州科技大学·视觉传达·大四·在读 | 吉林建筑大学·城乡规划·大五·在读 |
| 2026年6-8月 | 本科毕业，9月去南京工业大学读城乡规划硕士 | 本科毕业，9月去苏州科技大学读城乡规划硕士 |
| 2026年9月起 | 南京工业大学·城乡规划·研一 | 苏州科技大学·城乡规划·研一 |

- 杰西卡生日：2004年2月8日
- 杰西卡家住：上海市闵行区虹桥街道
- 用户家住：镇江市丹徒区

---

## v3.0.0 (2026-06-06) — 初始版本
- WebView 壳 Android APP
- 原生通知桥接（JessicaBridge）
- 后台运行（前台 Service + WakeLock）
- 开机自启
- 消息通知设置
- 默认头像

---

## 编译环境说明

### 环境组成
- JDK 17.0.9（Oracle）
- Gradle 8.4
- Android SDK 34 + Build Tools 34.0.0
- Android 项目模板（jessica-diary/）

### 仓库地址
https://github.com/urlien/jessica-build-env

### Release 下载
https://github.com/urlien/jessica-build-env/releases/download/v1.0/build-env.tar.gz（611MB）

### 一键打包
```bash
cd /root/.openclaw/workspace/jessica_project
bash build-apk.sh
```

### APK 输出
- 路径：`/root/.openclaw/workspace/jessica_project/杰西卡v3.2.0.apk`
- 大小：约 3.6MB
- 包含：WebView 壳 + HTML（assets/index.html）+ 原生通知桥接 + 前台 Service + 开机自启

### 注意事项
- build.gradle 中 Kotlin stdlib 版本已强制统一为 1.8.22
- settings.gradle 使用 dependencyResolutionManagement（不是 dependencyResolution）
- AGP 8.1.4 + Gradle 8.4
- MainActivity.java 有 deprecated API 警告（正常，不影响功能）
