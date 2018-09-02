package com.hhbgk.android.log;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class LogService extends Service {
    private String tag = getClass().getSimpleName();
    private WindowManager mWM;
    private DisplayMetrics mDisplayMetrics;
    private WindowManager.LayoutParams mWMLayoutParams;
    private LinearLayout mLolly;
    private RecyclerView mRecyclerView;
    public LogService() {
    }

    public static void showAndrlog(Context context, List<String> tags) {
        Intent start = new Intent(context, LogService.class);
//        start.putExtra("action", FLAG_INIT_ADD_WINDOW);
//        start.putExtra(LOLLY_TAGS, tags);
        context.startService(start);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(tag, "on create");
        initLollyWindow();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(tag, "on Start Command");
        /*if (mLolly.getVisibility() != View.VISIBLE) {
            mLolly.setVisibility(View.VISIBLE);
            mWM.addView(mLolly, mWMLayoutParams);
        } else {
            mWM.updateViewLayout(mLolly, mWMLayoutParams);
        }*/
        return Service.START_NOT_STICKY;
    }

    private void initLollyWindow() {
        //init the window layout params
        mWM = (WindowManager) this.getApplication().getSystemService(Context.WINDOW_SERVICE);
        mDisplayMetrics = new DisplayMetrics();
        mWM.getDefaultDisplay().getMetrics(mDisplayMetrics);
        mWMLayoutParams = new WindowManager.LayoutParams();
        mWMLayoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        mWMLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        mWMLayoutParams.format = PixelFormat.TRANSLUCENT;
        mWMLayoutParams.gravity = Gravity.START | Gravity.TOP;
        mWMLayoutParams.x = 0;
        mWMLayoutParams.y = 0;
        mWMLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWMLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWMLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        //init the log container
        initLolly();
        mWM.addView(mLolly, mWMLayoutParams);
        mRecyclerView = (RecyclerView) new RecyclerView(this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, getDip(300));
        mRecyclerView.setLayoutParams(params);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        LogAdapter mAdapter = new LogAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mLolly.addView(mRecyclerView);
    }

    private void initLolly() {

        //LollyContainer
        mLolly = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        mLolly.setLayoutParams(lp);
        mLolly.setOrientation(LinearLayout.VERTICAL);
        mLolly.setVisibility(View.VISIBLE);
        mLolly.setBackgroundColor(Color.BLACK);

        LogAdapter adapter = new LogAdapter();


    }


    private int getDip(Integer dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.floatValue(),
                getResources().getDisplayMetrics());
    }
}
