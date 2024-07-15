package com.violin.demo

import android.os.Bundle
import com.violin.base.act.BaseBindingAct
import com.violin.base.act.exts.binding
import com.violin.demo.databinding.ActivityMainBinding

class MainActivity : BaseBindingAct<ActivityMainBinding>() {
    override val mBinding: ActivityMainBinding by binding<ActivityMainBinding>(R.layout.activity_main)
//    private val mViewModel by viewModels<MainActViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setupView() {
        mBinding.btnView.setOnClickListener {

        }
    }


}