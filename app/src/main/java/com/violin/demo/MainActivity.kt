package com.violin.demo

import android.os.Bundle
import cn.xiaochuankeji.VideoActivity
import com.violin.base.act.BaseBindingAct
import com.violin.base.act.exts.binding
import com.violin.demo.databinding.ActivityMainBinding
import com.violin.features.common.CommonActivity
import com.violin.views.views.ViewActivity

class MainActivity : BaseBindingAct<ActivityMainBinding>() {
    override val mBinding: ActivityMainBinding by binding<ActivityMainBinding>(R.layout.activity_main)
//    private val mViewModel by viewModels<MainActViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setupView() {
        mBinding.btnView.setOnClickListener {
            ViewActivity.start(this)

        }
        mBinding.btnCommon.setOnClickListener {
            CommonActivity.start(this)
        }
        mBinding.btnVideo.setOnClickListener {
            VideoActivity.start(this)
        }
    }



}