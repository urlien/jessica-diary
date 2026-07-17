# 杰西卡日记 v3.4 设计框架

## 设计定位

**个人日记 × 现代科技感** — 保留杰西卡的温暖私密感，注入视频参考中的渐变紫+毛玻璃+大卡片语言。

**一句话场景：** 深夜，一个人躺在床上，手机屏幕微光，和一个温柔的 AI 聊天，写下今天的日记。

## 设计原则

1. **温暖但不幼稚** — 深色基底 + 柔和渐变，不是纯黑科技风
2. **信息分层清晰** — 大卡片承载核心信息，小元素做点缀
3. **毛玻璃做层次** — 半透明卡片叠加背景，不是死板的纯色块
4. **数据可视化** — 大数字+渐变色，一眼看到关键信息
5. **动效克制** — 只在关键交互处用动效，不晃眼

## 色彩系统

### 深色主题（默认）
```
--bg:           #0a0b10        深色基底（比现在更深一点）
--bg-card:      rgba(16,18,28,0.85)    卡片底色
--bg-card-glass: rgba(255,255,255,0.04)  毛玻璃层
--border:       rgba(255,255,255,0.06)  微光边框
--border-glow:  rgba(139,92,246,0.15)   紫光边框

--text:         #e8e8f0        主文字（更亮）
--text-dim:     #6b6d7a        次要文字（更暗）

--accent:       #8b5cf6        主强调色（渐变紫）
--accent-light: #a78bfa        浅紫
--accent2:      #f59e0b        辅助强调色（琥珀金）
--accent2-light:#fbbf24        浅金

--gradient-primary: linear-gradient(135deg, #8b5cf6, #6366f1)    主渐变
--gradient-warm:    linear-gradient(135deg, #f59e0b, #ef4444)    暖渐变
--gradient-glow:    linear-gradient(135deg, rgba(139,92,246,0.2), rgba(99,102,241,0.1))  光晕渐变

--red:          #ef4444
--green:        #22c55e
--pink:         #ec4899
--orange:       #f59e0b
```

### 浅色主题（保留，微调）
```
--bg:           #f8f9fc
--bg-card:      rgba(255,255,255,0.9)
--accent:       #7c3aed
--accent2:      #d97706
```

## 组件重设计

### 1. 首页卡片
**现在：** 纯色半透明方块
**改为：** 毛玻璃卡片 + 微光边框 + 悬浮光晕

```css
.hp-card {
  background: rgba(255,255,255,0.03);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255,255,255,0.06);
  border-radius: 16px;
  transition: all 0.3s ease;
}
.hp-card:hover {
  border-color: rgba(139,92,246,0.2);
  box-shadow: 0 8px 32px rgba(139,92,246,0.08);
  transform: translateY(-2px);
}
```

### 2. 数据展示（天气、番茄钟、天数）
**现在：** 普通数字
**改为：** 大数字 + 渐变色 + 微光

```css
.hp-card-value {
  font-size: 32px;
  font-weight: 700;
  background: var(--gradient-primary);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}
```

### 3. 聊天气泡
**现在：** 纯色方块
**改为：** 毛玻璃 + 侧边渐变条

```css
.message.jessica .msg-bubble {
  background: rgba(139,92,246,0.06);
  border-left: 2px solid rgba(139,92,246,0.3);
  backdrop-filter: blur(8px);
}
.message.henderson .msg-bubble {
  background: rgba(99,102,241,0.08);
  border-right: 2px solid rgba(99,102,241,0.3);
}
```

### 4. 侧边栏日历
**现在：** 普通网格
**改为：** 紫光标记今天 + 日记日期发光点

```css
.day-cell.today {
  background: var(--gradient-primary);
  box-shadow: 0 0 12px rgba(139,92,246,0.3);
}
.day-cell.has-jp::after {
  content: '';
  position: absolute;
  bottom: 4px;
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background: var(--accent2);
  box-shadow: 0 0 6px rgba(245,158,11,0.4);
}
```

### 5. 输入区域
**现在：** 普通输入框
**改为：** 毛玻璃输入栏 + 发送按钮渐变

```css
.input-wrapper {
  background: rgba(255,255,255,0.04);
  border: 1px solid rgba(255,255,255,0.08);
  border-radius: 16px;
  backdrop-filter: blur(8px);
}
.send-btn {
  background: var(--gradient-primary);
  border-radius: 12px;
}
```

### 6. 模型切换器
**现在：** 已有基础样式
**改为：** 紫光胶囊 + 下拉毛玻璃

```css
.model-switcher-btn {
  background: rgba(139,92,246,0.1);
  border: 1px solid rgba(139,92,246,0.2);
  border-radius: 20px;
}
```

## 布局约束（不变）

- 首页布局：保持现有卡片网格
- 聊天界面：保持现有三段式（header + chat area + input）
- 侧边栏：保持现有左滑模式
- 背景图：保持用户自定义，不强制覆盖

## 动效规范

- 卡片悬浮：`transform: translateY(-2px)` + `box-shadow` 扩散，200ms ease
- 页面切换：fade + 轻微 scale，300ms
- 日记生成中：呼吸光效（紫光脉冲）
- 打字指示器：三点跳动，保留

## 字体

- 保持系统字体栈不变
- 数据数字可用 `font-variant-numeric: tabular-nums` 保证对齐
