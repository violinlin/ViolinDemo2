package com.violin.base.act.exts

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

fun ImageView.load(
    model: Any?,
    @DrawableRes placeholderRes: Int? = null,
    @DrawableRes errorRes: Int? = null
) {
    if (context.isDestroy()) {
        return
    }
    val request = Glide.with(this).load(model)
    placeholderRes?.let { request.placeholder(it) }
    errorRes?.let { request.error(it) }
    request.into(this)
}

fun Context?.isDestroy(): Boolean {
    if (this == null) {
        return true
    }
    if (this is Activity) {
        if (isFinishing) {
            return true
        } else {
            return isDestroyed
        }
    }
    return false
}
