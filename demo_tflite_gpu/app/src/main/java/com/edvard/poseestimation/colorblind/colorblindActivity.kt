package com.edvard.poseestimation.colorblind

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.content.Intent
import android.widget.TextView
import com.edvard.poseestimation.MainpageActivity
import com.edvard.poseestimation.R

class colorblindActivity : Activity() {
    private var cbimage: ImageView? = null
    private val disease = arrayOf("辨色能力正常","可能有紅色色盲","可能有綠色色盲","可能有紅色色弱","可能有綠色色弱")
    private val diseasenum = intArrayOf(1,0,0,0,0)
    private var weak_green = 0
    private var weak_red = 0
    private var now : Int = 0
    private var cbans = 0
    private var img1 = arrayOf(R.drawable.cb1_1,R.drawable.cb1_2,R.drawable.cb1_3,R.drawable.cb1_4)
    private var img2 = arrayOf(R.drawable.cb2_1,R.drawable.cb2_2,R.drawable.cb2_3,R.drawable.cb2_4)
    private var dice = (Math.random() * 2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colorblind)

        val edittext = findViewById<EditText>(R.id.CBans)
        edittext.visibility = View.VISIBLE
        //   val cbtext3 = findViewById<TextView>(R.id.t3)
        val button = findViewById(R.id.CBbut1) as Button
        waitbut()
        button.setOnClickListener {
            //      cbtext3.visibility = View.INVISIBLE
            if(now < 4 )
                cbtest()
            edittext.setText("")
            waitbut()
        }

    }
    fun randpick(){
        dice = (Math.random() * 2)

    }
    fun waitbut(){
        randpick()
        //   val button = findViewById(R.id.CBbut1) as Button
        cbimage = findViewById<ImageView>(R.id.ColorBlind)
        val count = findViewById<TextView>(R.id.textView11)
        if(now == 0){
            if(dice >= 0 && dice < 1)
                cbimage!!.setImageResource(img1[now])
            else
                cbimage!!.setImageResource(img2[now])
        }
        else if(now == 1){
            count.text = "第二題"
            if(dice >= 0 && dice < 1)
                cbimage!!.setImageResource(img1[now])
            else
                cbimage!!.setImageResource(img2[now])
        }
        else if(now == 2){
            count.text = "第三題"
            if(dice >= 0 && dice < 1)
                cbimage!!.setImageResource(img1[now])
            else
                cbimage!!.setImageResource(img2[now])
        }
        else if(now == 3){
            count.text = "第四題"
            if(dice >= 0 && dice < 1)
                cbimage!!.setImageResource(img1[now])
            else
                cbimage!!.setImageResource(img2[now])
        }
        else if(now == 4){
            cbimage!!.visibility = View.INVISIBLE
            cbfinish()
        }
        /*   else if(now == 5){
               val intent = Intent()
               intent.setClass(this, MainpageActivity::class.java)
               startActivity(intent)
           }*/
    }
    fun cbtest(){
        val edittext = findViewById<EditText>(R.id.CBans)
        //   edittext.visibility = View.VISIBLE
        if (now == 0) {
            if(dice >= 0 && dice < 1) {
                if (edittext.text.isNullOrEmpty()) {
                    cbans = 0
                }
                else
                    cbans = edittext.text.toString().toInt()
                if (cbans == 36) {
                    //   diseasenum[0] = 1
                    //diseasenum[1] = 0
                }
                else {
                    diseasenum[0] = 0
                    diseasenum[1] = 1
                }
            }
            else{
                if (edittext.text.isNullOrEmpty()) {
                    cbans = 0
                }
                else
                    cbans = edittext.text.toString().toInt()
                if(cbans == 68 ||cbans == 86 || cbans == 608 ||cbans == 680 || cbans == 806 || cbans == 860){
                    //    diseasenum[0] = 1
                    //diseasenum[1] = 0
                }
                else{
                    diseasenum[0] = 0
                    diseasenum[1] = 1
                }
            }
            now+=1
        }
        else if (now == 1) {
            if(dice >= 0 && dice < 1) {
                if (edittext.text.isNullOrEmpty()) {
                    cbans = 0
                }
                else
                    cbans = edittext.text.toString().toInt()
                if (cbans == 26) {}
                else if (cbans == 6) {
                    diseasenum[0] = 0
                    diseasenum[1] = 1
                }
                else if (cbans == 2) {
                    diseasenum[0] = 0
                    diseasenum[2] = 1
                }
                else {
                    diseasenum[0] = 0
                    diseasenum[1] = 1
                    diseasenum[2] = 1
                }
            }
            else{
                if (edittext.text.isNullOrEmpty()) {
                    cbans = 0
                }
                else
                    cbans = edittext.text.toString().toInt()
                if(cbans != 0){
                    diseasenum[0] = 0
                    diseasenum[1] = 1
                    diseasenum[2] = 1
                    diseasenum[3] = 1
                    diseasenum[4] = 1
                }
            }
            now+=1
        }
        else if (now == 2) {
            if(dice >= 0 && dice < 1) {
                if (edittext.text.isNullOrEmpty()) {
                    cbans = 0
                }
                else
                    cbans = edittext.text.toString().toInt()
                if (cbans != 3) {
                    diseasenum[0] = 0
                    diseasenum[3] = 1
                }
            }
            else{
                if (edittext.text.isNullOrEmpty()) {
                    cbans = 0
                }
                else
                    cbans = edittext.text.toString().toInt()
                if(cbans != 73){
                    diseasenum[0] = 0
                    diseasenum[3] = 1
                }
            }
            now+=1
        }
        else if (now == 3) {
            if(dice >= 0 && dice < 1) {
                if (edittext.text.isNullOrEmpty()) {
                    cbans = 0
                }
                else
                    cbans = edittext.text.toString().toInt()
                if (cbans != 6) {
                    diseasenum[0] = 0
                    diseasenum[4] = 1
                }
            }
            else{
                if (edittext.text.isNullOrEmpty()) {
                    cbans = 0
                }
                else
                    cbans = edittext.text.toString().toInt()
                if(cbans == 6){
                }
                else if(cbans == 5){
                    diseasenum[0] = 0
                    diseasenum[1] = 1
                    diseasenum[2] = 1
                    diseasenum[3] = 1
                    diseasenum[4] = 1
                }
                else{
                    diseasenum[0] = 0
                    diseasenum[3] = 1
                    diseasenum[4] = 1
                }
            }
            now+=1
        }
        else
            now+=1
        edittext.setText("")
    }
    fun cbfinish(){
        if(diseasenum[1]==0 && diseasenum[2]==0 && diseasenum[3]==0 && diseasenum[4]==0)
            diseasenum[0] = 1
        if(diseasenum[1]==1){
            diseasenum[3] = 0
            weak_red = 2
        }
        if(diseasenum[2]==1){
            diseasenum[4] = 0
            weak_green = 2
        }
        if(diseasenum[3]==1)
            weak_red = 1
        if(diseasenum[4]==1)
            weak_green = 1
        val intent = Intent()
        intent.setClass(this,MainpageActivity::class.java)

        intent.putExtra("fragment", "col")
        intent.putExtra("weak_green", weak_green)
        intent.putExtra("weak_red", weak_red)
        startActivity(intent)
    }
}