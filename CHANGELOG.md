# 杰西卡日记 — 更新日志

## v3.3.1 (2026-06-13)

### 22:41 — 代码审查
- 克隆仓库，对比用户上传的 HTML 与仓库版本，确认一致（v3.3.0，最后 commit 06-08 03:48）
- 逐项检查 5 个待办事项的状态

### 22:53 — Bug 修复（文件上传全面修复）
- **修复所有 file input 在 Android WebView 中无法触发的问题**：8 个 `<input type="file">` 全部从 `display:none` 改为 `position:absolute;opacity:0;width:0;height:0;pointer-events:none`
- 根因：Android WebView 中 `display:none` 的元素无法被程序化 `.click()` 触发，`<label for="">` 同样失效
- 涉及：背景上传、杰西卡头像、用户头像、聊天附件、知识库文件上传、数据导入、自定义提示音、表情包上传

### 22:53 — 知识库页面重做
- **文件列表改为卡片网格布局**：`display:grid; grid-template-columns:repeat(2,1fr)`（≥600px 时 3 列）
- 每张卡片：大图标 + 文件名（去掉后缀）+ 文件大小，右上角删除按钮
- `_renderFileList()` 函数重写，生成卡片 HTML
- GitHub 导入列表同步改为卡片网格，checkbox 放在左上角
- 新增 `.kb-storage-info` 样式，存储信息横跨整行

### 22:53 — 首页顶部 safe-area 适配
- `.hp-header` 的 `padding-top` 加入 `env(safe-area-inset-top, 0px)`
- 4 个断点全部修改：默认 / ≤768px / ≤480px / ≤360px
- 解决有刘海/药丸屏手机上首页顶部被遮挡的问题

### 23:07 — 推送
- 3 个文件推送到 GitHub：`jessica_full.html`、`CHANGELOG.md`、`notes/2026-06-13.md`
- 推送完成后清理 git remote URL 中的 Token

### 23:12 — 云备份分析
- 检查 Gist（`bfaeefb7cb7ee121da56b482cba32856`）中的备份数据
- 旧备份（06-08）：169 条消息，55 项数据，`_version: 2`
- 新备份（06-13 23:21）：185 条消息，40 项数据，`_version: 2`
- 发现：`_idb_jp_entries`（IndexedDB 日记条目）从未被备份进去
- 根因：手机上的 APP 运行的是旧版 `_collectData()`（v2），不含 IndexedDB 收集逻辑
- `jessica_diary_chat` 字段从新备份中消失（可能 localStorage 被清理）

### 23:15 — 对话记录导出
- 导出全部 169 条对话内容（05-15 ~ 06-08）
- 关键事件：克洛伊人设、知识库上传、搜索引擎大战、记忆问题、坐标分享、云同步开发

### 23:22 — 新备份到达
- 用户在 APP 内触发备份，新 Gist 文件 `jessica-backup-2026-06-13-15-21-35.json`（143KB）
- 消息更新到 06-13 19:38，新增 16 条对话
- 但 `_idb_jp_entries` 仍然缺失，确认需要重新打包 APK

### 23:26 — 工作区整理
- 删除残留文件（`jessica_repo.html`、`kb_reference.jpg`、截图）
- `jessica_uploaded.html` → `jessica_full.html`
- 工作区与 `jessica-repo/` 同步

### 23:29 — APK 打包尝试
- 编译环境（611MB）不在本机，尝试下载
- GitHub releases 下载速度 ~330KB/s，预计 30 分钟
- 使用 Token 认证加速下载

### 说明
- 版本号未升级（仍为 3.3.0），需手动更新 `APP_VERSION` 和 `build.gradle`
- 灵感抽屉已在之前版本中移除，首页无此入口
- 云同步 `_collectData()` 已包含 `jessica_diary_chat` 和 IndexedDB 日记条目（v3 版本）
- 手机端 APP 需重新打包才能修复日记备份缺失问题

---

## v3.3.0 (2026-06-08)

### 02:08 — 初始检查
- 克隆 GitHub 仓库 `urlien/jessica-diary`，检查当前版本 v3.2.0
- 对比用户上传的旧版 HTML（v3.1.0）与仓库新版（v3.2.0），确认仓库已是最新
- 下载并分析用户截图 5 张，确认具体问题

### 02:09 — Bug 修复（知识库上传 / 通知权限 / 通知开关）
- **修复知识库上传手机端无响应**：将 `<label for="kbFileInput">` 改为 `<div onclick="document.getElementById('kbFileInput').click()">`，解决 Android WebView 中 `<label for="">` 不触发文件选择的问题
- **修复通知权限检查显示浏览器设置**：完全重写 `checkNotificationPermission()` 函数，优先使用 `JessicaBridge.requestNotificationPermission()` 原生桥接请求系统通知权限，不再走 Web Notification API（该 API 在 WebView 中会弹出浏览器设置）
- **修复通知开关 HTML 属性**：移除 `<input id="notifToggle" checked>` 中的 `checked` 硬编码，由 JS 动态控制初始状态
- **APP_VERSION 升级**：3.2.0 → 3.3.0

### 02:09 — 后台省电优化
- **新增省电模式**：`NotificationBridge` 新增 `_pauseBackgroundTimers()` / `_resumeBackgroundTimers()` 方法，页面不可见时自动暂停非关键定时器
- **WakeLock 优化**：页面不可见时自动释放 WakeLock 节省电量，恢复可见时重新获取
- **定时器优化**：`updateHpClock`、`updateClock`、`applyDynamicBackground` 等定时器增加 `document.hidden` 检查，后台不执行
- **心情刷新间隔**：从 15 秒调整为 30 秒，减少 CPU 占用
- **后台运行提示文案更新**：注明已优化省电

