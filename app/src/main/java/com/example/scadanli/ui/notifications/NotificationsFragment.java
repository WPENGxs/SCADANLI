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
import com.example.scadanli.R;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class NotificationsFragment extends Fragment {

    public NotificationsViewModel notificationsViewModel;
    public com.example.scadanli.databinding.FragmentNotificationsBinding binding;

    @SuppressLint({"SetJavaScriptEnabled", "UseCompatLoadingForDrawables"})
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = com.example.scadanli.databinding.FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Button sheds_btu=binding.shedsButton;
        Button greenhouses_btu=binding.greenhousesButton;
        Button field_btu=binding.fieldsButton;

        WebView webView=binding.web;
        webView.setClickable(true);//???????????????
        webView.getSettings().setJavaScriptEnabled(true);//??????JS
        webView.getSettings().setSupportZoom(true);//????????????????????????
        webView.getSettings().setBuiltInZoomControls(true);//????????????????????????
        webView.getSettings().setDomStorageEnabled(true);//???????????????webView??????????????????????????????????????????????????????
        webView.getSettings().setUseWideViewPort(true);//??????????????????
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        sheds_btu.setBackground(getResources().getDrawable(R.drawable.round_select));
        greenhouses_btu.setBackground(getResources().getDrawable(R.drawable.round));
        field_btu.setBackground(getResources().getDrawable(R.drawable.round));
        webView.loadUrl("https://sh.coda.wiki:5002/2000");



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
                    // ??????????????????
                    Log.d("????????????...","success");
                } else {
                    // ?????????
                    Log.d("?????????...",+newProgress+"");
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
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                sheds_btu.setBackground(getResources().getDrawable(R.drawable.round_select));
                greenhouses_btu.setBackground(getResources().getDrawable(R.drawable.round));
                field_btu.setBackground(getResources().getDrawable(R.drawable.round));
                webView.loadUrl("https://sh.coda.wiki:5002/2000");
            }
        });

        greenhouses_btu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                sheds_btu.setBackground(getResources().getDrawable(R.drawable.round));
                greenhouses_btu.setBackground(getResources().getDrawable(R.drawable.round_select));
                field_btu.setBackground(getResources().getDrawable(R.drawable.round));
                webView.loadUrl("https://sh.coda.wiki:5002/2001");
            }
        });

        field_btu.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                sheds_btu.setBackground(getResources().getDrawable(R.drawable.round));
                greenhouses_btu.setBackground(getResources().getDrawable(R.drawable.round));
                field_btu.setBackground(getResources().getDrawable(R.drawable.round_select));
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