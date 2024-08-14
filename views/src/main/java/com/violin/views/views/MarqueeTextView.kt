package com.violin.views.views

import android.content.Context
import android.graphics.Rect
import android.text.style.AbsoluteSizeSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.request.RequestOptions
import com.drake.spannable.addSpan
import com.drake.spannable.replaceSpan
import com.drake.spannable.replaceSpanFirst
import com.drake.spannable.setSpan
import com.drake.spannable.span.ColorSpan
import com.drake.spannable.span.GlideImageSpan
import java.lang.reflect.Field

class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {
        val mytext = text.toString()
        setOnClickListener {
            this.text = "头像".setSpan(
                GlideImageSpan(this, "https://avatars.githubusercontent.com/u/21078112?v=4")
                    .setRequestOption(RequestOptions.circleCropTransform()) // 圆形裁剪图片
                    .setAlign(GlideImageSpan.Align.BOTTOM)
                    .setDrawableSize(50)
            ).addSpan("¥39.9 1000+ 人付款 ")
                .replaceSpan("¥[\\d\\.]+".toRegex()) { // 匹配价格颜色(包含货币符号)
                    ColorSpan("#479fd1")
                }.replaceSpanFirst("[\\d\\.]+".toRegex()) { // 匹配价格字号
                    AbsoluteSizeSpan(18, true)
                }.addSpan("fdsfffffffffffffffffffffffffffffffffffffffffffffffffffffffffsdfsdfdsf")

        }


    }

    override fun isFocused(): Boolean {
        return true
    }

    override fun onFocusChanged(
        focused: Boolean, direction: Int,
        previouslyFocusedRect: Rect?
    ) {
        if (focused) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect)
        }
    }

    override fun onWindowFocusChanged(focused: Boolean) {
        if (focused) {
            super.onWindowFocusChanged(focused)
        }
    }

    /**
     * 利用反射 设置跑马灯的速度
     * 在onLayout中调用即可
     *
     * @param newSpeed 新的速度
     */
    fun setMarqueeSpeed(newSpeed: Float) {
        try {
            // 获取走马灯配置对象
            val tvClass = Class.forName("android.widget.TextView")
            val marqueeField: Field = tvClass.getDeclaredField("mMarquee")
            marqueeField.isAccessible = true
            val marquee: Any = marqueeField.get(this) ?: return
            // 设置新的速度
            val marqueeClass: Class<*> = marquee.javaClass
            // 速度变量的名称可能与此示例的不相同 可自行打印查看
            for (field in marqueeClass.declaredFields) {
            }
            // SDK中的是mPixelsPerMs，但我的开发机是下面的名称
            val speedField: Field =
                marqueeClass.getDeclaredField("mPixelsPerSecond") //低版本：mScrollUnit
            speedField.setAccessible(true)
            val orgSpeed = speedField.get(marquee) as Float
            // 这里设置了相对于原来的20倍
            speedField.set(marquee, newSpeed)
            // Log.i("SpanTextView", "setMarqueeSpeed: " + orgSpeed);
            //  Log.i("SpanTextView", "setMarqueeSpeed: " + newSpeed);
        } catch (e: ClassNotFoundException) {
        } catch (e: NoSuchFieldException) {
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}