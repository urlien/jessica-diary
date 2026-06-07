# 杰西卡日记

> 你的 AI 日记伙伴 — 单文件 HTML 应用，打包为 Android APK

杰西卡是一个运行在 Android WebView 中的 AI 对话+日记应用。整个前端逻辑打包在一个 HTML 文件里，配合原生壳实现通知、后台运行、开机自启等系统级能力。

## ✨ 功能一览

**核心**
- 💬 AI 对话（流式打字效果、Markdown 渲染、代码高亮）
- 📝 日记生成与管理
- 🔍 全文搜索（MiniSearch + FlexSearch 双引擎）
- 📎 知识库上传（支持 PDF、Word、TXT）

**生活**
- 🌤 天气系统
- 🗺 地图选位置（Leaflet）
- 🎭 心情/状态追踪
- 📅 动态人设（随时间自动切换学业状态）

**娱乐**
- ♟ 围棋（9×9 / 13×13 / 19×19，内置 AI）
- 🎮 2048 / 贪吃蛇 / 俄罗斯方块 / 扫雷
- 💡 灵感抽屉（奇思妙想 / 优秀案例 / 未来计划）

**数据**
- ☁️ 云同步（GitHub Gist，保留最近 5 个版本）
- 📦 导出压缩包（ZIP 格式，移动端走 Share API）
- 💾 本地存储（localStorage + LZ-String 压缩）

**系统**
- 🔔 原生通知桥接（JessicaBridge）
- 🔋 后台省电优化（WakeLock / 定时器暂停）
- 🚀 开机自启

## 📦 当前版本

**v3.3.0** — 详见 [CHANGELOG.md](CHANGELOG.md)

## 🏗 项目结构

```
├── jessica_full.html    # 完整前端（单文件，~1.1MB）
├── 杰西卡v3.2.0.apk     # 预编译 APK
├── build-apk.sh         # 一键打包脚本
├── 打包指南.txt           # 打包说明
├── CHANGELOG.md         # 更新日志
└── notes/               # 工作记录
```

## 🛠 编译 APK

需要编译环境？见 [jessica-build-env](https://github.com/urlien/jessica-build-env)

```bash
bash build-apk.sh
```

输出：`杰西卡v3.2.0.apk`（约 3.6MB）

> ⚠️ APK 签名变更时需先卸载旧版再安装，否则会报 `-7` 错误

## 📱 适配

- Android WebView 全屏沉浸模式
- `env(safe-area-inset-*)` 安全区域适配
- 禁用弹性滚动 / 下拉刷新
- viewport 锁定缩放

## 相关仓库

| 仓库 | 用途 |
|------|------|
| [jessica-diary](https://github.com/urlien/jessica-diary) | HTML 源码 + APK（本仓库） |
| [jessica-build-env](https://github.com/urlien/jessica-build-env) | 编译环境（JDK17 + Gradle 8.4 + Android SDK 34） |
