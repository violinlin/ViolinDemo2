package com.violin.views.views

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.violin.views.R
import org.libpag.PAGFile
import org.libpag.PAGView

class ViewActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val pagview = findViewById<PAGView>(R.id.pagview)
        pagview.composition = PAGFile.Load(assets, "piaopingdiban.pag")
        pagview.setRepeatCount(-1)
        pagview.play()
        val animlist = arrayOf(
            "anim_lucky_gift_level_3.pag",
            "anim_lucky_gift_level_4.pag",
            "bg03_bmp.pag",
            "bg04_bmp.pag",
            "half_bmp.pag"
        )
        pagview.setOnClickListener {
            count++
            pagview.composition = PAGFile.Load(assets, animlist[count % animlist.size])
            Log.d("pagView:", "duration" + pagview.composition.duration())
            pagview.setRepeatCount(-1)
            pagview.play()

        }
        initViewFlipper()


    }

    private fun initViewFlipper() {
        val view_flipper = findViewById<ViewFlipper>(R.id.view_flipper)
        val tv2 = findViewById<TextView>(R.id.tv2)
//        val view1 = TextView(this)
//        view1.text = "111111111"
//        view1.setBackgroundColor(Color.RED)
//        view_flipper.addView(view1)
//
//        val view2 = TextView(this)
//        view2.text = "2222222222"
//        view2.setBackgroundColor(Color.BLUE)
//        view_flipper.addView(view2)


        view_flipper.flipInterval = 3 * 1000
        view_flipper.startFlipping()
        view_flipper.setOnClickListener {
            if (tv2.visibility != View.VISIBLE) {
                tv2.visibility = View.VISIBLE
                view_flipper.startFlipping()
            } else {
                tv2.visibility = View.GONE

                val dis = view_flipper.displayedChild
                if (dis != 0) {
                    view_flipper.showNext()
                }

                view_flipper.stopFlipping()
            }

        }


    }


    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, ViewActivity::class.java)
            context.startActivity(starter)
        }
    }
}