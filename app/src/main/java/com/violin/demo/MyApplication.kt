package com.violin.demo

import android.app.Application
import com.tencent.matrix.Matrix
import com.tencent.matrix.trace.TracePlugin
import com.tencent.matrix.trace.config.TraceConfig
import com.violin.demo.matrix.DynamicConfigImpl
import com.violin.demo.matrix.MatrixPluginListener

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(AUncaughtExceptionHandler(this))
        initMatrix()
    }

    private fun initMatrix() {
        val builder = Matrix.Builder(this) // build matrix

        builder.pluginListener(MatrixPluginListener(this)) // add general pluginListener

        val dynamicConfig = DynamicConfigImpl() // dynamic config
        val tracePlugin = configureTracePlugin(dynamicConfig)
        builder.plugin(tracePlugin)
        Matrix.init(builder.build())
        tracePlugin?.start()
    }

    private fun configureTracePlugin(dynamicConfig: DynamicConfigImpl): TracePlugin? {
        val fpsEnable: Boolean = dynamicConfig.isFPSEnable
        val traceEnable: Boolean = dynamicConfig.isTraceEnable
        val signalAnrTraceEnable: Boolean = dynamicConfig.isSignalAnrTraceEnable
        val traceConfig = TraceConfig.Builder()
            .dynamicConfig(dynamicConfig)
            .enableFPS(fpsEnable)
            .enableEvilMethodTrace(traceEnable)// 慢方法
            .enableAnrTrace(traceEnable) //anr
            .enableStartup(traceEnable)// 启动速度
            .enableIdleHandlerTrace(traceEnable) // Introduced in Matrix 2.0
            .enableSignalAnrTrace(signalAnrTraceEnable) // Introduced in Matrix 2.0
//            .anrTracePath(anrTraceFile.absolutePath).printTracePath(printTraceFile.absolutePath)
            .isDebug(false)
            .isDevEnv(false)
            .build()

        //Another way to use SignalAnrTracer separately
        //useSignalAnrTraceAlone(anrTraceFile.getAbsolutePath(), printTraceFile.getAbsolutePath());
        return TracePlugin(traceConfig)
    }
}