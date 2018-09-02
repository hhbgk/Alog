package com.hhbgk.android.log;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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
        list =  new ArrayList<String>();
        for (int i = 0; i < 100; i++) {
            list.add("test " + i);
        }
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
        holder.textView.setText(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView textView;
        //实现的方法
        MyHolder(View itemView) {
            super(itemView);
            textView = (TextView) new TextView(itemView.getContext());
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
