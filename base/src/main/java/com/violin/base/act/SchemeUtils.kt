package com.violin.base.act

import android.app.Activity
import android.content.Intent
import com.violin.base.act.beans.DetailActivityData

object SchemeUtils {
    fun openDetailActivity(activity: Activity, itemData: DetailActivityData) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_FRAGMENT_CLASSNAME, itemData.fragmentClassName)
            putExtra(DetailActivity.EXTRA_TITLE, itemData.title)
        }
        activity.startActivity(intent)
    }
}