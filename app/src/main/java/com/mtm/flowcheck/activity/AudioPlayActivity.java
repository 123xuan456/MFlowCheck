package com.mtm.flowcheck.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mtm.flowcheck.R;
import com.mtm.flowcheck.utils.FileUtils;
import com.mtm.flowcheck.utils.recorder.player.PlayDialog;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 播放页面
 */
public class AudioPlayActivity extends BaseActivity{
    TextView titleTextView;
    ImageView backImageView;
    private ListView listview;
    private ArrayAdapter<String> adapter;
    @Override
    public int getLayoutId() {
        return R.layout.audio_activity;
    }

    @Override
    public void initView() {
        titleTextView = (TextView) findViewById(R.id.all_title);
        backImageView = (ImageView) findViewById(R.id.iv_back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView.setText("录音文件");
        listview = (ListView) findViewById(R.id.listview);
    }

    @Override
    public void initData() {
        FileUtils.createDirs(FileUtils.recordPath(AudioPlayActivity.this));
        searchFile();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = FileUtils.recordPath(AudioPlayActivity.this)+(String) listview.getItemAtPosition(position);
                new PlayDialog(AudioPlayActivity.this)
                        .addWavFile(new File(s))
                        .showDialog();
            }
        });
    }
    private void searchFile() {
        Observable
                .create(new ObservableOnSubscribe<String[]>() {
                    @Override
                    public void subscribe(ObservableEmitter<String[]> e) throws Exception {
                        File folder = new File(FileUtils.recordPath(AudioPlayActivity.this));
                        /**将文件夹下所有文件名存入数组*/
                        e.onNext(folder.list());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String[]>() {
                    @Override
                    public void accept(String[] s) throws Exception {
                        adapter = new ArrayAdapter<String>(AudioPlayActivity.this, android.R.layout.simple_list_item_1, s);
                        listview.setAdapter(adapter);
                    }
                });
    }
}
