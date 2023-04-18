package com.edvard.poseestimation.contrast

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.edvard.poseestimation.MainpageActivity
import com.edvard.poseestimation.R

class contrastActivity : Activity() {
    private var digit: Int = 0

    override fun onBackPressed() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contrast)

        val buttonY = findViewById<Button>(R.id.but1) as Button
        val buttonN = findViewById<Button>(R.id.but2) as Button

        Generate_image()
        buttonY.setOnClickListener {
            digit += 1
            Generate_image()
        }
        buttonN.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, MainpageActivity::class.java)
            intent.putExtra("fragment", "cont")
            intent.putExtra("answer" , 1)
            startActivity(intent)
        }
    }

    private fun Generate_image(){
        var bmp = BitmapFactory.decodeResource(resources, R.drawable.pr1)
        val yee = findViewById(R.id.E) as ImageView
        if(digit == 0){
        }
        else if(digit == 1){
            bmp = BitmapFactory.decodeResource(resources, R.drawable.pr2)
        }
        else if(digit == 2){
            bmp = BitmapFactory.decodeResource(resources, R.drawable.pr3)
        }
        else if(digit == 3){
            bmp = BitmapFactory.decodeResource(resources, R.drawable.pr4)
        }
        else if(digit == 4){
            bmp = BitmapFactory.decodeResource(resources, R.drawable.pr5)
        }
        else if(digit == 5){
            bmp = BitmapFactory.decodeResource(resources, R.drawable.pr6)
        }
        else if(digit == 6){
            bmp = BitmapFactory.decodeResource(resources, R.drawable.pr7)
        }
        else if(digit == 7){
            bmp = BitmapFactory.decodeResource(resources, R.drawable.pr8)
        }
        else{
            val intent = Intent()
            intent.setClass(this, MainpageActivity::class.java)
            intent.putExtra("fragment", "cont")
            intent.putExtra("answer" , 0)
            startActivity(intent)
        }
        bmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
        yee.setImageBitmap(bmp)
    }
}