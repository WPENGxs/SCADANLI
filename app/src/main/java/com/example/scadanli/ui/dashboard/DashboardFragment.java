package com.example.scadanli.ui.dashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
//import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.scadanli.CirCleProgressBar;
import com.example.scadanli.MainActivity;
import com.example.scadanli.R;
import com.example.scadanli.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {

    public DashboardViewModel dashboardViewModel;
    public FragmentDashboardBinding binding;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CirCleProgressBar cir_bar_hum=binding.humidityCircle;//湿度进度条
        CirCleProgressBar cir_bar_water=binding.waterVapourSaturationCircle;//水汽饱和度进度条
        CirCleProgressBar cir_br_oxygen=binding.oxygenCircle;//氧气含量

        CirCleProgressBar cir_bar_soil=binding.soilCircle;//土壤湿度
        CirCleProgressBar cir_bar_carbon=binding.carbonCircle;//二氧化碳浓度

        cir_bar_hum.setText(true,"50%");
        cir_bar_hum.setCurrentProgress(50f);//设置进度条进度
        cir_bar_hum.setCircleColor(Color.parseColor("#5f7099"));

        cir_bar_water.setText(true,"50%");
        cir_bar_water.setCurrentProgress(50f);
        cir_bar_water.setCircleColor(Color.parseColor("#d21e6d"));

        cir_br_oxygen.setText(true,"20%");
        cir_br_oxygen.setCurrentProgress(20f);
        cir_br_oxygen.setCircleColor(Color.parseColor("#d21e6d"));

        cir_bar_soil.setText(true,"30%");
        cir_bar_soil.setCurrentProgress(30f);

        cir_bar_carbon.setText(true,"10%");
        cir_bar_carbon.setCurrentProgress(10f );

        TextView text_tem=binding.temperature;//温度
        text_tem.setText("32");

        AddLine(binding.infobox,getContext());
        AddLine(binding.infobox,getContext());

        return root;
    }

    public void AddLine(LinearLayout infobox, Context context){
        HorizontalScrollView horizontalScrollView=new HorizontalScrollView(context);
        horizontalScrollView.setHorizontalScrollBarEnabled(false);
        LinearLayout line=new LinearLayout(context);
        line.setGravity(View.TEXT_ALIGNMENT_CENTER);
        horizontalScrollView.addView(line);
        infobox.addView(horizontalScrollView);

        LayoutInflater inflater=LayoutInflater.from(context);
        LinearLayout cir_bar=inflater.inflate(R.layout.cir_bar,null).findViewById(R.id.cir_bar);
        line.addView(cir_bar);
        LinearLayout display_bar=inflater.inflate(R.layout.display_bar,null).findViewById(R.id.display_bar);
        line.addView(display_bar);
        LinearLayout control_bar=inflater.inflate(R.layout.control_bar,null).findViewById(R.id.control_bar);
        line.addView(control_bar);

    }

    public void AddCirBar(LinearLayout linearLayout, Context context){
        LinearLayout box=new LinearLayout(context);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setBackgroundResource(R.drawable.round);

        CirCleProgressBar cirCleProgressBar=new CirCleProgressBar(context);
        cirCleProgressBar.setMinimumWidth(200);
        cirCleProgressBar.setMinimumHeight(200);
        cirCleProgressBar.setCircleBgWidth(10);
        cirCleProgressBar.setCircleWidth(10);
        cirCleProgressBar.setTextSize(20f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            cirCleProgressBar.setForegroundGravity(View.TEXT_ALIGNMENT_CENTER);
        }

        TextView textView=new TextView(context);
        textView.setWidth(50);
        textView.setHeight(100);
        textView.setPadding(10,0,0,0);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(20);
        textView.setText("0000");

        box.addView(cirCleProgressBar);
        box.addView(textView);

        linearLayout.addView(box);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}