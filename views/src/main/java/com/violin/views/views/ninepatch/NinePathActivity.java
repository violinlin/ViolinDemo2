package com.violin.views.views.ninepatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.fpt.metrics.MetricsHUD;
import com.violin.views.R;

import java.util.ArrayList;

public class NinePathActivity extends Activity {
    public static String HOST = "http://192.168.25.36:8000/xunlei/bubble/";
    public static void start(Context context) {
        Intent starter = new Intent(context, NinePathActivity.class);
        context.startActivity(starter);
    }

    NinePathAdapter adapter = new NinePathAdapter();
    RecyclerView recyclerView = null;
    

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninepath);
        new MetricsHUD().show(this);
        findViewById(R.id.btn_webp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView ivLeftUP = findViewById(R.id.win_left_up);
                Glide.with(ivLeftUP).load(HOST + "bubble_left_up.webp").into(ivLeftUP);
                ImageView ivRightUP = findViewById(R.id.win_right_up);
                Glide.with(ivRightUP).load(HOST + "bubble_right_up.webp").into(ivRightUP);
                ImageView ivRightDown = findViewById(R.id.win_right_down);
                Glide.with(ivRightDown).load(HOST + "bubble_right_down.webp").into(ivRightDown);
                ImageView ivLeftDown = findViewById(R.id.win_left_down);
                Glide.with(ivLeftDown).load(HOST + "bubble_left_down.webp").into(ivLeftDown);
            }
        });
        findViewById(R.id.btn_notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String urlWebp = HOST + "bubble_back2.webp";
                final String urlPng = "http://192.168.25.195:8000/xunlei/bubble/bubble_back.png";
                ArrayList<NinePathAdapter.NinePathBean> list = new ArrayList<>();
                for (int i = 0; i < 1; i++) {
                    String url = "";
                    if (i % 2 == 0) {
                        url = urlWebp;
                    } else {
                        url = urlWebp;
                    }
                    list.add(new NinePathAdapter.NinePathBean(url, "text:" + i));
                }
                adapter.list.addAll(list);

                adapter.notifyDataSetChanged();
            }
        });
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}
