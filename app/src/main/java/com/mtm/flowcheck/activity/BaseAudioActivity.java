package com.mtm.flowcheck.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.baidu.speech.asr.SpeechConstant;
import com.lzy.okgo.utils.HttpUtils;
import com.mtm.flowcheck.R;
import com.mtm.flowcheck.bean.CheckBean;
import com.mtm.flowcheck.bean.RequestResultBean;
import com.mtm.flowcheck.bean.UploadAudioAndTxtBean;
import com.mtm.flowcheck.bean.UserBean;
import com.mtm.flowcheck.dao.DBHelper;
import com.mtm.flowcheck.listeners.MyStringCallback;
import com.mtm.flowcheck.utils.FileUtils;
import com.mtm.flowcheck.utils.HTTPUtils;
import com.mtm.flowcheck.utils.LogUtils;
import com.mtm.flowcheck.utils.ToastUitl;
import com.mtm.flowcheck.utils.recog.MyRecognizer;
import com.mtm.flowcheck.utils.recog.listener.IRecogListener;
import com.mtm.flowcheck.utils.recog.listener.MessageStatusRecogListener;
import com.sinosoft.key.SM2Util;
import com.zlw.main.recorderlib.RecordManager;
import com.zlw.main.recorderlib.recorder.RecordConfig;
import com.zlw.main.recorderlib.recorder.RecordHelper;
import com.zlw.main.recorderlib.recorder.listener.RecordResultListener;
import com.zlw.main.recorderlib.recorder.listener.RecordStateListener;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_FINISHED;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_FINSIH;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_LONG_SPEECH_FINISHED;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_NONE;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_PAUSE;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_READY;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_RECOGNITION;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_RESUME;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_SPEAKING;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_START;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_STOP;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_STOPPED;
import static com.mtm.flowcheck.utils.recog.IStatus.STATUS_WAITING_READY;

public abstract class BaseAudioActivity extends BaseActivity {

