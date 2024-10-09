package com.violin.views.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout

class ClipLinearLayout@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr)  {

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            if (!isViewCompletelyVisible(childView)) {
                childView.visibility = View.GONE
            }

        }
    }

    fun isViewCompletelyVisible(view: View): Boolean {
        // 获取屏幕可见区域
        val rect = Rect()
        val isVisible = view.getGlobalVisibleRect(rect)

        // 获取 View 的宽高
        val viewWidth = view.width
        val viewHeight = view.height

        // 判断 View 是否可见以及是否完全显示
        return isVisible && rect.width() == viewWidth && rect.height() == viewHeight
    }
}