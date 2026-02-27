package com.violin.base.act.beans

import android.app.Activity
import kotlin.reflect.KClass

data class DetailActivityData(
    val title: String,
    val fragmentClassName: String,
    var activityClass: KClass<out Activity>? = null
)
