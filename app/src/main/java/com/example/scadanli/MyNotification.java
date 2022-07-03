package com.example.scadanli;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

/**
 * android 8.0 以后的版本，在创建通知栏的时候，加了一个channelId
 * 为了兼容android所有版本，最好在代码里做一下适配
 */
public final class MyNotification {

    MyNotification(){}

    public static void TestNotification(Context context){
        Toast.makeText(context, "通知测试", Toast.LENGTH_SHORT).show();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 使用NotificationChannel 类构建一个通知渠道
            NotificationChannel notificationChannel = new NotificationChannel("1", "test",
                    NotificationManager.IMPORTANCE_HIGH);
            //通知的一些行为样式配置。
            notificationChannel.enableLights(true); //是否在launcher icon右上角展示提示点
            notificationChannel.setLightColor(Color.RED); //提示点颜色

            notificationManager.createNotificationChannel(notificationChannel);

            Notification notification = new Notification.Builder(context,"1")
                    .setContentTitle("测试标题")//标题
                    .setContentText("这里是测试内容哦")//内容
                    .setSubText("——测试小文字")//内容下面的一小段文字
                    .setTicker("测试")//收到信息后状态栏显示的文字信息
                    .setWhen(System.currentTimeMillis())//设置通知时间
                    .setSmallIcon(R.mipmap.ic_launcher_round)//设置小图标
                    .build();
            notificationManager.notify(1, notification);
        }else {
            Notification notification = new Notification.Builder(context)
                    .setContentTitle("test标题")//标题
                    .setContentText("这里是测试内容哦")//内容
                    .setSubText("——测试小文字")//内容下面的一小段文字
                    .setTicker("测试")//收到信息后状态栏显示的文字信息
                    .setWhen(System.currentTimeMillis())//设置通知时间
                    .setSmallIcon(R.mipmap.ic_launcher_round)//设置小图标
                    .build();
            notificationManager.notify(1, notification);
        }
    }

    public static void SendNotification(Context context,String Title,String ContentText,String SubText,String Ticker){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 使用NotificationChannel 类构建一个通知渠道
            NotificationChannel notificationChannel = new NotificationChannel("2", Title,
                    NotificationManager.IMPORTANCE_HIGH);
            //通知的一些行为样式配置。
            notificationChannel.enableLights(true); //是否在launcher icon右上角展示提示点
            notificationChannel.setLightColor(Color.RED); //提示点颜色

            notificationManager.createNotificationChannel(notificationChannel);

            Notification notification = new Notification.Builder(context,"2")
                    .setContentTitle(Title)//标题
                    .setContentText(ContentText)//内容
                    .setSubText(SubText)//内容下面的一小段文字
                    .setTicker(Ticker)//收到信息后状态栏显示的文字信息
                    .setWhen(System.currentTimeMillis())//设置通知时间
                    .setSmallIcon(R.mipmap.ic_launcher_round)//设置小图标
                    .build();
            notificationManager.notify(1, notification);
        }else {
            Notification notification = new Notification.Builder(context)
                    .setContentTitle(Title)//标题
                    .setContentText(ContentText)//内容
                    .setSubText(SubText)//内容下面的一小段文字
                    .setTicker(Ticker)//收到信息后状态栏显示的文字信息
                    .setWhen(System.currentTimeMillis())//设置通知时间
                    .setSmallIcon(R.mipmap.ic_launcher_round)//设置小图标
                    .build();
            notificationManager.notify(1, notification);
        }
    }
}
