package com.violin.views.views.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.violin.views.views.InfluenceView

class RecyclerViewAdapter : Adapter<RecyclerViewAdapter.MyHolder>() {


    private val mData: ArrayList<MyBean> = ArrayList()

    fun setData(data: List<MyBean>) {
        mData.clear()
        mData.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(InfluenceView(parent.context))
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.onBind(mData[position])

    }

    inner class MyHolder(itemView: View) : ViewHolder(itemView) {
        fun onBind(bean: MyBean) {
            (itemView as? InfluenceView)?.setData(bean)
        }
    }

    data class MyBean(
        var id: Long? = null,
        var name: String? = null,//名字
        var title: String? = null,//多语言
        var medal_icon: String? = null,//勋章静态图icon
        var medal_ani: String? = null,//勋章动态图icon
        var plate_left: String? = null,//称号牌左边图
        var plate_middle: String? = null,//称号牌底板
        var plate_right_icon: String? = null,//称号牌右边静态图
        var plate_right_ani: String? = null,//称号牌右边动态图
    )
}