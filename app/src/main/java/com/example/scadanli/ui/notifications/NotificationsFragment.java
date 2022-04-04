package com.example.scadanli.ui.notifications;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.scadanli.MainActivity;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class NotificationsFragment extends Fragment {

    public NotificationsViewModel notificationsViewModel;
    public com.example.scadanli.databinding.FragmentNotificationsBinding binding;

    @SuppressLint("SetJavaScriptEnabled")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = com.example.scadanli.databinding.FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        WebView webView=binding.web;
        webView.setClickable(true);//设置可点击
        webView.getSettings().setJavaScriptEnabled(true);//支持JS
        webView.getSettings().setSupportZoom(true);//设置可以支持缩放
        webView.getSettings().setBuiltInZoomControls(true);//设置出现缩放工具
        webView.getSettings().setDomStorageEnabled(true);//设置为使用webView推荐的窗口，主要是为了配合下一个属性
        webView.getSettings().setUseWideViewPort(true);//扩大缩放比例
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        webView.loadUrl("https://sh.coda.wiki:5002/2000");

        Button sheds_btu=binding.shedsButton;
        Button greenhouses_btu=binding.greenhousesButton;
        Button field_btu=binding.fieldsButton;

/*        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

        });*/

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    Log.d("加载完成...","success");
                } else {
                    // 加载中
                    Log.d("加载中...",+newProgress+"");
                }
            }
            @Override
            public void onPermissionRequest(final PermissionRequest request) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    request.grant(request.getResources());
                }

//                getActivity().runOnUiThread(new Runnable() {
//                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//                    @Override
//                    public void run() {
//                        request.grant(request.getResources());
//                    }
//                });
            }
        });

        sheds_btu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { webView.loadUrl("https://sh.coda.wiki:5002/2000"); }
        });

        greenhouses_btu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("https://sh.coda.wiki:5002/2001");
            }
        });

        field_btu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.loadUrl("https://sh.coda.wiki:5002/2002");
            }
        });
        
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}