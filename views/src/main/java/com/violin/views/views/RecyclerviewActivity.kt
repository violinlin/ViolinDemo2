package com.violin.views.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.violin.views.R
import com.violin.views.databinding.ActivityRecyclerviewBinding
import com.violin.views.views.adapter.RecyclerViewAdapter

class RecyclerviewActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityRecyclerviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivityRecyclerviewBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    val repeatRunnable = Runnable {
        recyclerViewAdapter?.let {
            it.notifyDataSetChanged()
            repeatNotify()
        }
    }

    private fun repeatNotify(delayTime: Long = 1000) {
        mBinding.rclView.postDelayed(repeatRunnable, delayTime)
    }

    var recyclerViewAdapter: RecyclerViewAdapter? = null
    private fun initView() {
        recyclerViewAdapter = RecyclerViewAdapter()
        mBinding.rclView.adapter = recyclerViewAdapter
        mBinding.rclView.layoutManager = LinearLayoutManager(this)
        val list = ArrayList<RecyclerViewAdapter.MyBean>()
        for (i in 0..1) {
            list.add(RecyclerViewAdapter.MyBean())
        }
        recyclerViewAdapter?.setData(list)

//        repeatNotify()
    }


    companion object {
        fun start(context: Context) {
            val starter = Intent(context, RecyclerviewActivity::class.java)
            context.startActivity(starter)
        }
    }


}