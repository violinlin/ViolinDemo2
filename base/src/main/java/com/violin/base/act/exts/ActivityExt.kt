package com.violin.base.act.exts

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun <T : ViewDataBinding> AppCompatActivity.binding(@LayoutRes resId: Int): Lazy<T> =
    lazy { DataBindingUtil.setContentView(this, resId) }