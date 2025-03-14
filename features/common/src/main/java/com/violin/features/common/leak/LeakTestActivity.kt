package com.violin.features.common.leak

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.violin.fretures.common.databinding.ActivityLeakTestBinding

class LeakTestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeakTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeakTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.main.postDelayed({
            Toast.makeText(this, "leak test", Toast.LENGTH_SHORT).show()
        }, 10 * 1000)
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, LeakTestActivity::class.java)
            context.startActivity(starter)
        }
    }
}