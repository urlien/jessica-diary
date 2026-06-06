#!/bin/bash
# 杰西卡 APP 一键打包脚本
# 用法: bash build-apk.sh
# 功能: 编译 APK + 自动同步 HTML 到 GitHub

set -e

PROJECT="/root/.openclaw/workspace/jessica_project"
ENV="$PROJECT/build-env"
HTML="$PROJECT/jessica_full.html"

# 从 build.gradle 读取版本号
VERSION=$(grep 'versionName' "$ENV/jessica-diary/app/build.gradle" | sed 's/.*"\(.*\)".*/\1/')
APK_OUT="$PROJECT/杰西卡v${VERSION}.apk"

echo "=========================================="
echo "  杰西卡 APP v${VERSION} — 一键打包"
echo "=========================================="

# 检查编译环境
if [ ! -d "$ENV/jdk17" ] || [ ! -d "$ENV/gradle-8.4" ] || [ ! -d "$ENV/android-sdk" ]; then
    echo "❌ 编译环境不存在，请先下载 build-env"
    echo "   详见打包指南.txt 第四节"
    exit 1
fi

# 检查 HTML 文件
if [ ! -f "$HTML" ]; then
    echo "❌ 找不到 jessica_full.html"
    exit 1
fi

echo "📋 复制 HTML..."
cp "$HTML" "$ENV/jessica-diary/app/src/main/assets/index.html"

# 确保 APP 名称为"杰西卡"（而不是"杰西卡日记"）
STRINGS_XML="$ENV/jessica-diary/app/src/main/res/values/strings.xml"
if [ -f "$STRINGS_XML" ]; then
    if grep -q '>杰西卡日记<' "$STRINGS_XML"; then
        echo "📛 修改 APP 名称: 杰西卡日记 → 杰西卡"
        sed -i 's/>杰西卡日记</>杰西卡</' "$STRINGS_XML"
    fi
fi

echo "🔧 设置环境变量..."
export JAVA_HOME="$ENV/jdk17"
export PATH="$JAVA_HOME/bin:$PATH"
export ANDROID_SDK_ROOT="$ENV/android-sdk"

echo "🔨 编译中..."
cd "$ENV/jessica-diary"
"$ENV/gradle-8.4/bin/gradle" assembleDebug --no-daemon --quiet

echo "📦 复制 APK..."
cp app/build/outputs/apk/debug/app-debug.apk "$APK_OUT"

echo "✅ APK 编译完成: $APK_OUT"
echo "   版本: v${VERSION}"

# 自动同步到 GitHub
echo ""
echo "📤 同步到 GitHub..."
cd "$PROJECT"
if [ -d ".git" ]; then
    git add jessica_full.html CHANGELOG.md
    git commit -m "build: v${VERSION} $(date '+%Y-%m-%d %H:%M')" 2>/dev/null || true
    git push origin main 2>/dev/null && echo "✅ 已推送到 GitHub" || echo "⚠️ 推送失败（可能需要认证）"
else
    echo "⚠️ 未初始化 Git 仓库，跳过同步"
fi

echo ""
echo "=========================================="
echo "  完成！"
echo "=========================================="
