package com.violin.base.act.dynamicres

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources

class DynamicContextWrapper(base: Context) : ContextWrapper(base) {

    private val dynamicResources by lazy { DynamicResources(base.resources) }

    override fun getResources(): Resources {
        return dynamicResources
    }
}