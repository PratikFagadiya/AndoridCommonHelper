package com.pratik.commmonhelper_library;

import android.os.Bundle;
import android.widget.Toast;

import com.pratik.commonnhelper.BaseActivity;
import com.pratik.commonnhelper.CustomToast;
import com.pratik.commonnhelper.LogUtils;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean debugMode = BuildConfig.DEBUG;

        LogUtils.logD("TAG", "Height Is " + displayHeight + " \nWidth Is " + displayWidth, debugMode);

        CustomToast.makeText(mContext, "Hello Developer", Toast.LENGTH_SHORT, CustomToast.TYPE_SUCCESS).show();
    }
}