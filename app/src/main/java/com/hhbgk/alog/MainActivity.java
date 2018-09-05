package com.hhbgk.alog;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.hhbgk.android.log.LogService;

public class MainActivity extends Activity {
    private String tag = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LogService.showAndrlog(this, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 10;
                while (i>0) {
                    i--;
//                    Log.e(tag, "i=====================================================================================================" +i);
                    Log.v(tag, "start============");
                    Log.d(tag, "start============");
                    Log.i(tag, "start============");
                    Log.w(tag, "start============");
                    Log.e(tag, "start============");
                    SystemClock.sleep(1000);
                }
            }
        }).start();
/*
        new Thread(new Runnable() {
            @Override
            public void run() {
                Process process = null;
                try {
                    //Runtime.getRuntime().exec("logcat -c");
                    process = Runtime.getRuntime().exec("logcat -s '*:D'");
                    if (process != null) {
                        InputStream is = process.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String str ;
                        while(null != (str = br.readLine())){
                            Log.e(tag, str);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
*/
    }
}
