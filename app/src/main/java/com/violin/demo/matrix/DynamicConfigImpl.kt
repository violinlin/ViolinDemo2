package com.violin.demo.matrix

import com.tencent.mrs.plugin.IDynamicConfig

class DynamicConfigImpl : IDynamicConfig {
    val isFPSEnable: Boolean
        get() = true
    val isTraceEnable: Boolean
        get() = true
    val isMatrixEnable: Boolean
        get() = true
    val isDumpHprof: Boolean
        get() = false

    val isSignalAnrTraceEnable: Boolean
        get() = false


    override fun get(key: String, defStr: String?): String? {
        return defStr
    }

    override fun get(key: String, defInt: Int): Int {
        if (key == IDynamicConfig.ExptEnum.clicfg_matrix_trace_evil_method_threshold.toString()) {
            return 200
        }
        return defInt
    }

    override fun get(key: String, defLong: Long): Long {
        return defLong
    }

    override fun get(key: String, defBool: Boolean): Boolean {
        return defBool
    }

    override fun get(key: String, defFloat: Float): Float {
        return defFloat
    }
}