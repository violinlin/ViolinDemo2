package com.violin.base.act.beans

import android.app.Activity
import kotlin.reflect.KClass

data class FeatureItemData(
    val title: String,
    val fragmentType: String,
    var activityClass: KClass<out Activity>? = null
)
