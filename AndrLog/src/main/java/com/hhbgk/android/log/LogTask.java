package com.hhbgk.android.log;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;

import static com.hhbgk.android.log.LogService.MSG_UPDATE_LOG;


/**
 * Description:
 * Author:created by bob on 18-9-5.
 */
public final class LogTask extends HandlerThread implements Handler.Callback {
    private String tag = getClass().getSimpleName();
    private Handler mWorkerHandler;
    final static int MSG_READ_LOG = 100;
    private WeakReference<Handler> mHandlerWeakRef;

    public LogTask(String name) {
        super(name);
    }

    @Override
    protected void onLooperPrepared() {
        mWorkerHandler = new Handler(getLooper(), this);
    }

    public Handler getWorkHandler(){
        return mWorkerHandler;
    }

    public void setUIHandler(Handler handler){
        mHandlerWeakRef = new WeakReference<>(handler);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what){
            case MSG_READ_LOG:
                Process process;
                try {
                    Runtime.getRuntime().exec("logcat -c");
                    process = Runtime.getRuntime().exec("logcat -v time");
                    if (process != null) {
                        InputStream is = process.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));
                        String str ;
                        while(null != (str = br.readLine())){

                            if (mHandlerWeakRef != null && mHandlerWeakRef.get() != null) {
                                Message m = Message.obtain();
                                m.what = MSG_UPDATE_LOG;
                                m.obj = str;
                                mHandlerWeakRef.get().sendMessage(m);
                                //Log.e(tag, str);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return false;
    }

}
