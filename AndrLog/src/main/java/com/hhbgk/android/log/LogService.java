package com.hhbgk.android.log;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.List;

public class LogService extends Service {
    private String tag = getClass().getSimpleName();
    private WindowManager mWM;
    private DisplayMetrics mDisplayMetrics;
    private WindowManager.LayoutParams mWMLayoutParams;
    private LinearLayout mLolly;
    private RecyclerView mRecyclerView;
    static final int MSG_UPDATE_LOG = 1000;
    private LogTask mWorkerThread;
    private LogAdapter mAdapter;
    private TopbarView mTopbarView;
    private Spinner mTagsSpinner = null;
    private List<String> mTags = null;
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
        mWorkerThread = new LogTask("my_thread");
        mWorkerThread.start();
        mWorkerThread.setUIHandler(mHandler);
        initLollyWindow();

        initLollyListener();

        mWorkerThread.getWorkHandler().sendEmptyMessage(LogTask.MSG_READ_LOG);
    }

    private void initLollyListener() {
        mTopbarView.setOnTouchListener(new View.OnTouchListener() {
            private int originY = mWMLayoutParams.y;
            private int deltaY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    deltaY = (int) (event.getRawY() - originY);
                    if (Math.abs(deltaY) > 5.0f) {
                        mWMLayoutParams.y = originY + deltaY - 90;
                        mWM.updateViewLayout(mLolly, mWMLayoutParams);
                    }
                }
                return true;
            }
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWorkerThread != null) {
            mWorkerThread.quit();
            mWorkerThread = null;
        }
    }

    private void initLollyWindow() {
        //init the window layout params
        mWM = (WindowManager) this.getApplication().getSystemService(Context.WINDOW_SERVICE);
        mDisplayMetrics = new DisplayMetrics();
        mWM.getDefaultDisplay().getMetrics(mDisplayMetrics);
        mWMLayoutParams = new WindowManager.LayoutParams();
//        mWMLayoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        mWMLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        mWMLayoutParams.format = PixelFormat.TRANSLUCENT;
        mWMLayoutParams.gravity = Gravity.START | Gravity.TOP;
        mWMLayoutParams.x = 0;
        mWMLayoutParams.y = 0;
        mWMLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWMLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mWMLayoutParams.height = getDip(300);//WindowManager.LayoutParams.MATCH_PARENT;

        //init the log container
        initLolly();
        mWM.addView(mLolly, mWMLayoutParams);
        mRecyclerView = (RecyclerView) new RecyclerView(this);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT/*getDip(300)*/);
        mRecyclerView.setLayoutParams(params);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new LogAdapter();
        mRecyclerView.setAdapter(mAdapter);

        //mTerminalBar
        mTopbarView = new TopbarView(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                getDip(40));
        mTopbarView.setLayoutParams(lp);
        mTopbarView.setOrientation(LinearLayout.HORIZONTAL);
        mTopbarView.setBackgroundColor(Color.parseColor("#303F9F"));

        ArrayAdapter tagAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mTags!!);
        mTagsSpinner.setAdapter(tagAdapter);

        mTagsSpinner = Spinner(this)
        lpc = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        mTagsSpinner!!.layoutParams = lpc
        mTags = ArrayList()
        mTags!!.add("All")
        val tagAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, mTags!!)
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mTagsSpinner!!.adapter = tagAdapter
        mTagsSpinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                mLogAdapter!!.changeTags(tagAdapter.getItem(position))
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                mLogAdapter!!.changeTags(tagAdapter.getItem(0))
            }
        }

        mLolly.addView(mTopbarView);
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
    }



    private int getDip(Integer dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.floatValue(),
                getResources().getDisplayMetrics());
    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MSG_UPDATE_LOG:
                    mAdapter.addData((String) message.obj);
                    int p = mAdapter.getItemCount();
                    mRecyclerView.smoothScrollToPosition(p);
                    break;
            }
            return false;
        }
    });
}