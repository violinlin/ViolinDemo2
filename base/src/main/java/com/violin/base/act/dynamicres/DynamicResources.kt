package com.violin.base.act.dynamicres

import android.content.res.Resources
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class DynamicResources(base: Resources) :
    Resources(base.assets, base.displayMetrics, base.configuration) {

    override fun getText(id: Int): CharSequence {
        val default = super.getText(id).toString()
        return decodeIfBase64(default)
    }

    override fun getText(id: Int, def: CharSequence?): CharSequence? {
        val default = super.getText(id,def).toString()
        return decodeIfBase64(default)
    }

    override fun getString(id: Int): String {
        val default = super.getString(id)
        return decodeIfBase64(default)
    }

    private fun decodeIfBase64(raw: String): String {
        return if (raw.startsWith("base64_")) {
            decodeBase64(raw.substring("base64_".length))
        } else {
            raw
        }
    }
    @OptIn(ExperimentalEncodingApi::class)
    fun decodeBase64(encoded: String): String {
        try {
            // Base64.decode 返回 ByteArray
            val bytes = Base64.decode(encoded)
            // 转成字符串
            return String(bytes, Charsets.UTF_8)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""

    }

    override fun getString(id: Int, vararg formatArgs: Any): String {
        val str = getString(id)
        return if (formatArgs.isNotEmpty()) String.format(str, *formatArgs) else str
    }
}