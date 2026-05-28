package com.violin.features.common.network

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.violin.base.act.BaseFragment
import com.violin.fretures.common.databinding.FragmentOkhttpLearningBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class OkHttpLearningFragment : BaseFragment<FragmentOkhttpLearningBinding>() {

    private val mainHandler = Handler(Looper.getMainLooper())
    private val client = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    private var activeCall: Call? = null

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOkhttpLearningBinding {
        return FragmentOkhttpLearningBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        binding.etUrl.setText(DEFAULT_URL)
        binding.tvResult.text = buildIntroText()

        binding.btnGet.setOnClickListener {
            executeGet(normalizeUrl(binding.etUrl.text.toString()))
        }
        binding.btnPost.setOnClickListener {
            executePost(normalizeUrl(binding.etUrl.text.toString()))
        }
        binding.btnCancel.setOnClickListener {
            activeCall?.cancel()
            activeCall = null
            setLoading(false)
            binding.tvResult.text = "已取消当前请求"
        }
    }

    private fun executeGet(url: String) {
        if (url.isBlank()) {
            binding.tvResult.text = "请输入 URL"
            return
        }

        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "ViolinDemo-OkHttpLearning/1.0")
            .get()
            .build()

        executeRequest("GET", request, buildGetCode(url))
    }

    private fun executePost(url: String) {
        if (url.isBlank()) {
            binding.tvResult.text = "请输入 URL"
            return
        }

        val json = """
            {
              "source": "ViolinDemo",
              "topic": "OkHttp learning"
            }
        """.trimIndent()
        val body = json.toRequestBody(JSON_MEDIA_TYPE)
        val request = Request.Builder()
            .url(url)
            .header("User-Agent", "ViolinDemo-OkHttpLearning/1.0")
            .post(body)
            .build()

        executeRequest("POST JSON", request, buildPostCode(url, json))
    }

    private fun executeRequest(title: String, request: Request, sampleCode: String) {
        activeCall?.cancel()
        setLoading(true)
        binding.tvResult.text = "正在执行 $title：${request.url}"

        val start = System.currentTimeMillis()
        val call = client.newCall(request)
        activeCall = call
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                if (call.isCanceled()) {
                    postResult(call, "请求已取消", sampleCode)
                    return
                }
                postResult(
                    call,
                    "请求失败：${e.javaClass.simpleName}\n${e.message.orEmpty()}",
                    sampleCode
                )
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val elapsed = System.currentTimeMillis() - start
                    val bodyText = it.body?.string().orEmpty()
                    val result = buildString {
                        appendLine("请求方式：${request.method}")
                        appendLine("请求地址：${request.url}")
                        appendLine("状态码：${it.code} ${it.message}")
                        appendLine("耗时：${elapsed}ms")
                        appendLine("协议：${it.protocol}")
                        appendLine("内容类型：${it.header("Content-Type") ?: "-"}")
                        appendLine()
                        appendLine("响应头：")
                        it.headers.names().take(MAX_HEADER_COUNT).forEach { name ->
                            appendLine("$name: ${it.header(name).orEmpty()}")
                        }
                        appendLine()
                        appendLine("响应片段：")
                        appendLine(bodyText.take(MAX_RESPONSE_CHARS).ifBlank { "(无响应内容)" })
                    }
                    postResult(call, result, sampleCode)
                }
            }
        })
    }

    private fun postResult(call: Call, result: String, sampleCode: String) {
        mainHandler.post {
            if (view == null) return@post
            if (activeCall !== call) return@post
            activeCall = null
            setLoading(false)
            binding.tvResult.text = buildString {
                appendLine(result)
                appendLine()
                appendLine("本次请求对应代码：")
                append(sampleCode)
            }
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.btnGet.isEnabled = !loading
        binding.btnPost.isEnabled = !loading
        binding.btnCancel.isEnabled = loading
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
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

    override fun onDestroyView() {
        activeCall?.cancel()
        activeCall = null
        super.onDestroyView()
    }

    private fun buildIntroText(): String {
        return """
            这个页面演示 OkHttp 的几个常用动作：

            1. 创建 OkHttpClient 并设置超时
            2. 使用 Request.Builder 构建 GET 请求
            3. 使用 JSON RequestBody 构建 POST 请求
            4. 通过 enqueue 异步执行请求
            5. 取消正在执行的 Call

            输入 URL 后点击按钮开始。
        """.trimIndent()
    }

    private fun buildGetCode(url: String): String {
        return """
            val client = OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .build()

            val request = Request.Builder()
                .url("$url")
                .get()
                .build()

            client.newCall(request).enqueue(callback)
        """.trimIndent()
    }

    private fun buildPostCode(url: String, json: String): String {
        return """
            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = ${json.quoteForCode()}.toRequestBody(mediaType)

            val request = Request.Builder()
                .url("$url")
                .post(body)
                .build()

            client.newCall(request).enqueue(callback)
        """.trimIndent()
    }

    private fun String.quoteForCode(): String {
        return "\"\"\"\n$this\n\"\"\".trimIndent()"
    }

    companion object {
        private const val DEFAULT_URL = "https://postman-echo.com/get?hello=okhttp"
        private const val TIMEOUT_SECONDS = 8L
        private const val MAX_RESPONSE_CHARS = 1200
        private const val MAX_HEADER_COUNT = 12
        private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()
    }
}
