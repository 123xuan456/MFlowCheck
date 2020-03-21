package com.mtm.flowcheck.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.speech.utils.LogUtil;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvent;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.util.ResourceUtil;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.bean.UploadAudioAndTxtBean;
import com.mtm.flowcheck.bean.UserBean;
import com.mtm.flowcheck.dao.DBHelper;
import com.mtm.flowcheck.service.NFCService;
import com.mtm.flowcheck.utils.FileUtils;
import com.mtm.flowcheck.utils.JsonParser;
import com.mtm.flowcheck.utils.LogUtils;
import com.mtm.flowcheck.utils.ToastUitl;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.mtm.flowcheck.service.NFCService.COMMANDS_START_RECORD_VIDEO;
import static com.mtm.flowcheck.service.NFCService.COMMANDS_STOP_RECORD_VIDEO;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_FINSIH;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_PAUSE;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_RESUME;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_START;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_STOP;

public class IatDemo extends Activity implements OnClickListener {
    private static String TAG = "IatDemo";
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 听写结果内容
    private EditText mResultText;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
    private Toast mToast;
    private String mEngineType = "cloud";
    private LinearLayout llReset;
    private LinearLayout llFinish;
    private ImageView ivController;
    private boolean isStart = false;
    private boolean isPause = false;
//    RecordManager recordManager;
    public Chronometer chronometer;
    public TextView tvAudioState;
    public TextView tvAudioWords;
    private long curBase;


    @SuppressLint("ShowToast")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.iatdemo);
        initLayout();
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mIat = SpeechRecognizer.createRecognizer(IatDemo.this, mInitListener);
    }

    /**
     * 初始化Layout。
     */
    private void initLayout() {
        findViewById(R.id.iat_recognize).setOnClickListener(this);
        findViewById(R.id.iat_stop).setOnClickListener(this);
        findViewById(R.id.iat_cancel).setOnClickListener(this);
        llReset = findViewById(R.id.ll_reset);
        ivController = findViewById(R.id.iv_controller);
        llFinish = findViewById(R.id.ll_finish);
        llReset.setOnClickListener(this);
        ivController.setOnClickListener(this);
        llFinish.setOnClickListener(this);
        chronometer = findViewById(R.id.chronometer);
        tvAudioState = findViewById(R.id.tv_audio_state);
        tvAudioWords = findViewById(R.id.tv_audio_words);
        mResultText = ((EditText) findViewById(R.id.et_end_record_hint));
//        initRecordManager();
    }

    /**
     * 初始化录音服务
     */
