package com.hhbgk.android.log;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.MyHolder> {
    private final List<String> list;
    public LogAdapter() {
        list =  new ArrayList<>();
    }

    public void addData(String log) {
        list.add(log);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout layout = new LinearLayout(parent.getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(params);
        return new MyHolder(layout);
/*
        TextView textView = new TextView(parent.getContext());
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(lp);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
        textView.setTextColor(Color.WHITE);
        return new MyHolder(textView);
        */
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String item = list.get(position);
//        holder.updateText(item);
        holder.textView.setText(spanLine(item));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private ForegroundColorSpan mRedErrorSpan = new ForegroundColorSpan(Color.parseColor("#ff6b68"));
    private ForegroundColorSpan mVerboseWhiteSpan = new ForegroundColorSpan(Color.WHITE);
    private ForegroundColorSpan mDebugBlueSpan = new ForegroundColorSpan(Color.parseColor("#327ebb"));
    private ForegroundColorSpan mInfoGreenSpan = new ForegroundColorSpan(Color.parseColor("#41bb30"));
    private ForegroundColorSpan mWarnYellowSpan = new ForegroundColorSpan(Color.YELLOW);
    private SpannableStringBuilder spanLine(String line) {
        SpannableStringBuilder builder = new SpannableStringBuilder(line);
        if (line.contains("V/")) {
            builder.setSpan(mVerboseWhiteSpan, 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
        }
        if (line.contains("D/")) {
            builder.setSpan(mDebugBlueSpan, 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
        }
        if (line.contains("I/")) {
            builder.setSpan(mInfoGreenSpan, 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
        }
        if (line.contains("System.err")) {
            builder.setSpan(mWarnYellowSpan, 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
        }
        if (line.contains("E/")) {
            builder.setSpan(mRedErrorSpan, 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
        }
        if (line.contains("W/")) {
            builder.setSpan(mWarnYellowSpan, 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
        }
        if (line.contains("A/")) {
            builder.setSpan(mRedErrorSpan, 0, line.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return builder;
        }
        return builder;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView textView;
        //实现的方法
        MyHolder(View itemView) {
            super(itemView);
            textView = new TextView(itemView.getContext());
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                    AbsListView.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(lp);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f);
            textView.setTextColor(Color.WHITE);
            ((LinearLayout)itemView).addView(textView);
        }

        void updateText(String s) {
            ((TextView)itemView).setText(s);
        }
    }
}
