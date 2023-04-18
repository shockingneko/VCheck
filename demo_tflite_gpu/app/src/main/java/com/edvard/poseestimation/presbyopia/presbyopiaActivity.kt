package com.edvard.poseestimation.presbyopia

import android.content.Intent
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.edvard.poseestimation.MainpageActivity
import com.edvard.poseestimation.R

class presbyopiaActivity : Activity() {

    override fun onBackPressed() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presbyopia)

        var first = 0
        var second = 0
        var count = 0
        val changeback = findViewById<ConstraintLayout>(R.id.changebackground)
        val changescene_back = findViewById<ImageView>(R.id.changescene_back)
        val changescene = findViewById<TextView>(R.id.changescene)
        val image = findViewById<ImageView>(R.id.imageView2)
        val text = findViewById<TextView>(R.id.textView)
        val text2 = findViewById<TextView>(R.id.textView2)
        val no = findViewById(R.id.button5) as Button
        val yes = findViewById(R.id.button4) as Button

        yes.setOnClickListener {
            if (count == 0){
                first = 0
                count++
                text.visibility = View.INVISIBLE
                text2.setVisibility(View.INVISIBLE)
                no.setVisibility(View.INVISIBLE)
                yes.setVisibility(View.INVISIBLE)
                image.setVisibility(View.INVISIBLE)
                changeback.background = resources.getDrawable(R.drawable.ltorbackground)
                changescene_back.visibility = View.VISIBLE
                changescene.visibility = View.VISIBLE
                Handler().postDelayed({
                    text.text = "請覆蓋右眼，用左眼觀察線條。"
                    text.visibility = View.VISIBLE
                    text2.setVisibility(View.VISIBLE)
                    yes.setVisibility(View.VISIBLE)
                    no.setVisibility(View.VISIBLE)
                    image.setVisibility(View.VISIBLE)
                    changescene_back.visibility = View.INVISIBLE
                    changescene.visibility = View.INVISIBLE
                    changeback.background = resources.getDrawable(R.drawable.testbackground)
                },1500)
            }
            else{
                second = 0
                val intent = Intent()
                intent.setClass(this, MainpageActivity::class.java)
                intent.putExtra("fragment", "pres")
                intent.putExtra("first" , first)
                intent.putExtra("second" , second)
                startActivity(intent)
            }
        }
        no.setOnClickListener {
            if (count == 0){
                first = 1
                count++
                text.visibility = View.INVISIBLE
                text2.setVisibility(View.INVISIBLE)
                no.setVisibility(View.INVISIBLE)
                yes.setVisibility(View.INVISIBLE)
                image.setVisibility(View.INVISIBLE)
                changeback.background = resources.getDrawable(R.drawable.ltorbackground)
                changescene_back.visibility = View.VISIBLE
                changescene.visibility = View.VISIBLE
                Handler().postDelayed({
                    text.text = "請覆蓋右眼，用左眼觀察線條。"
                    text.visibility = View.VISIBLE
                    text2.setVisibility(View.VISIBLE)
                    yes.setVisibility(View.VISIBLE)
                    no.setVisibility(View.VISIBLE)
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
                intent.putExtra("fragment", "pres")
                intent.putExtra("first" , first)
                intent.putExtra("second" , second)
                startActivity(intent)
            }
        }
    }
}