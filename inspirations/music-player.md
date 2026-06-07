# 🎵 音乐播放器 — 恶魔电台

> 来源：用户上传的参考代码
> 日期：2026-06-08
> 状态：💡 灵感存档，待实现

## 概述

一个纯 CSS + JS 的浮动 BGM 面板，固定在屏幕左侧，点击展开。整体风格是暗黑 + 橙红（#ff4500），有唱片旋转动画。

## 功能

- 🎵 唱片封面旋转（CSS @keyframes csmSpin，播放时转，暂停时停）
- ▶️ 播放/暂停
- 🔀 三种模式：顺序播放 / 随机播放 / 单曲循环
- 📊 进度条 + 时间显示（可拖动跳转）
- 🔊 音量滑块
- 🎨 封面随当前歌曲切换

## 技术实现

- **纯前端**：无依赖，一个 div + 内联 CSS + img onerror 初始化 hack
- **Audio API**：new Audio()，直接设 src 为 MP3 直链
- **UI**：position: fixed，左侧浮动，transform: translateX(-105%) 滑入滑出
- **唱片动画**：CSS animation: csmSpin 4s linear infinite，animation-play-state 控制

## 歌单格式

```html
<option value="https://xxx.com/song.mp3" data-cover="https://xxx.com/cover.jpg">🎵 歌手 - 歌名</option>
```

每首歌需要：**MP3 直链** + **封面图链接**

## ⚠️ 难点：音乐源

这是最大的问题。参考代码用的是 meimoaiimg.com 的直链 MP3，类似图床服务。

### 可能的方案

| 方案 | 优点 | 缺点 |
|------|------|------|
| 自建音乐图床 | 完全控制 | 需要服务器 + 存储 + 带宽 |
| 第三方直链站 | 免费 | 可能随时失效 |
| 网易云/QQ音乐外链 | 歌曲多 | 反爬严格，链接经常失效 |
| 用户本地上传 | 无版权问题 | 不能跨设备同步 |
| GitHub + jsDelivr | 免费托管 | 文件大小限制，不适合大文件 |
| Cloudflare R2 | 免费 10GB | 需要配置 |

### Kirameku 的做法

Kirameku 有 /music 页面，但它是 Next.js + FastAPI 全栈方案，后端有数据库存歌单，前端通过 API 拉取。对杰西卡来说太重了。

### 我的想法

最轻量的方案：**用户自己上传 MP3 → 存到 localStorage（压缩后）或 IndexedDB**，纯本地播放。缺点是不能跨设备，但至少不用折腾服务器。

或者：**GitHub 仓库 + raw 链接**，把 MP3 文件扔到一个公开仓库里，用 raw.githubusercontent.com 做直链。问题是单文件 25MB 限制，而且 GitHub 可能会限速。

## 待解决

- [ ] 确定音乐源方案
- [ ] UI 风格适配杰西卡（暗黑恶魔风 vs 杰西卡现有风格？）
- [ ] 是否支持用户自定义歌单
- [ ] 是否需要歌词显示
