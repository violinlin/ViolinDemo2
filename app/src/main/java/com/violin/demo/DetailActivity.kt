package com.violin.demo

import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.violin.base.act.BaseActivity
import com.violin.demo.databinding.ActivityDetailLayoutBinding
import com.violin.features.common.crash.CrashFragment
import com.violin.features.views.viewpager.ViewsFragment

class DetailActivity : BaseActivity<ActivityDetailLayoutBinding>() {
    override fun createBinding(inflater: LayoutInflater): ActivityDetailLayoutBinding {
        return ActivityDetailLayoutBinding.inflate(inflater)
    }

    override fun initView() {
        // 设置标题
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        supportActionBar?.title = title

        // 获取Fragment类型并加载对应的Fragment
        val fragmentType = intent.getStringExtra(EXTRA_FRAGMENT_TYPE) ?: ""
        loadFragment(fragmentType)
    }

    private fun loadFragment(fragmentType: String) {
        val fragment: Fragment = when (fragmentType) {
            CrashFragment.TAG -> {
                CrashFragment.newInstance()
            }

            ViewsFragment.TAG -> {
                ViewsFragment.newInstance()
            }

            else -> {
                CrashFragment.newInstance()
            }

        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    companion object {
        const val EXTRA_FRAGMENT_TYPE = "fragment_type"
        const val EXTRA_TITLE = "title"
    }


}
