package com.jessica.diary;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

/**
 * 前台服务：保持 APP 在后台持续运行
 * 杰西卡可以随时给你发消息和通知
 */
public class KeepAliveService extends Service {

    private static final String CHANNEL_ID = "jessica_background";
    private static final int NOTIF_ID = 2001;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID, "后台运行", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("保持杰西卡日记在后台运行");
            getSystemService(NotificationManager.class).createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
            notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("杰西卡在线")
            .setContentText("杰西卡正在等你回来 💬")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build();

        startForeground(NOTIF_ID, notification);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
