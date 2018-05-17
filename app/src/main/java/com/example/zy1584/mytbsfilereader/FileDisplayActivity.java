package com.example.zy1584.mytbsfilereader;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsReaderView;
import com.tencent.smtt.sdk.ValueCallback;

import java.io.File;

public class FileDisplayActivity extends AppCompatActivity {

    private static final String TAG = "FileDisplayActivity";
    private TbsReaderView mTbsReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_display);

        mTbsReaderView = new TbsReaderView(this, new TbsReaderView.ReaderCallback() {
            @Override
            public void onCallBackAction(Integer integer, Object o, Object o1) {
                Log.d(TAG, "onCallBackAction: " + integer);
            }
        });
        ViewGroup viewById = findViewById(android.R.id.content);
        viewById.addView(mTbsReaderView);

        Intent intent = getIntent();
        if (intent != null) {
            String path = intent.getStringExtra("path");
            if (!TextUtils.isEmpty(path)) {
                //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
                String bsReaderTemp = "/storage/emulated/0/TbsReaderTemp";
                File bsReaderTempFile = new File(bsReaderTemp);

                if (!bsReaderTempFile.exists()) {
                    Log.d(TAG, "准备创建/storage/emulated/0/TbsReaderTemp！！");
                    boolean mkdir = bsReaderTempFile.mkdir();
                    if (!mkdir) {
                        Log.e(TAG, "创建/storage/emulated/0/TbsReaderTemp失败！！！！！");
                    }
                }

                boolean bool = mTbsReaderView.preOpen(getFileType(path), false);
                if (bool) {
                    Bundle bundle = new Bundle();
                    bundle.putString("filePath", path);
                    bundle.putString("tempPath", Environment.getExternalStorageDirectory() + "/" + "TbsReaderTemp");
                    mTbsReaderView.openFile(bundle);
                }
            }
        }
    }

    public static void show(Context context, String path) {
        Intent intent = new Intent(context, FileDisplayActivity.class);
        intent.putExtra("path", path);
        context.startActivity(intent);
    }

    /***
     * 获取文件类型
     *
     * @param paramString
     * @return
     */
    private String getFileType(String paramString) {
        String str = "";

        if (TextUtils.isEmpty(paramString)) {
            Log.d(TAG, "paramString---->null");
            return str;
        }
        Log.d(TAG, "paramString:" + paramString);
        int i = paramString.lastIndexOf('.');
        if (i <= -1) {
            Log.d(TAG, "i <= -1");
            return str;
        }


        str = paramString.substring(i + 1);
        Log.d(TAG, "paramString.substring(i + 1)------>" + str);
        return str;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onStopDisplay();
    }

    /**
     * 在停止展示时记得要调用onStop方法，否则再次打开文件会显示一直加载中。
     */
    public void onStopDisplay() {
        if (mTbsReaderView != null) {
            mTbsReaderView.onStop();
        }
    }
}
