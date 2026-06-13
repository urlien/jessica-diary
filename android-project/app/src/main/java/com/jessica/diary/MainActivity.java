package com.jessica.diary;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebResourceRequest;
import android.webkit.ValueCallback;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {

    private WebView webView;
    private PowerManager.WakeLock wakeLock;
    private ValueCallback<Uri[]> fileUploadCallback;
    private static final int FILE_CHOOSER_REQUEST = 200;
    private static final String CHANNEL_ID = "jessica_messages";
    private static final String CHANNEL_BG = "jessica_background";
    private static final int NOTIF_ID_MSG = 1001;
    private static final int NOTIF_ID_SERVICE = 2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 全屏沉浸模式 — 内容延伸到状态栏和导航栏后面
        getWindow().getDecorView().setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        );
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        // 创建通知渠道
        createNotificationChannels();

        // 请求通知权限 (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS")
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 100);
            }
        }

        // 初始化 WebView
        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setDatabaseEnabled(true);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        // 移动端适配：禁止缩放，让 HTML 自己控制布局
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(false);
        settings.setSupportZoom(false);
        settings.setDisplayZoomControls(false);

        // 注入 JavaScript 接口（用于原生通知）
        webView.addJavascriptInterface(new JessicaBridge(), "JessicaBridge");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

        // WebChromeClient：处理权限请求（含通知）和文件上传
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                runOnUiThread(() -> request.grant(request.getResources()));
            }

            @Override
            public boolean onShowFileChooser(WebView webView,
                    ValueCallback<Uri[]> filePathCallback,
                    FileChooserParams fileChooserParams) {
                if (fileUploadCallback != null) {
                    fileUploadCallback.onReceiveValue(null);
                }
                fileUploadCallback = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try {
                    startActivityForResult(intent, FILE_CHOOSER_REQUEST);
                } catch (Exception e) {
                    fileUploadCallback = null;
                    return false;
                }
                return true;
            }
        });

        // 获取 WakeLock（防止 CPU 休眠）
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "jessica:keepalive");
        wakeLock.acquire();

        // 加载 HTML
        webView.loadUrl("file:///android_asset/index.html");
    }

    /**
     * 创建通知渠道
     */
    private void createNotificationChannels() {
        // 消息通知渠道
        NotificationChannel msgChannel = new NotificationChannel(
            CHANNEL_ID,
            "杰西卡的消息",
            NotificationManager.IMPORTANCE_HIGH
        );
        msgChannel.setDescription("杰西卡主动发来的消息通知");
        msgChannel.enableVibration(true);
        msgChannel.setVibrationPattern(new long[]{0, 200, 100, 200});

        // 后台运行通知渠道
        NotificationChannel bgChannel = new NotificationChannel(
            CHANNEL_BG,
            "后台运行",
            NotificationManager.IMPORTANCE_LOW
        );
        bgChannel.setDescription("保持杰西卡日记在后台运行");

        NotificationManager nm = getSystemService(NotificationManager.class);
        nm.createNotificationChannel(msgChannel);
        nm.createNotificationChannel(bgChannel);
    }

    /**
     * JavaScript 接口：供 WebView 内的 JS 调用原生功能
     */
    public class JessicaBridge {

        @JavascriptInterface
        public void sendNotification(String title, String message) {
            showNotification(title, message);
        }

        @JavascriptInterface
        public void startBackgroundService() {
            startKeepAliveService();
        }

        @JavascriptInterface
        public void stopBackgroundService() {
            stopKeepAliveService();
        }

        @JavascriptInterface
        public void acquireWakeLock() {
            if (wakeLock != null && !wakeLock.isHeld()) {
                wakeLock.acquire();
            }
        }

        @JavascriptInterface
        public void releaseWakeLock() {
            if (wakeLock != null && wakeLock.isHeld()) {
                wakeLock.release();
            }
        }

        @JavascriptInterface
        public boolean isNotificationPermissionGranted() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                return ContextCompat.checkSelfPermission(MainActivity.this,
                    "android.permission.POST_NOTIFICATIONS") == PackageManager.PERMISSION_GRANTED;
            }
            return true; // Android 12 及以下不需要运行时权限
        }
    }

    /**
     * 显示系统级通知
     */
    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setAutoCancel(true)
            .setContentIntent(pi)
            .setDefaults(NotificationCompat.DEFAULT_ALL);

        NotificationManagerCompat nm = NotificationManagerCompat.from(this);
        try {
            nm.notify(NOTIF_ID_MSG, builder.build());
        } catch (SecurityException e) {
            // 无通知权限，忽略
        }
    }

    /**
     * 启动前台保活服务
     */
    private void startKeepAliveService() {
        Intent intent = new Intent(this, KeepAliveService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }

    private void stopKeepAliveService() {
        Intent intent = new Intent(this, KeepAliveService.class);
        stopService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_CHOOSER_REQUEST) {
            if (fileUploadCallback != null) {
                Uri[] results = null;
                if (resultCode == RESULT_OK && data != null) {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
                fileUploadCallback.onReceiveValue(results);
                fileUploadCallback = null;
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
        // 重新获取 WakeLock
        if (wakeLock != null && !wakeLock.isHeld()) {
            wakeLock.acquire();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
        }
        super.onDestroy();
    }
}