### 02:23 — 围棋游戏完全重写
- **修复自杀判断**：新增 `_isLegalMove()` 方法，落子后模拟提子再检查己方是否有气，无气则判定为非法（自杀禁手）
- **修复打劫判断**：正确检测单子循环提劫（仅当提一子且被提子位置等于上一手时设置 koPoint）
- **重写 AI 评估**：连五 > 活四 > 冲四 > 活三，加入气数、提子潜力、角边位置权重
- **新增棋盘尺寸选择**：9×9 / 13×13 / 19×19，通过 `<select>` 切换
- **开局策略**：优先占角、星位加分
- **内部方法重命名**：`getGroup` → `_getGroup`，`countLiberties` 合并入 `_getGroup`，`placeStone` → `_placeStone`，`captureStones` 合并入 `_placeStone`

### 02:41 — 云同步功能（GitHub Gist）
- **新增 ☁️ 云同步区块**：设置页「数据管理」下方新增云同步 UI，包含 Token 输入框、备份/恢复/状态三个按钮
- **`cloudSync` 对象**：实现 `_collectData()`（收集 localStorage）、`_restoreData()`（恢复数据）、`_api()`（GitHub API 封装）、`_findGist()`（查找已有备份）、`backup()`、`restore()`、`status()` 方法
- **数据存储**：备份到用户 GitHub 账号的私有 Gist，描述为 `jessica-diary-backup (auto)`
- **Token 管理**：Token 存在 localStorage `jessica_gist_token`，设置页自动填充
- **自动查找**：首次备份后 Gist ID 存入 `jessica_gist_id`，后续自动定位

### 02:46 — 新增 4 个小游戏
- **🔢 2048**：`Game2048` 对象，4×4 网格，滑动合并数字，支持方向键控制，记录最高分
- **🐍 贪吃蛇**：`SnakeGame` 对象，Canvas 渲染，15×15 网格，方向键控制，自动增长
- **🧱 俄罗斯方块**：`TetrisGame` 对象，Canvas 渲染，10×20 网格，7 种方块，支持旋转/消行
- **💣 扫雷**：`MinesweeperGame` 对象，3 种难度（简单 9×9/中等 16×16/困难 16×30），左键揭开/右键标旗
- **游戏入口**：游戏中心新增 4 个卡片，`selectGame()` 函数扩展支持新游戏
- **键盘事件**：全局 keydown 监听，根据当前活动 overlay 分发到对应游戏

### 02:51 — Token 调试
- 用户提供第一个 Token → `Bad credentials`（无效）
- 用户提供第二个 Token → 有效，备份成功
- GitHub 发来安全邮件：Token 被检测出现在 Gist 内容中，自动撤销
- 原因：`_collectData()` 把 `jessica_gist_token` 也打包进了备份数据

### 02:55 — 云同步安全修复
- **修复 Token 泄露到 Gist 内容的问题**：`_collectData()` 新增 `SKIP` 集合，排除 `jessica_gist_token`、`jessica_gist_id`、`jessica_last_backup` 三个字段
- **`_restoreData()` 同步排除**：恢复时也跳过这三个字段，不覆盖本地 Token
- 用户提供第三个 Token → 有效，备份成功

### 03:01 — 云同步多版本备份
- **保留最近 5 个备份版本**：每次备份生成带时间戳的文件名（`jessica-backup-2026-06-08-03-01-00.json`），不再覆盖旧备份
- **自动清理**：超过 5 个版本时自动删除最旧的
- **恢复时自动找最新**：`restore()` 按文件名排序，恢复最新的备份

### 03:03 — 灵感抽屉
- **新增 💡 灵感抽屉**：首页新增卡片入口，点击打开灵感管理面板
- **三种类型**：💡 奇思妙想 / 🌟 优秀案例 / 📋 未来计划
- **链接识别**：输入 URL 自动变为可点击链接
- **预置内容**：默认包含 Kirameku 和 XinghuisamaBlogs 两个优秀案例
- **数据存储**：localStorage `jessica_ideas`，支持云同步备份

### 03:05 — 工作记录文件夹
- **新增 `notes/` 文件夹**：用于存放每次工作的吐槽和记录
- **首篇记录**：`notes/2026-06-08.md`，包含本次全部工作内容和吐槽

### Git 提交记录
| 时间 | Commit | 内容 |
|------|--------|------|
| 02:23 | `06e591a` | feat: v3.3.0 — 知识库上传修复/通知权限重做/后台省电/版本升级 |
| 02:34 | `0ab55b5` | fix: 围棋规则完全重写 — 自杀判断/打劫/AI评估/棋盘尺寸选择 |
| 02:44 | `973e946` | feat: 云同步(GitHub Gist) + 新增4个小游戏 |
| 02:56 | `e634939` | fix: 云同步备份时排除Token字段，防止GitHub自动撤销 |
| 03:01 | `8470dd1` | docs: v3.3.0 更新日志细化 — 逐条记录时间线和修改内容 |
| 03:04 | `9623f08` | feat: 云同步多版本备份(保留5个) + 灵感抽屉 |
| 03:06 | `ad81a49` | notes: 2026-06-08 工作记录 + 吐槽 |

### 说明
- APP_VERSION 升级到 3.3.0
- APK 安装失败(-7) 是签名不一致导致，需先卸载旧版再安装新版
- APK 启动图标（mipmap）在 Android 项目中修改，不在 HTML 中
- 云同步功能需使用修复后的 HTML（排除 Token 字段）重新打包 APK 才能安全使用

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
