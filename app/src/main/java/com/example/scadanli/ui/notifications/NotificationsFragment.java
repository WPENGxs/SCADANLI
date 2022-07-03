package com.example.scadanli.ui.notifications;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.example.scadanli.MainActivity;
import com.example.scadanli.R;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import static android.content.Context.MODE_PRIVATE;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class NotificationsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener
        , AMapLocationListener, LocationSource {

    public NotificationsViewModel notificationsViewModel;
    public com.example.scadanli.databinding.FragmentNotificationsBinding binding;

    private MapView mapView;
    private AMap aMap;
    //定位服务类。此类提供单次定位、持续定位、地理围栏、最后位置相关功能
    private AMapLocationClient aMapLocationClient;
    private OnLocationChangedListener listener;
    //定位参数设置
    private AMapLocationClientOption aMapLocationClientOption;
    private String MyCity="";
    TextView WeatherText;

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
        WeatherText=binding.weatherText;

        /*确保调用SDK任何接口前先调用更新隐私合规updatePrivacyShow、updatePrivacyAgree两个接口并且参数值都为true，若未正确设置有崩溃风险***
         使用sea SDK 功能前请设置隐私权政策是否弹窗告知用户*/
        AMapLocationClient.updatePrivacyShow(getContext(), true, true);
        AMapLocationClient.updatePrivacyAgree(getContext(), true);

        mapView=binding.mapView;
        mapView.onCreate(savedInstanceState);

        aMap=mapView.getMap();
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));//放大定位级别

        //设置地图类型
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        GetLocationPermission();

        MyLocationStyle locationStyle = new MyLocationStyle();
        locationStyle.strokeColor(Color.BLUE);
        locationStyle.strokeWidth(5);
        aMap.setMyLocationStyle(locationStyle);

        // 设置定位监听
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式，参见类AMap。
        aMap.setMyLocationType(AMap.MAP_TYPE_NORMAL);
        // 设置为true表示系统定位按钮显示并响应点击，false表示隐藏，默认是false
        aMap.setMyLocationEnabled(true);

        try {
            aMapLocationClient = new AMapLocationClient(getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        aMapLocationClient.setLocationListener(this);

        //初始化定位参数
        aMapLocationClientOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        aMapLocationClientOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        aMapLocationClientOption.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        aMapLocationClientOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        aMapLocationClientOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        aMapLocationClientOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        //启动定位
        aMapLocationClient.startLocation();
        /*WebView webView=binding.web;
        webView.setClickable(true);//设置可点击
        webView.getSettings().setJavaScriptEnabled(true);//支持JS
        webView.getSettings().setSupportZoom(true);//设置可以支持缩放
        webView.getSettings().setBuiltInZoomControls(true);//设置出现缩放工具
        webView.getSettings().setDomStorageEnabled(true);//设置为使用webView推荐的窗口，主要是为了配合下一个属性
        webView.getSettings().setUseWideViewPort(true);//扩大缩放比例
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        sheds_btu.setBackground(getResources().getDrawable(R.drawable.round_select));
        greenhouses_btu.setBackground(getResources().getDrawable(R.drawable.round));
        field_btu.setBackground(getResources().getDrawable(R.drawable.round));
        webView.loadUrl("https://sh.coda.wiki:5002/2000");*/



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

        /*webView.setWebChromeClient(new WebChromeClient(){
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
        });*/
        
        return root;
    }

    public void GetCityWeather(String city,TextView textView){
        WeatherSearchQuery mQuery = new WeatherSearchQuery(city, WeatherSearchQuery.WEATHER_TYPE_LIVE);
        WeatherSearch mWeatherSearch= null;
        try {
            mWeatherSearch = new WeatherSearch(getContext());
            mWeatherSearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
                @Override
                public void onWeatherLiveSearched(LocalWeatherLiveResult localWeatherLiveResult, int rCode) {
                    //天气实时数据
                    if (rCode == 1000) {
                        if (localWeatherLiveResult != null&&localWeatherLiveResult.getLiveResult() != null) {
                            LocalWeatherLive weatherlive = localWeatherLiveResult.getLiveResult();
                            String WeatherStr = "" + weatherlive.getReportTime() + "发布\n" +
                                    weatherlive.getWeather() + "\n" +
                                    weatherlive.getTemperature() + "°" + "\n" +
                                    weatherlive.getWindDirection() + "风\t" +
                                    weatherlive.getWindPower() + "级" + "\n" +
                                    "湿度\t" + weatherlive.getHumidity() + "%";
                            textView.setText(WeatherStr);
                        }else {
                            Toast.makeText(getContext(),"ERROR!",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getContext(),"ERROR!",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {
                    //天气预报数据
                }
            });
            mWeatherSearch.setQuery(mQuery);
            mWeatherSearch.searchWeatherAsyn(); //异步搜索
        } catch (AMapException e) {
            Toast.makeText(getContext(),"ERROR!",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void GetLocationPermission(){
        XXPermissions.with(this).permission(Permission.ACCESS_FINE_LOCATION)
                .permission(Permission.ACCESS_BACKGROUND_LOCATION)
                .permission(Permission.ACCESS_COARSE_LOCATION)
                .request(new OnPermissionCallback() {
            @Override
            public void onGranted(List<String> permissions, boolean all) {
                if(all){
//                        Toast.makeText(getContext(),"开始录音",Toast.LENGTH_SHORT).show();
                }else {
                    AlertDialog.Builder dialog=new AlertDialog.Builder(getContext());
                    dialog.setTitle("权限错误");
                    dialog.setMessage("未授权定位权限，无法进行地图定位");
                    dialog.show();
                }
            }
        });


    }

    /**
     * 定位回调监听，当定位完成后调用此方法
     * @param aMapLocation
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if(listener!=null && aMapLocation!=null) {
            listener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                aMapLocation.getLatitude();//获取经度
                aMapLocation.getLongitude();//获取纬度;
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果
                aMapLocation.getCountry();//国家信息
                aMapLocation.getProvince();//省信息
                aMapLocation.getCity();//城市信息
                aMapLocation.getDistrict();//城区信息
                aMapLocation.getRoad();//街道信息
                aMapLocation.getCityCode();//城市编码
                aMapLocation.getAdCode();//地区编码

                MyCity=aMapLocation.getCity();
                GetCityWeather(MyCity,WeatherText);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("Tomato","location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        listener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {

    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        //销毁定位客户端
        if(aMapLocationClient!=null){
            aMapLocationClient.onDestroy();
            aMapLocationClient = null;
            aMapLocationClientOption = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}