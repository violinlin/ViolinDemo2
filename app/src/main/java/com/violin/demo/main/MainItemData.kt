package com.violin.demo.main

import android.app.Activity
import kotlin.reflect.KClass

data class MainItemData(
    val title: String,
    val fragmentType: String,
    var activityClass: KClass<out Activity>? = null
)
