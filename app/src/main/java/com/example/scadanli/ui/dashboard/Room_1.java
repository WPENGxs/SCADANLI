package com.example.scadanli.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scadanli.CirCleProgressBar;
import com.example.scadanli.R;

public class Room_1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room1);

        CirCleProgressBar cir_bar_01=findViewById(R.id.cir_progressbar_01);//进度条1
        CirCleProgressBar cir_bar_02=findViewById(R.id.cir_progressbar_02);//进度条2
        CirCleProgressBar cir_bar_03=findViewById(R.id.cir_progressbar_03);//进度条3

        cir_bar_01.setText(true,"30%");
        cir_bar_02.setText(true,"60%");
        cir_bar_03.setText(true,"40%");

        cir_bar_01.setCurrentProgress(30f);//设置进度条进度
        cir_bar_02.setCurrentProgress(60f);
        cir_bar_03.setCurrentProgress(40f);


    }
    public void check_button(){//开关检查函数，在创建Activity时检查开关的开闭来显示开关的颜色状态
        /*
         在此检查开关的状态
        */
    }
    public void button_color(Button btu){//在开关点击后设置开关颜色
        btu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btu.getCurrentTextColor()==Color.RED){
                    btu.setTextColor(Color.GREEN);
                    Toast.makeText(getApplicationContext(),"正在打开...",Toast.LENGTH_SHORT).show();
                }else{
                    btu.setTextColor(Color.RED);
                    Toast.makeText(getApplicationContext(),"正在关闭...",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}