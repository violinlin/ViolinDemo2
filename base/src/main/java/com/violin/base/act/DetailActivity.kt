package com.violin.base.act

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.violin.base.R
import com.violin.base.act.dynamicres.DynamicContextWrapper
import com.violin.base.databinding.ActivityDetailLayoutBinding

class DetailActivity : BaseActivity<ActivityDetailLayoutBinding>() {
    override fun createBinding(inflater: LayoutInflater): ActivityDetailLayoutBinding {
        return ActivityDetailLayoutBinding.inflate(inflater)
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(DynamicContextWrapper(newBase))
    }
    override fun initView() {
        // 设置标题
        val title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        supportActionBar?.title = title

        // 获取Fragment类型并加载对应的Fragment
        val fragmentType = intent.getStringExtra(EXTRA_FRAGMENT_CLASSNAME) ?: ""
        loadFragment(fragmentType)
    }

    private fun loadFragment(fragmentClassName: String) {
        try {
            // 这里用 Activity 自己的 classLoader 就行
            val fragment = supportFragmentManager.fragmentFactory.instantiate(
                classLoader,
                fragmentClassName
            ).apply {
                // 如需给 Fragment 传参，可以把 Intent 的 extras 直接塞进去
                arguments = intent.extras
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            finish()
        }

    }

    companion object {
        const val EXTRA_FRAGMENT_CLASSNAME = "fragmentClassName"
        const val EXTRA_TITLE = "title"
    }


}