//    private void initRecordManager() {
//        recordManager = RecordManager.getInstance();
//        recordManager.init(getApplication(), true);
//        recordManager.changeFormat(RecordConfig.RecordFormat.MP3);//可保存三种录音文件
//        recordManager.changeRecordConfig(recordManager.getRecordConfig().setSampleRate(8000));//音频采样率
//        recordManager.changeRecordConfig(recordManager.getRecordConfig().setEncodingConfig(AudioFormat.ENCODING_PCM_16BIT));//音频位宽
//        recordManager.changeRecordDir(FileUtils.recordPath(this));//录音文件夹
//        recordManager.changeRecordName("test");
//
//    }

    int ret = 0;// 函数调用返回值

    @Override
    public void onClick(View view) {
//        if (null == mIat) {
//            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
//            this.showTip("创建对象失败，请确认 libmsc.so 放置正确，\n 且有调用 createUtility 进行初始化");
//            return;
//        }
        switch (view.getId()) {
            // 开始听写
            // 如何判断一次听写结束：OnResult isLast=true 或者 onError
            case R.id.iat_recognize:
                if (initAudioPermission()) {
                    mIatResults.clear();
//                    // 设置参数
//                    setParam();
//                    ret = mIat.startListening(mRecognizerListener);
//                    if (ret != ErrorCode.SUCCESS) {
//                        showTip("听写失败,错误码：" + ret + ",请点击网址https://www.xfyun.cn/document/error-code查询解决方案");
//                    } else {
//                        showTip("请开始说话");
//                    }

                    startSpeech();


                }
                break;
            // 停止听写
            case R.id.iat_stop:
                if (null != mIat) {
                    mIat.cancel();
                }
//                mIat.stopListening();
                showTip("停止听写");
                break;
            // 取消听写
            case R.id.iat_cancel:
                if (null != mIat) {
                    mIat.cancel();
                }
//                mIat.cancel();
                showTip("取消听写");
                break;


            case R.id.ll_reset:
//                llFinish.setClickable(true);
                cancel();
                showTip("取消录音");
                break;
            case R.id.iv_controller:// 开始--暂停
                if (initAudioPermission()) {
                    if (isStart) {
                        pause();
                    } else {
                        if (isPause) {
                            resume();
                        } else {
                            start();
                        }
                        isStart = true;
                    }
                } else {
                    ToastUitl.show("未开启录音权限，无法使用录音功能");
                }
                break;
            case R.id.ll_finish:
                cancel();
                showTip("结束录音");
                break;


            default:
                break;
        }
    }

    public void updateBtnTextByStatus(int status) {
        switch (status) {
            case STATUS_START:
                //添加振动
                Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
                assert vibrator != null;
                vibrator.vibrate(100);
                tvAudioWords.setText(String.format(getResources().getString(R.string.words), 0));
                chronometer.setBase(SystemClock.elapsedRealtime() - curBase);
                chronometer.start();
                tvAudioState.setText("录音识别中");
                mResultText.setText("");
                ivController.setImageResource(R.drawable.icon_start);
                break;
            case STATUS_RESUME:
                chronometer.setBase(SystemClock.elapsedRealtime() - curBase);
                chronometer.start();
                tvAudioState.setText("录音识别中");
                ivController.setImageResource(R.drawable.icon_start);
                break;
            case STATUS_PAUSE:
                curBase = SystemClock.elapsedRealtime() - chronometer.getBase();
                chronometer.stop();
                tvAudioState.setText("暂停中");
                ivController.setImageResource(R.drawable.icon_stop);
                break;
            case STATUS_STOP:
                curBase = 0;
                chronometer.stop();
                tvAudioState.setText("录制完成");
                ivController.setImageResource(R.drawable.icon_stop);

//                recordManager.setRecordResultListener(new RecordResultListener() {
//                    @Override
//                    public void onResult(File result) {
//                        showTip("录音结束： " + result.getAbsolutePath());
//                    }
//                });

                break;
            case STATUS_FINSIH:
                curBase = 0;
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                tvAudioState.setText("");
                tvAudioWords.setText(String.format(getResources().getString(R.string.words), mResultText.getText().length()));
                ivController.setImageResource(R.drawable.icon_stop);

            default:
                break;
        }
    }

    private void setClickable(boolean clickable) {
        ivController.setClickable(!clickable);
        llReset.setClickable(clickable);
        llFinish.setClickable(clickable);
    }

    protected void start() {
        showTip("开始录音");
//        recordManager.start();
        updateBtnTextByStatus(STATUS_START);

        startSpeech();
    }

    protected void resume() {
        showTip("继续录音");
//        recordManager.resume();
        updateBtnTextByStatus(STATUS_RESUME);

        mIat.cancel();
        mIat = null;
        mIat = SpeechRecognizer.createRecognizer(IatDemo.this, mInitListener);
        startSpeech();
    }

    protected void pause() {
        isPause = true;
        isStart = false;
        showTip("暂停录音");
//        recordManager.pause();
        updateBtnTextByStatus(STATUS_PAUSE);
        if (null != mIat) {
            mIat.cancel();
        }
    }

    protected void cancel() {
        isPause = false;
        isStart = false;
        showTip("取消听写");
//        recordManager.stop();
        updateBtnTextByStatus(STATUS_FINSIH);
        if (null != mIat) {
            mIat.cancel();
        }
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
            showTip(error.getPlainDescription(true));
            EventBus.getDefault().post("speech");
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtil.i("isLast=" + isLast);
            //isLast=true
            EventBus.getDefault().post("speech");
            if (isLast) {
                String text = JsonParser.parseIatResult(results.getResultString());
                mResultText.append(text);
                mResultText.setSelection(mResultText.length());
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
            Log.d(TAG, "返回音频数据：" + data.length);

        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_AUDIO_URL);
                Log.d(TAG, "session id =" + sid);
            }
        }
    };

    private void showTip(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mToast.setText(str);
                mToast.show();
            }
        });
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置引擎
//        mEngineType = SpeechConstant.TYPE_CLOUD;//在线
        mEngineType = SpeechConstant.TYPE_LOCAL;//离线

        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
        //mIat.setParameter(MscKeys.REQUEST_AUDIO_URL,"true");
        if (mEngineType.equals(SpeechConstant.TYPE_LOCAL)) {
            // 设置本地识别资源
            mIat.setParameter(ResourceUtil.ASR_RES_PATH, getResourcePath());
        }
        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域（普通话）
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
//		mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
//		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");

    }

    private String getResourcePath() {
        StringBuffer tempBuffer = new StringBuffer();
        //识别通用资源
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "iat/common.jet"));
        tempBuffer.append(";");
        tempBuffer.append(ResourceUtil.generateResourcePath(this, ResourceUtil.RESOURCE_TYPE.assets, "iat/sms_16k.jet"));
        //识别8k资源-使用8k的时候请解开注释
        return tempBuffer.toString();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mIat) {
            // 退出时释放连接
            mIat.cancel();
            mIat.destroy();
        }
    }

    public boolean initAudioPermission() {
        String[] permissions = {
                Manifest.permission.RECORD_AUDIO,
        };
        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                // 进入到这里代表没有权限.
                LogUtils.i("权限未开启" + perm);
                ActivityCompat.requestPermissions(this, permissions, 123);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(String commands) {
        //此次监听录音结束之后再重启录音实现长录音功能
        if (commands.equals("speech")) {
            mIat.cancel();
            mIat = null;
            mIat = SpeechRecognizer.createRecognizer(IatDemo.this, mInitListener);
            startSpeech();
        }

    }

    private void startSpeech() {
        if (mIat != null) {
            setParam();
            ret = mIat.startListening(mRecognizerListener);
        }
    }

}
