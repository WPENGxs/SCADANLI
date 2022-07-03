package com.example.scadanli;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scadanli.databinding.ActivityMainBinding;
import com.example.scadanli.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.iflytek.cloud.SpeechUtility;

import com.iflytek.cloud.Setting;
import com.iflytek.cloud.SpeechUtility;

import java.io.IOException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static String inputText;
    public static Context context;//声明防止无法在DashboardFragment click中调用getApplicationContext()

    public static Data data;
    public static SQLiteDatabase database;

    Handler handler=null;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // 应用程序入口处调用，避免手机内存过小，杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
        // 如在Application中调用初始化，需要在Mainifest中注册该Applicaiton
        // 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
        // 参数间使用半角“,”分隔。
        // 设置你申请的应用appid,请勿在'='与appid之间添加空格及空转义符

        // 注意： appid 必须和下载的SDK保持一致，否则会出现10407错误
        SpeechUtility.createUtility(MainActivity.this, "appid=cf2e2940"+","+"SpeechConstant.FORCE_LOGIN=true");
        // 以下语句用于设置日志开关（默认开启），设置成false时关闭语音云SDK日志打印
        Setting.setShowLog(true);

        Data data=new Data(this,"data",null,1);
        database=data.getWritableDatabase();

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        context=getApplicationContext();//获取现在的context,传给DashboardFragment

        handler=new android.os.Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 0x01://测试通知栏提醒
                        MyNotification.TestNotification(context);
                        break;
                    case 0x02:
                        MyNotification.SendNotification(context,"警告","您有一个预警信息需要处理","","Warning");
                        break;
                    case 0x03:
                        MyNotification.SendNotification(context,"通知","昨天的监控报告已生成,请查收","","Report");
                        break;
                    default:
                        Toast.makeText(context,"推送服务掉线",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        //开启一个监听线程,用来接受请求或信息
        Thread thread = new Thread() {
            @Override
            @SuppressWarnings("InfiniteLoopStatement")
            public void run() {
                try {
                    SCADANLI_Socket socket = new SCADANLI_Socket("1.15.28.84", 39002);
                    while (true) {//死循环读信息
                        Message message = new Message();
                        message.what = 0xff;
                        String str = "" + socket.GetData();
                        switch (str) {
                            case "test":
                                message.what = 0x01;
                                break;
                            case "warning":
                                message.what = 0x02;
                                break;
                            case "report":
                                message.what = 0x03;
                                break;
                        }
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();//开启线程

    }

    /*@Override
    public void onBackPressed(){
        finish();
        System.exit(0);
    }*/



}