package com.hhbgk.alog;

import android.app.Activity;
import android.os.Bundle;

import com.hhbgk.android.log.LogService;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogService.showAndrlog(this, null);
    }
}
