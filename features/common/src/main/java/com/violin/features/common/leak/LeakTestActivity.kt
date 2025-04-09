package com.violin.features.common.leak

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.violin.fretures.common.databinding.ActivityLeakTestBinding
import java.lang.ref.WeakReference

class LeakTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeakTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeakTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.root.setOnClickListener {
            finish()
        }
        LeakTest()
        test()
        test1(this)
        test3()
        instance = WeakReference(this)
    }

    private var instance: WeakReference<LeakTestActivity>? = null
    private fun test() {
        binding.root.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                instance?.get()?.finish()
//                finish()
            }

        })
    }

    private fun test1(activity:LeakTestActivity) {
        binding.root.setOnClickListener(object : View.OnClickListener {
            private var instance: WeakReference<LeakTestActivity>? = null
            override fun onClick(v: View?) {
                instance?.get()?.finish()
            }

        })
    }

    private fun test2() {
        binding.root.setOnClickListener { finish() }
    }

    private fun test3() {
        binding.root.setOnClickListener { instance?.get()?.finish() }
    }


    override fun onDestroy() {
        Log.d("Leak", "activity onDestroy")
        super.onDestroy()
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, LeakTestActivity::class.java)
            context.startActivity(starter)
        }
    }
}