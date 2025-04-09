package com.violin.features.common.leak;

import android.view.View;

import java.lang.ref.WeakReference;

public class LeakTest {
    private WeakReference<LeakTest> mInstance;

    public LeakTest() {
        init();
    }

    private void init() {
        mInstance = new WeakReference<>(this);
    }

    private void fun1(View view) {
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                funTest();
            }
        });

    }

    private void fun2(View view) {
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mInstance.get().funTest();
            }
        });

    }

    private void fun3(LeakTest leakTest,View view) {
        view.setOnClickListener(new View.OnClickListener() {
             WeakReference<LeakTest> instance = new WeakReference<>(leakTest);
            @Override
            public void onClick(View v) {
                instance.get().funTest();
            }
        });

    }

    private void funTest() {

    }
}
