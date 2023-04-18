package com.edvard.poseestimation.asti

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.edvard.poseestimation.MainpageActivity
import com.edvard.poseestimation.R

class astigmatismActivity : Activity() {
    private var count = 0
    private var b1: Button? = null
    private var b2: Button? = null
    private var tv: TextView? = null

    override fun onBackPressed() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_astigmatism)

        val changeback = findViewById<ConstraintLayout>(R.id.changebackground)
        val changescene_back = findViewById<ImageView>(R.id.changescene_back)
        val changescene = findViewById<TextView>(R.id.changescene)
        val image = findViewById<ImageView>(R.id.imageView)
        var first = 0
        var second = 0
        b1 = findViewById(R.id.button2)
        b2 = findViewById(R.id.button3)
        tv = findViewById(R.id.textView)
        image.setImageResource(R.drawable.asti)

        b1!!.setOnClickListener {
            if (count == 0){
                first = 0
                count++
                tv!!.visibility = View.INVISIBLE
                b1!!.visibility = View.INVISIBLE
                b2!!.visibility = View.INVISIBLE
                image.visibility = View.INVISIBLE
                changeback.background = resources.getDrawable(R.drawable.ltorbackground)
                changescene_back.visibility = View.VISIBLE
                changescene.visibility = View.VISIBLE
                Handler().postDelayed({
                    tv!!.text = "請覆蓋右眼，用左眼觀察線條。"
                    tv!!.visibility = View.VISIBLE
                    b1!!.visibility = View.VISIBLE
                    b2!!.visibility = View.VISIBLE
                    image.visibility = View.VISIBLE
                    changescene_back.visibility = View.INVISIBLE
                    changescene.visibility = View.INVISIBLE
                    changeback.background = resources.getDrawable(R.drawable.testbackground)
                },1500)
            }
            else{
                second = 0
                val intent = Intent()
                intent.setClass(this, MainpageActivity::class.java)
                intent.putExtra("fragment", "asti")
                intent.putExtra("first" , first)
                intent.putExtra("second" , second)
                startActivity(intent)
            }
        }
        b2!!.setOnClickListener {
            if (count == 0){
                first = 1
                count++
                tv!!.visibility = View.INVISIBLE
                b1!!.setVisibility(View.INVISIBLE)
                b2!!.setVisibility(View.INVISIBLE)
                image.setVisibility(View.INVISIBLE)
                changeback.background = resources.getDrawable(R.drawable.ltorbackground)
                changescene_back.visibility = View.VISIBLE
                changescene.visibility = View.VISIBLE

                Handler().postDelayed({
                    tv!!.text = "請覆蓋右眼，用左眼觀察線條。"
                    tv!!.visibility = View.VISIBLE
                    b1!!.setVisibility(View.VISIBLE)
                    b2!!.setVisibility(View.VISIBLE)
                    image.setVisibility(View.VISIBLE)
                    changescene_back.visibility = View.INVISIBLE
                    changescene.visibility = View.INVISIBLE
                    changeback.background = resources.getDrawable(R.drawable.testbackground)
                },1500)
            }
            else{
                second = 1
                val intent = Intent()
                intent.setClass(this, MainpageActivity::class.java)
                intent.putExtra("fragment", "asti")
                intent.putExtra("first" , first)
                intent.putExtra("second" , second)
                startActivity(intent)
            }
        }
    }
}