# 🌟 Kirameku

> 在线体验：https://boke.hiromu.top
> GitHub：https://github.com/Xinghongia/Kirameku
> 日期：2026-06-08

## 项目简介

Next.js 16 + FastAPI 构建的高颜值前后端分离个人博客。毛玻璃设计、暗色模式、说说/杂谈/评论区、GitHub OAuth 登录、RSS 订阅、Markdown 写作、独立后台管理面板。

## 技术栈

**前端**
- Next.js 16 + React 19（App Router，SSR/SSG）
- Tailwind CSS 4
- Framer Motion（页面过渡与微交互）
- TypeScript
- Live2D 看板娘

**后端**
- FastAPI + SQLModel + PostgreSQL
- 阿里云 OSS（图片存储）
- JWT 认证

**管理后台**
- Vue 3 + Element Plus（内嵌于后端）

## 功能模块

| 模块 | 路径 | 描述 |
|------|------|------|
| 首页 | / | 文章预览、说说、照片墙 |
| 文章 | /posts | 分类、标签、Markdown 渲染 |
| 说说 | /moments | 碎片化记录，类朋友圈 |
| 杂谈 | /messages | 轻量话题讨论 |
| 小说 | /novel | 书架→搜索→目录→阅读 |
| 收藏夹 | /bookmark | 站点导航，自动 favicon |
| 项目 | /projects | 个人项目展示 |
| 友链 | /friends | 漂流瓶主题，可拖动 |
| 照片墙 | /photowall | 瀑布流展示 |
| 归档 | /timeline | 时间河流可视化 |
| **音乐** | /music | **云音乐播放器，支持歌单** |
| 关于 | /about | 关于博主 |

## 对杰西卡的参考价值

- 🎵 音乐播放器 — 歌单管理、播放器 UI（已有灵感存档）
- 🎮 小游戏 — 已参考加了 4 个
- 📸 照片墙 — 瀑布流展示
- 📖 小说阅读 — 书架→目录→阅读流程
- 🎨 毛玻璃 + 暗色设计 — UI 风格参考
- ⚠️ 全栈太重，不适合直接搬，但功能思路可以做"轻量纯前端版"
