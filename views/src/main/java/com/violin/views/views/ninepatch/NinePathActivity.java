package com.violin.views.views.ninepatch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.violin.views.R;

import java.util.ArrayList;

public class NinePathActivity extends Activity {
    public static void start(Context context) {
        Intent starter = new Intent(context, NinePathActivity.class);
        context.startActivity(starter);
    }
    NinePathAdapter adapter = new NinePathAdapter();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ninepath);
        findViewById(R.id.btn_notify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
            }
        });
        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<NinePathAdapter.NinePathBean> list = new ArrayList<>();
        final String urlWebp = "http://192.168.25.36:8000/xunlei/bubble/bubble_back2.webp";
        final String urlPng = "http://192.168.25.195:8000/xunlei/bubble/bubble_back.png";
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
        recyclerView.setAdapter(adapter);
    }
}
