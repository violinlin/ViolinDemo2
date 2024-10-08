package yz.l.core.mvvm.exts

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

fun <T : ViewDataBinding> View.binding(@LayoutRes resId: Int): Lazy<T> =
    lazy { DataBindingUtil.inflate(LayoutInflater.from(context), resId, null, false) }