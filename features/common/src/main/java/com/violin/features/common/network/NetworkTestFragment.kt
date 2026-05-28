package com.violin.features.common.network

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import com.violin.base.act.BaseFragment
import com.violin.fretures.common.databinding.FragmentNetworkTestBinding
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URI
import java.net.URL

class NetworkTestFragment : BaseFragment<FragmentNetworkTestBinding>() {

    private val mainHandler = Handler(Looper.getMainLooper())

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNetworkTestBinding {
        return FragmentNetworkTestBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.etUrl.setText(DEFAULT_URL)
        binding.btnRequest.setOnClickListener {
            requestUrl(normalizeUrl(binding.etUrl.text.toString()))
        }
        binding.btnDns.setOnClickListener {
            resolveDns(normalizeUrl(binding.etUrl.text.toString()))
        }
    }

    private fun requestUrl(url: String) {
        if (url.isBlank()) {
            binding.tvResult.text = "请输入 URL"
            return
        }

        setLoading(true)
        binding.tvResult.text = "正在请求：$url"

        Thread {
            val result = runCatching {
                val start = System.currentTimeMillis()
                val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = TIMEOUT_MILLIS
                    readTimeout = TIMEOUT_MILLIS
                    instanceFollowRedirects = true
                    setRequestProperty("User-Agent", "ViolinDemo-NetworkTest/1.0")
                }

                try {
                    val code = connection.responseCode
                    val elapsed = System.currentTimeMillis() - start
                    buildString {
                        appendLine("请求地址：$url")
                        appendLine("状态码：$code ${connection.responseMessage.orEmpty()}")
                        appendLine("耗时：${elapsed}ms")
                        appendLine("内容类型：${connection.contentType ?: "-"}")
                        appendLine()
                        appendLine("响应片段：")
                        appendLine(readResponseSnippet(connection).ifBlank { "(无响应内容)" })
                    }
                } finally {
                    connection.disconnect()
                }
            }.getOrElse { throwable ->
                "请求失败：${throwable.javaClass.simpleName}\n${throwable.message.orEmpty()}"
            }

            postResult(result)
        }.start()
    }

    private fun resolveDns(url: String) {
        val host = runCatching { URI(url).host }.getOrNull()
        if (host.isNullOrBlank()) {
            binding.tvResult.text = "无法解析 URL，请检查输入"
            return
        }

        setLoading(true)
        binding.tvResult.text = "正在解析：$host"

        Thread {
            val result = runCatching {
                val start = System.currentTimeMillis()
                val addresses = InetAddress.getAllByName(host)
                val elapsed = System.currentTimeMillis() - start

                buildString {
                    appendLine("Host：$host")
                    appendLine("耗时：${elapsed}ms")
                    appendLine("解析结果：")
                    addresses.forEach { address ->
                        appendLine(address.hostAddress)
                    }
                }
            }.getOrElse { throwable ->
                "DNS 解析失败：${throwable.javaClass.simpleName}\n${throwable.message.orEmpty()}"
            }

            postResult(result)
        }.start()
    }

    private fun readResponseSnippet(connection: HttpURLConnection): String {
        val stream = if (connection.responseCode >= HTTP_BAD_REQUEST) {
            connection.errorStream
        } else {
            connection.inputStream
        } ?: return ""

        return stream.bufferedReader().use { reader ->
            val buffer = CharArray(MAX_RESPONSE_CHARS)
            val count = reader.read(buffer, 0, buffer.size)
            if (count > 0) String(buffer, 0, count) else ""
        }
    }

    private fun normalizeUrl(input: String): String {
        val url = input.trim()
        if (url.isBlank()) return ""
        return if (url.startsWith("http://") || url.startsWith("https://")) {
            url
        } else {
            "https://$url"
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.btnRequest.isEnabled = !loading
        binding.btnDns.isEnabled = !loading
        binding.progressBar.visibility = if (loading) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
    }

    private fun postResult(result: String) {
        mainHandler.post {
            if (view == null) return@post
            setLoading(false)
            binding.tvResult.text = result
        }
    }

    companion object {
        private const val DEFAULT_URL = "https://www.baidu.com"
        private const val TIMEOUT_MILLIS = 8_000
        private const val HTTP_BAD_REQUEST = 400
        private const val MAX_RESPONSE_CHARS = 1024
    }
}
