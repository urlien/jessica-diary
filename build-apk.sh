#!/bin/bash
# 杰西卡 APP 一键打包脚本
# 用法: bash build-apk.sh
# 功能: 编译 APK + 自动同步到 GitHub
#
# 前置条件：需要 JDK 17 + Gradle 8.4 + Android SDK 34
# 安装路径默认: /root/.openclaw/workspace/jessica_project/build-env/
# 可通过环境变量 BUILD_ENV 覆盖

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
PROJECT="$SCRIPT_DIR"
BUILD_ENV="${BUILD_ENV:-/root/.openclaw/workspace/jessica_project/build-env}"
ANDROID_PROJECT="$PROJECT/android-project"
HTML="$PROJECT/jessica_full.html"

# 从 build.gradle 读取版本号
VERSION=$(grep 'versionName' "$ANDROID_PROJECT/app/build.gradle" | sed 's/.*"\(.*\)".*/\1/')
APK_OUT="$PROJECT/杰西卡v${VERSION}.apk"

echo "=========================================="
echo "  杰西卡 APP v${VERSION} — 一键打包"
echo "=========================================="

# 检查编译环境
if [ ! -d "$BUILD_ENV/jdk17" ] || [ ! -d "$BUILD_ENV/gradle-8.4" ] || [ ! -d "$BUILD_ENV/android-sdk" ]; then
    echo "❌ 编译环境不存在: $BUILD_ENV"
    echo "   请先下载编译环境或设置 BUILD_ENV 环境变量"
    exit 1
fi

# 检查 HTML 文件
if [ ! -f "$HTML" ]; then
    echo "❌ 找不到 jessica_full.html"
    exit 1
fi

echo "📋 复制 HTML..."
cp "$HTML" "$ANDROID_PROJECT/app/src/main/assets/index.html"

# 确保 APP 名称为"杰西卡"
STRINGS_XML="$ANDROID_PROJECT/app/src/main/res/values/strings.xml"
if [ -f "$STRINGS_XML" ]; then
    if grep -q '>杰西卡日记<' "$STRINGS_XML"; then
        echo "📛 修改 APP 名称: 杰西卡日记 → 杰西卡"
        sed -i 's/>杰西卡日记</>杰西卡</' "$STRINGS_XML"
    fi
fi

echo "🔧 设置环境变量..."
export JAVA_HOME="$BUILD_ENV/jdk17"
export PATH="$JAVA_HOME/bin:$PATH"
export ANDROID_SDK_ROOT="$BUILD_ENV/android-sdk"

echo "🔨 编译中..."
cd "$ANDROID_PROJECT"
"$BUILD_ENV/gradle-8.4/bin/gradle" assembleDebug --no-daemon --quiet

echo "📦 复制 APK..."
cp app/build/outputs/apk/debug/app-debug.apk "$APK_OUT"

echo "✅ APK 编译完成: $APK_OUT"
echo "   版本: v${VERSION}"

# 自动同步到 GitHub
echo ""
echo "📤 同步到 GitHub..."
cd "$PROJECT"
if [ -d ".git" ]; then
    git add jessica_full.html CHANGELOG.md android-project/
    git commit -m "build: v${VERSION} $(date '+%Y-%m-%d %H:%M')" 2>/dev/null || true
    git push origin main 2>/dev/null && echo "✅ 已推送到 GitHub" || echo "⚠️ 推送失败（可能需要认证）"
else
    echo "⚠️ 未初始化 Git 仓库，跳过同步"
fi

echo ""
echo "=========================================="
echo "  完成！"
echo "=========================================="
