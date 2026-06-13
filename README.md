# 🐰 杰西卡日记

一个基于 WebView 的 Android APP，把完整的日记/聊天/小游戏功能打包成一个轻量 APK。

**📦 [最新 Release 下载](https://github.com/urlien/jessica-diary/releases/latest)**

**🌐 [在线预览 (GitHub Pages)](https://urlien.github.io/jessica-diary/)**

---

## 功能一览

- 💬 AI 聊天对话（支持上下文记忆）
- 📝 日记生成与编辑
- 🎮 小游戏（围棋 / 2048 / 贪吃蛇 / 俄罗斯方块 / 扫雷）
- 📚 知识库管理
- ☁️ 云同步备份（GitHub Gist）
- 🔔 原生消息通知
- 🌙 后台运行 + 开机自启
- 🗺️ 地图定位

## 快速开始

### 安装 APK
1. 去 [Releases](https://github.com/urlien/jessica-diary/releases/latest) 下载最新 APK
2. 安装到手机（需允许未知来源）

### 本地开发
```bash
# 克隆仓库
git clone https://github.com/urlien/jessica-diary.git
cd jessica-diary

# 编辑 HTML（核心文件）
# 用任意编辑器打开 jessica_full.html

# 推送后 GitHub Actions 自动编译 APK
git add jessica_full.html
git commit -m "你的修改说明"
git push origin main
```

### 手动打包（需要编译环境）
```bash
# 编译环境：JDK 17 + Gradle 8.4 + Android SDK 34
bash build-apk.sh
```

## 项目结构

```
├── jessica_full.html        ← 核心页面（所有功能都在这里）
├── android-project/         ← Android WebView 壳
│   └── app/src/main/
│       ├── java/            ← 原生桥接（通知/后台/WakeLock）
│       ├── assets/          ← HTML 打包到这里
│       └── res/             ← 图标、主题
├── build-apk.sh             ← 一键打包脚本
├── CHANGELOG.md             ← 更新日志
└── 打包指南.txt               ← 完整打包文档
```

## 自动化

每次 push 到 `main` 分支，GitHub Actions 自动：
- ✅ 编译 APK → 上传到 Release
- ✅ 部署 HTML → GitHub Pages 在线预览

## 许可

私人项目，仅供个人使用。
