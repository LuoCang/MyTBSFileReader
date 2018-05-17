package com.example.zy1584.mytbsfilereader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.RadioGroup;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private boolean isOpenInside;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_download_and_open_doc).setOnClickListener(this);
        findViewById(R.id.btn_local_doc).setOnClickListener(this);
        findViewById(R.id.btn_local_excel).setOnClickListener(this);
        findViewById(R.id.btn_local_txt).setOnClickListener(this);
        findViewById(R.id.btn_local_pdf).setOnClickListener(this);
        findViewById(R.id.btn_local_ppt).setOnClickListener(this);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rg_open_way);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_openFile) {
                    isOpenInside = true;
                } else if (checkedId == R.id.rb_openFileReader) {
                    isOpenInside = false;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        String filePath = getFilePath(v.getId());
        if (TextUtils.isEmpty(filePath)) return;
        if (isOpenInside) {
            FileDisplayActivity.show(this, filePath);
        } else {
            QbSdk.openFileReader(this, filePath, null, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String s) {
                    Log.d(TAG, "onReceiveValue: " + s);
                }
            });
        }
    }

    private String getFilePath(int id) {
        String path = null;
        switch (id) {
            case R.id.btn_download_and_open_doc:
                path = "http://www.hrssgz.gov.cn/bgxz/sydwrybgxz/201101/P020110110748901718161.doc";
                break;
            case R.id.btn_local_doc:
                path = "/storage/emulated/0/test.docx";
                break;
            case R.id.btn_local_txt:
                path = "/storage/emulated/0/test.txt";
                break;
            case R.id.btn_local_excel:
                path = "/storage/emulated/0/test.xlsx";
                break;
            case R.id.btn_local_ppt:
                path = "/storage/emulated/0/test.pptx";
                break;
            case R.id.btn_local_pdf:
                path = "/storage/emulated/0/test.pdf";
                break;
        }
        return path;
    }
}
