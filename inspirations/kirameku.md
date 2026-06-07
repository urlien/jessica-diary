# 🌟 优秀案例：Kirameku

> 仓库：https://github.com/Xinghongia/Kirameku
> 日期：2026-06-08
> 状态：🌟 优秀案例参考

## 项目简介

一个基于 Next.js 16 + FastAPI 构建的高颜值前后端分离个人博客。毛玻璃设计、暗色模式、说说/杂谈/评论区、GitHub OAuth 登录、RSS 订阅、Markdown 写作、独立后台管理面板。

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
- Vue 3 + Element Plus
- 内嵌于后端，无需单独部署

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

### 可借鉴的
- 🎵 音乐播放器（/music）— 歌单管理、播放器 UI
- 🎮 小游戏（18 个）— 已经参考加了 4 个
- 📸 照片墙 — 瀑布流展示
- 📖 小说阅读系统 — 书架→目录→阅读的完整流程
- 🎨 毛玻璃 + 暗色设计 — UI 风格参考

### 不适合直接搬的
- 全栈架构（Next.js + FastAPI + PostgreSQL）太重
- 需要服务器部署，不适合"单文件 HTML"的定位
- Live2D 看板娘体积大

### 启发
杰西卡可以做"轻量版"：把 Kirameku 的功能用纯前端实现，数据存 localStorage，不需要后端。牺牲跨设备同步，换来零部署成本。
