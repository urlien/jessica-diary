# 杰西卡日记

基于 WebView 的 Android APP，核心是一个 HTML 文件打包成 APK。

## 快速打包

```bash
# 前置：编译环境 (JDK 17 + Gradle 8.4 + Android SDK 34)
# 默认路径: /root/.openclaw/workspace/jessica_project/build-env/
bash build-apk.sh
```

## 仓库结构

```
├── jessica_full.html        ← 主页面（主要修改对象）
├── android-project/         ← Android 项目模板（WebView 壳）
│   ├── app/
│   │   ├── build.gradle
│   │   └── src/main/
│   │       ├── AndroidManifest.xml
│   │       ├── assets/index.html  ← 打包时从根目录复制
│   │       ├── java/              ← MainActivity + 通知桥接
│   │       └── res/               ← 图标、主题
│   ├── build.gradle
│   └── settings.gradle
├── build-apk.sh             ← 一键打包脚本
├── CHANGELOG.md             ← 更新日志
├── 打包指南.txt               ← 完整打包文档
├── notes/                   ← 工作记录
├── inspirations/            ← 灵感
└── knowledge-base/          ← 知识库
```

## 编译环境

编译环境需单独下载（611MB），包含 JDK 17 + Gradle 8.4 + Android SDK 34：

```bash
# 从零搭建（推荐，更快）
# JDK 17: 清华镜像 Adoptium
# Gradle 8.4: 腾讯云镜像
# Android SDK 34: Google 官方
```

详见 `打包指南.txt`。
