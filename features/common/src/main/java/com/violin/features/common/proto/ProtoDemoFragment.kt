package com.violin.features.common.proto

import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.protobuf.InvalidProtocolBufferException
import com.violin.base.act.BaseFragment
import com.violin.fretures.common.databinding.FragmentProtoDemoBinding

/**
 * 演示：ProtoBuf 中使用 map<string, string> 存储键值对，
 * 并在 Android 中进行序列化 / 反序列化。
 */
class ProtoDemoFragment : BaseFragment<FragmentProtoDemoBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProtoDemoBinding {
        return FragmentProtoDemoBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.btnSerialize.setOnClickListener {
            demoProtoMap()
        }
    }

    private fun demoProtoMap() {
        // 1. 构造一个包含若干 key-value 的消息
        val original = KeyValueMap.newBuilder()
            .putEntries("user_id", "12345")
            .putEntries("user_name", "Alice")
            .putEntries("token", "abcdefg")
            .build()

        // 2. 序列化成字节数组（可以写入文件 / 通过网络发送等）
        val bytes = original.toByteArray()


        // 3. 从字节数组反序列化回来
        val parsed: KeyValueMap = try {
            KeyValueMap.parseFrom(bytes)
        } catch (e: InvalidProtocolBufferException) {
            e.printStackTrace()
            binding.tvResult.text = "解析失败: ${e.message}"
            return
        }
        parsed.entriesMap.get("")

        // 4. 把原始 map 和 解析后的 map 展示出来
        val sb = StringBuilder()
        sb.appendLine("=== 原始构建的 KeyValueMap ===")
        original.entriesMap.forEach { (k, v) ->
            sb.appendLine("$k = $v")
        }

        sb.appendLine()
        sb.appendLine("=== 反序列化后的 KeyValueMap ===")
        parsed.entriesMap.forEach { (k, v) ->
            sb.appendLine("$k = $v")
        }

        binding.tvResult.text = sb.toString()
    }
}