    public CheckBean checkInfo;
    public ImageView ivController;
    public LinearLayout llReset;
    public LinearLayout llFinish;
    public EditText recordEndHintEt;
    public Chronometer chronometer;
    public TextView tvAudioState;
    public TextView tvAudioWords;
    private long curBase;
    public int nowPosition;
    /**
     * 识别控制器，使用MyRecognizer控制识别的流程
     */
    protected MyRecognizer myRecognizer;
    protected int status;
    protected Handler handler;
    private String resumeTv;
    RecordManager recordManager;
    public String fileName = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IRecogListener listener = new MessageStatusRecogListener(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handleMsg(msg);
            }
        });
        // DEMO集成步骤 1.1 1.3 初始化：new一个IRecogListener示例 & new 一个 MyRecognizer 示例,并注册输出事件
        myRecognizer = new MyRecognizer(mContext, listener);
        status = STATUS_NONE;
        tvAudioWords.setText(String.format(getResources().getString(R.string.words), 0));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onAudioClick() {
        switch (status) {
            case STATUS_RESUME://继续录音
                start();
                recordManager.resume();
                status = STATUS_WAITING_READY;
                updateBtnTextByStatus(STATUS_RESUME);
                break;
            case STATUS_NONE: // 开始录音
                start();
                recordManager.start();
                status = STATUS_WAITING_READY;
                updateBtnTextByStatus(STATUS_START);
                break;
            case STATUS_WAITING_READY: // 调用本类的start方法后，即输入START事件后，等待引擎准备完毕。
            case STATUS_READY: // 引擎准备完毕。
            case STATUS_SPEAKING: // 用户开始讲话
            case STATUS_FINISHED: // 一句话识别语音结束
            case STATUS_RECOGNITION: // 暂停录音
                stop(); //停止按钮
                recordManager.pause();
                status = STATUS_RESUME; // 引擎识别中
                updateBtnTextByStatus(STATUS_PAUSE);
                break;
            default:
                break;
        }
    }


    /**
     * 重录
     */
    public void onfinishAndReset() {
        recordManager.stop();
        cancel();
        status = STATUS_NONE; // 识别结束，回到初始状态
        updateBtnTextByStatus(STATUS_FINSIH);
    }

    @SuppressLint("StringFormatInvalid")
    protected void handleMsg(Message msg) {
        LogUtils.i("最终数据：" + msg.obj.toString());
        switch (msg.what) { // 处理MessageStatusRecogListener中的状态回调
            case STATUS_FINISHED://语音识别结束
                if (msg.arg2 == 1) {
                    LogUtils.i("最终数据1：" + msg.obj.toString());
                    if (TextUtils.isEmpty(resumeTv)) {
                        recordEndHintEt.append(msg.obj.toString());
                    } else {
                        recordEndHintEt.setText("");
                        recordEndHintEt.append(resumeTv + msg.obj.toString());
                        resumeTv = recordEndHintEt.getText().toString();
                    }
                    tvAudioWords.setText(String.format(getResources().getString(R.string.words), recordEndHintEt.getText().length()));
                }
                status = msg.what;
                break;
            case STATUS_NONE://开始识别
            case STATUS_READY:
            case STATUS_SPEAKING:
            case STATUS_RECOGNITION:
                status = msg.what;
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
                resumeTv = "";
                tvAudioWords.setText(String.format(getResources().getString(R.string.words), 0));
                chronometer.setBase(SystemClock.elapsedRealtime() - curBase);
                chronometer.start();
                tvAudioState.setText("录音识别中");
                recordEndHintEt.setText("");
                ivController.setImageResource(R.drawable.icon_start);
                break;
            case STATUS_RESUME:
                chronometer.setBase(SystemClock.elapsedRealtime() - curBase);
                chronometer.start();
                tvAudioState.setText("录音识别中");
                resumeTv = recordEndHintEt.getText().toString();
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
                break;
            case STATUS_FINSIH:
                curBase = 0;
                chronometer.stop();
                chronometer.setBase(SystemClock.elapsedRealtime());
                tvAudioState.setText("");
                recordEndHintEt.setText("");
                tvAudioWords.setText(String.format(getResources().getString(R.string.words), recordEndHintEt.getText().length()));
                ivController.setImageResource(R.drawable.icon_stop);
            default:
                break;
        }
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     */
    protected void start() {
        Map<String, Object> params = new LinkedHashMap<String, Object>();
        params.put(SpeechConstant.ACCEPT_AUDIO_VOLUME, false);
        params.put(SpeechConstant.VAD_ENDPOINT_TIMEOUT, 0); // 长语音
//        params.put(SpeechConstant.VAD, SpeechConstant.VAD_TOUCH);//自动判断关闭。  VAD_TOUCH：需用户点击关闭
        params.put(SpeechConstant.PID, 19362);
        // 声音回调
        params.put(SpeechConstant.SOUND_START, R.raw.bdspeech_recognition_start);
//        params.put(SpeechConstant.SOUND_END, R.raw.bdspeech_speech_end);
//        params.put(SpeechConstant.SOUND_SUCCESS, R.raw.bdspeech_recognition_success);
//        params.put(SpeechConstant.SOUND_ERROR, R.raw.bdspeech_recognition_error);
        params.put(SpeechConstant.SOUND_CANCEL, R.raw.bdspeech_recognition_cancel);

        // 保存录音文件（百度语音不支持暂停继续，自己保存录音文件）
//        params.put(SpeechConstant.ACCEPT_AUDIO_DATA, true); // 目前必须开启此回掉才能保存音频
//        params.put(SpeechConstant.OUT_FILE, getVoicePath());
//        LogUtils.i( "语音录音文件将保存在：" + getVoicePath());

        // params 也可以根据文档此处手动修改，参数会以json的格式在界面和logcat日志中打印
        LogUtils.i("设置的start输入参数：" + params);
        // DEMO集成步骤2.2 开始识别
        myRecognizer.start(params);
    }

    /**
     * 开始录音后，手动点击“停止”按钮。
     * SDK会识别不会再识别停止后的录音。
     */
    protected void stop() {
        // DEMO集成步骤4 (可选） 停止录音
        myRecognizer.stop();
    }

    /**
     * 开始录音后，手动点击“取消”按钮。
     * SDK会取消本次识别，回到原始状态。
     */
    protected void cancel() {
        // DEMO集成步骤5 (可选） 取消本次识别
        myRecognizer.cancel();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recordManager.stop();
        if (myRecognizer != null) {
            myRecognizer.release();
        }
    }

    /**
     * android 6.0 以上需要动态申请权限
     */
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

    public String getTxtFile() {
        return FileUtils.recordPath(mContext) + fileName + ".txt";
    }

    private String getAudioFile() {
        return FileUtils.recordPath(mContext) + fileName + "mp3";
    }

}
