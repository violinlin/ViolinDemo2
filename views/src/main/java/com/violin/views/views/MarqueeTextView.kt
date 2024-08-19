package com.violin.views.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
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
import com.violin.base.act.LogUtil
import com.violin.views.R
import java.lang.reflect.Field

class MarqueeTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

    init {

        setOnClickListener {
            val list = arrayListOf("", "")
            val text = "".addSpan("")
            for (item in list) {
//                text.addSpan("¥39.9 1000+ 人付款 ")
//                    .replaceSpan("¥39.9 1000+ 人付款 ") {
//                        ColorSpan(Color.BLUE)
//                    }
//                    .addSpan(
//                        "icon".setSpan(
//                            GlideImageSpan(
//                                this,
//                                "https://avatars.githubusercontent.com/u/21078112?v=4"
//                            )
//                                .setRequestOption(RequestOptions.circleCropTransform()) // 圆形裁剪图片
//                                .setAlign(GlideImageSpan.Align.BOTTOM)
//                                .setDrawableSize(-1, -1)
//                        )
//                    )
                addString()


            }


//            this.text = text

        }


    }

    @SuppressLint("StringFormatMatches")
    private fun addString() {
        val pic = arrayOf(
            "https://img01.mehiya.com/img/png/id/232939151016",
            "https://avatars.githubusercontent.com/u/21078112?v=4"
        )
        val placeholder = arrayOf("[name]", "[icon]", "[count]", "[times]")
        val span = "".addSpan("")
        for (item in pic) {
            val string = context.getString(
                R.string.Hiya_lucky_gift_reward2,
                placeholder
            )
//        val array = string.split(Regex("%\\d?\\\$?s"))
//        LogUtil.d("MainActivity", "array:" + array)
            span.addSpan(string)

            val nickName = "mynickname"
            span.replaceSpan("[name]") {
                nickName.replaceSpan(nickName){
                    ColorSpan("#FFDA36")
                }
            }.replaceSpan(
                "[icon]"
            ) {
                GlideImageSpan(this, item)
                    .setAlign(GlideImageSpan.Align.BOTTOM)
                    .setRequestOption(RequestOptions.circleCropTransform())
                    .setDrawableSize(50)
            }
        }

        text = span


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