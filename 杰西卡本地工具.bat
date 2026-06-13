@echo off
chcp 65001 >nul 2>&1
title 杰西卡日记 - 本地工具

:menu
cls
echo ==========================================
echo   杰西卡日记 - 本地工具
echo ==========================================
echo.
echo   1. 首次克隆仓库（只需运行一次）
echo   2. 拉取最新代码
echo   3. 编辑 HTML（用默认编辑器打开）
echo   4. 提交并推送修改
echo   5. 查看状态
echo   6. 打开 GitHub 仓库页面
echo   0. 退出
echo.
set /p choice=请选择操作:

if "%choice%"=="1" goto clone
if "%choice%"=="2" goto pull
if "%choice%"=="3" goto edit
if "%choice%"=="4" goto push
if "%choice%"=="5" goto status
if "%choice%"=="6" goto open
if "%choice%"=="0" exit
goto menu

:clone
echo.
echo 正在克隆仓库到当前目录...
git clone https://github.com/urlien/jessica-diary.git
if %errorlevel%==0 (
    echo.
    echo ✅ 克隆完成！进入 jessica-diary 目录即可操作。
) else (
    echo.
    echo ❌ 克隆失败，请检查 git 是否已安装。
)
echo.
pause
goto menu

:pull
cd jessica-diary 2>nul
if %errorlevel% neq 0 (
    echo ❌ 找不到 jessica-diary 目录，请先克隆仓库。
    pause
    goto menu
)
echo.
echo 拉取最新代码...
git pull origin main
echo.
pause
goto menu

:edit
cd jessica-diary 2>nul
if %errorlevel% neq 0 (
    echo ❌ 找不到 jessica-diary 目录，请先克隆仓库。
    pause
    goto menu
)
echo.
echo 正在打开 jessica_full.html...
start "" "jessica_full.html"
echo.
echo 编辑完成后，选择"4. 提交并推送修改"同步到 GitHub。
echo.
pause
goto menu

:push
cd jessica-diary 2>nul
if %errorlevel% neq 0 (
    echo ❌ 找不到 jessica-diary 目录，请先克隆仓库。
    pause
    goto menu
)
echo.
echo 当前修改状态:
git status --short
echo.
set /p msg=请输入提交说明（留空则用默认说明）:
if "%msg%"=="" set msg=update: 修改杰西卡 HTML %date% %time:~0,5%
git add jessica_full.html
git commit -m "%msg%"
echo.
echo 推送到 GitHub...
git push origin main
if %errorlevel%==0 (
    echo.
    echo ✅ 推送完成！服务器端会自动拉取最新代码。
) else (
    echo.
    echo ❌ 推送失败，请检查网络或认证。
)
echo.
pause
goto menu

:status
cd jessica-diary 2>nul
if %errorlevel% neq 0 (
    echo ❌ 找不到 jessica-diary 目录，请先克隆仓库。
    pause
    goto menu
)
echo.
git status
echo.
git log --oneline -5
echo.
pause
goto menu

:open
start https://github.com/urlien/jessica-diary
goto menu
