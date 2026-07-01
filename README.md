# 🐰 杰西卡日记

一个基于 WebView 的 Android APP，把日记/聊天/小游戏/知识库打包成一个轻量 APK。

**📦 [最新 Release 下载](https://github.com/urlien/jessica-diary/releases/latest)**

**🌐 [在线预览](https://urlien.github.io/jessica-diary/)**

---

## 功能

- 💬 AI 聊天（上下文记忆）
- 📝 日记生成与编辑
- 🎮 小游戏（围棋 / 2048 / 贪吃蛇 / 俄罗斯方块 / 扫雷）
- 📚 知识库管理（本地 + GitHub 导入）
- ☁️ 云同步备份（GitHub Gist）
- 🔔 原生消息通知
- 🌙 后台运行 + 开机自启
- 🗺️ 地图定位
- 🌆 像素城市动态背景（随时间/天气变化）

## 快速开始

### 安装
去 [Releases](https://github.com/urlien/jessica-diary/releases/latest) 下载 APK，安装到手机。

### 本地开发
```bash
git clone https://github.com/urlien/jessica-diary.git
cd jessica-diary
# 编辑 jessica_full.html
# 推送后自动编译 APK
git add jessica_full.html
git commit -m "你的修改"
git push origin main
```

### 手动打包
```bash
# 需要 JDK 17 + Gradle 8.4 + Android SDK 34
bash build-apk.sh
```

## 项目结构

```
├── jessica_full.html        ← 核心页面（所有功能）
├── android-project/         ← Android WebView 壳
│   └── app/src/main/
│       ├── java/            ← 原生桥接（通知/后台/WakeLock）
│       ├── assets/          ← HTML 打包到这里
│       └── res/             ← 图标、主题
├── build-apk.sh             ← 一键打包脚本
├── CHANGELOG.md             ← 更新日志 + 工作记录
├── inspirations/            ← 灵感档案
├── knowledge-base/          ← 知识库文件
├── 打包指南.txt               ← 完整打包文档
└── 杰西卡本地工具.bat          ← Windows 本地操作工具
```

## 自动化

每次 push 到 `main`：
- ✅ 编译 APK → 创建 Release
- ✅ 部署 HTML → GitHub Pages

## 版本

| 日期 | 版本 | 主要内容 |
|------|------|----------|
| 06-06 | v3.0.0 | WebView 壳 + 原生桥接 + 通知 + 后台 |
| 06-07 | v3.1.0 | 版本号 + 导出 + 表情包 + 人设 + 地图 |
| 06-07 | v3.2.0 | 压缩包导出 + 表情包批量 |
| 06-08 | v3.3.0 | 省电 + 围棋 + 云同步 + 小游戏 + 知识库 |
| 06-13 | v3.3.1 | 文件上传修复 + 知识库网格 + safe-area |
| 06-16 | v3.3.2 | 热更新 + 记忆备份 + 日历修复 + 登录跳过 |
| 06-19 | v3.3.3 | 新卡片 + 删游戏 + 触屏 + 备份版本管理 |
| 07-01 | v3.3.4 | 消息日期 + 天气中文 + BMI身高 + 待办详情 + 文件上传修复 |
