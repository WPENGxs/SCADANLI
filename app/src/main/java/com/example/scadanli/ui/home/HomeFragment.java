package com.example.scadanli.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.scadanli.MainActivity;
import com.example.scadanli.R;
import com.example.scadanli.SCADANLI_Socket;
import com.example.scadanli.databinding.FragmentHomeBinding;
import com.example.scadanli.ui.notifications.NotificationsFragment;
import com.example.scadanli.util.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    String return_str="";//socket返回的字符串
    Handler handler=null;

    LinearLayout talk_view;

    private static String TAG = MainActivity.class.getSimpleName();
    // 语音听写对象
    public SpeechRecognizer mIat;
    // 语音听写UI
    public RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();
    private EditText editText;
    public EditText showContacts;
    public TextView languageText;
    private Toast mToast;
    public SharedPreferences mSharedPreferences;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    public String[] languageEntries;
    public String[] languageValues;
    private String language = "zh_cn";
    public int selectedNum = 0;
    private String resultType = "json";
    private StringBuffer buffer = new StringBuffer();
    private boolean isClick_even=false;


    @SuppressLint("HandlerLeak")
    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        talk_view=binding.talkView;//定义中间的LinearLayout——talk_view

        AddText(talk_view,"您好,这里是智能农业实时操控平台,请问有什么可以帮到您的吗?",false);
        //添加机器人默认语句

        handler=new android.os.Handler(){
            public void handleMessage(Message message){
                switch (message.what){
                    case 0x01:if(return_str.length()<3){
                        return_str="解析错误,请重新输入";
                    }
                    if(return_str.equals("解析错误,请重新输入")){
                        RobotReturn(return_str);
                    }
                    else {
                        RobotReturn(StrProcess(return_str));//机器人返回
                    }
                        break;
                    case 0x02:
                        break;
                    default:RobotReturn("解析错误,请重新输入");break;
                }
            }
        };



        editText = binding.editText;
        Button text_button = binding.textButton;
        text_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.inputText = editText.getText().toString().trim();//将输入框的值传入MainActivity的inputText中
                if(!MainActivity.inputText.equals("")){
                    AddText(talk_view, MainActivity.inputText,true);//插入对话框
                    editText.setText("");//对话框置空
                    Thread thread= new Thread() {
                        @Override
                        public void run(){
                            Message message=new Message();
                            message.what=0x01;
                            try {
                                SCADANLI_Socket socket=new SCADANLI_Socket("1.15.28.84",39001);
                                socket.SentData(MainActivity.inputText);
                                return_str=socket.GetData();
                                handler.sendMessage(message);

                                socket.DisConnect();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();//开启线程
                    /*try {
                        thread.join(2000);//主线程等待服务器返回值，等待了2s
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/


                    /*if(return_str.length()<3){
                        return_str="解析错误,请重新输入";
                    }

                    if(return_str.equals("解析错误,请重新输入")){
                        RobotReturn(return_str);
                    }
                    else {
                        RobotReturn(StrProcess(return_str));//机器人返回
                    }*/
                }
            }
        });





        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        mIat = SpeechRecognizer.createRecognizer(MainActivity.context, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        //mIatDialog = new RecognizerDialog(MainActivity.context, mInitListener);
        mIatDialog = new RecognizerDialog(getContext(),mInitListener);

        mSharedPreferences = MainActivity.context.getSharedPreferences("com.iflytek.setting",
                Activity.MODE_PRIVATE);

        Button ifly_button = binding.iflyButton;
        ifly_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClick_even){
                    ifly();
                }
                else{
                    mIat.stopListening();
                    showTip("停止听写");
                    isClick_even=false;
                }
            }
        });

        return root;
    }

    /**
     * 机器人返回函数
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void RobotReturn(String str){
        AddText(talk_view , str,false);
        //return_str="";//将返回字符串置空
    }

    /**
     * 将服务器发过来的字符串处理成可读性更强的语句
     * @param str
     *              处理的字符串
     * @return
     *              返回处理好的字符串
     */
    public String StrProcess(String str){
        StringBuilder return_str= new StringBuilder();
        while(str.contains("/")){
            int num=str.indexOf("/");
            return_str.append("Instructions");
            String work_str=str.substring(0,num);

            int loc=work_str.indexOf("-");
            String loc_str=work_str.substring(0,loc);
            work_str=work_str.substring(loc+1);

            int obj=work_str.indexOf("-");
            String obj_str=work_str.substring(0,obj);
            work_str=work_str.substring(obj+1);

            int val=work_str.indexOf("-");
            String val_str=work_str.substring(0,val);
            work_str=work_str.substring(val+1);

            String act_str=work_str;
            return_str.append("\nlocation:").append(loc_str)
                    .append("\tobject:").append(obj_str)
                    .append("\nvalue:").append(val_str)
                    .append("\taction:").append(act_str).append("\n");
            str=str.substring(num+1);
        }
        return return_str.toString();
    }

    /**
     * 在LinearLayout中插入一条带头像的对话框
     * right_direction为true时为用户
     * @param linearLayout
     *              布局文件
     * @param str
     *              显示的文本
     * @param right_direction
     *              方向,false为头像在左,true为头像在右
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void AddText(LinearLayout linearLayout, String str, boolean right_direction){
        LinearLayout talk_list=new LinearLayout(getContext());
        talk_list.setGravity(LinearLayout.HORIZONTAL);//设置方向
        talk_list.setLeftTopRightBottom(100,200,100,200);

        ImageView imageView=new ImageView(getContext());//设置头像
        imageView.setMaxHeight(160);
        imageView.setMaxWidth(160);
        imageView.setAdjustViewBounds(true);

        TextView textView=new TextView(getContext());//设置文本
        textView.setTextSize(15);
        textView.setTextColor(Color.rgb(0, 0, 0));//设置文本颜色为黑色

        if(right_direction){//为true时先文本再头像
            imageView.setImageResource(R.drawable.me);
            talk_list.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            textView.setBackgroundResource(R.drawable.round);
            talk_list.setPadding(300,10,0,10);
        }
        else{//为false时先头像再文本
            imageView.setImageResource(R.drawable.miao);
            talk_list.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            textView.setBackgroundResource(R.drawable.robot_round);
            talk_list.setPadding(0,10,300,10);
        }

        talk_list.addView(imageView);//向布局中添加头像
        talk_list.addView(textView);//向布局中添加文本
        textView.setText(str);
        linearLayout.addView(talk_list);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    int ret = 0; // 函数调用返回值

    public void ifly() {
        buffer.setLength(0);
        editText.setText(null);// 清空显示内容
        mIatResults.clear();
        // 设置参数
        setParam();
        boolean isShowDialog=true;
        //boolean isShowDialog = mSharedPreferences.getBoolean(
        //getString(R.string.pref_key_iat_show), true);
        if (isShowDialog) {
            isClick_even=false;
            // 显示听写对话框
            mIatDialog.setListener(mRecognizerDialogListener);
            mIatDialog.show();
            showTip("请开始说话");
        } else {
            isClick_even=true;
            // 不显示听写对话框
            ret = mIat.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                showTip("听写失败,错误码：" + ret + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            } else {
                showTip("请开始说话");
            }
        }
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, resultType);

        if (language.equals("zh_cn")) {
            String lag = mSharedPreferences.getString("iat_language_preference",
                    "mandarin");
            // 设置语言
            Log.e(TAG, "language = " + language);
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);
        } else {
            mIat.setParameter(SpeechConstant.LANGUAGE, language);
        }
        Log.e(TAG, "last language:" + mIat.getParameter(SpeechConstant.LANGUAGE));

        //此处用于设置dialog中不显示错误码信息
        //mIat.setParameter("view_tips_plain","false");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, mSharedPreferences.getString("iat_vadbos_preference", "4000"));

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, mSharedPreferences.getString("iat_vadeos_preference", "1000"));

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, mSharedPreferences.getString("iat_punc_preference", "0"));

        // 设置音频保存路径，保存音频格式支持pcm、wav.
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
                MainActivity.context.getExternalFilesDir("msc").getAbsolutePath() + "/iat.wav");
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        // 返回结果
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
        }

        // 识别回调错误
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

    };

    private void showTip(final String str) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(MainActivity.context, str, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            Log.d(TAG, "onError " + error.getPlainDescription(true));
            showTip(error.getPlainDescription(true));

        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG, results.getResultString());
            if (isLast) {
                Log.d(TAG, "onResult 结束");
            }
            if (resultType.equals("json")) {
                printResult(results);
                return;
            }
            if (resultType.equals("plain")) {
                buffer.append(results.getResultString());
                editText.setText(buffer.toString());
                editText.setSelection(editText.length());
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小 = " + volume + " 返回音频数据 = " + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
    /**
     * 显示结果
     */
    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());
        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        editText.setText(resultBuffer.toString());
        editText.setSelection(editText.length());
    }
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
            }
        }
    };
}