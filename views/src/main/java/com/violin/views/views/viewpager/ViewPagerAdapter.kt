package com.violin.views.views.viewpager

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.PathInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.RecyclerView
import com.violin.views.R
import com.violin.views.views.ViewPagerActivity

class ViewPagerAdapter(private val items: List<String>) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {
    var onItemClickListener: ((position: Int) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView? = null
        var position: Int? = null

        init {
            textView = itemView.findViewById(R.id.textView)
            itemView.setOnClickListener {
                startAnim()
            }

        }

        fun setData(position: Int) {
            this.position = position
        }

        private fun startAnim() {
//            val angle = 100F
//            itemView.rotationY = angle
//            val scale = 0.6F
//            itemView.scaleX = scale
//            itemView.scaleY = scale
            // 1. 缩放动画：0.3 -> 0.9
            val distance = 8000
            val density: Float = itemView.context.getResources().getDisplayMetrics().density
            val pixelDistance: Float = distance * density
            itemView.setCameraDistance(pixelDistance)

            val inteceptScale = 0.8F
            val scaleX = ObjectAnimator.ofFloat(itemView, View.SCALE_X, 0.3f, inteceptScale)
            val scaleY = ObjectAnimator.ofFloat(itemView, View.SCALE_Y, 0.3f, inteceptScale)

            // 2. 透明度动画：0.5 -> 1
            val alpha = ObjectAnimator.ofFloat(itemView, View.ALPHA, 0.5f, 1f)

            // 3. 旋转动画：200° -> 70° (绕Y轴旋转)
            val rotation = ObjectAnimator.ofFloat(itemView, View.ROTATION_Y, 200f, 70f + 180F)

            // 创建动画集合
            var animatorSet = AnimatorSet()
            animatorSet.playTogether(scaleX, scaleY, alpha, rotation)

            // 设置动画参数
            animatorSet.duration = 200  // 200毫秒
            animatorSet.start()
            animatorSet.doOnEnd {
                val scaleX2 = ObjectAnimator.ofFloat(itemView, View.SCALE_X, inteceptScale, 1F)
                val scaleY2 = ObjectAnimator.ofFloat(itemView, View.SCALE_Y, inteceptScale, 1F)
                val rotation2 = ObjectAnimator.ofFloat(itemView, View.ROTATION_Y, 70f + 180F, 360F)
                animatorSet = AnimatorSet()
                animatorSet.duration = 600
                animatorSet.interpolator = PathInterpolator(0.33F, 1F, 0.68F, 1F)
//                animatorSet.interpolator = DecelerateInterpolator()
                animatorSet.playTogether(scaleX2, scaleY2, rotation2)
                animatorSet.start()
            }
            ViewPagerActivity.instance?.startLightAnim()
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_page, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView?.text = items[position]
        holder.setData(position)
    }
}
