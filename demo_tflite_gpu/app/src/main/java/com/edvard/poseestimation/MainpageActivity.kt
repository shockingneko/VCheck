package com.edvard.poseestimation

import android.app.Activity
import android.app.Fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.BottomNavigationView
import com.edvard.poseestimation.asti.astifinishFragment
import com.edvard.poseestimation.colorblind.colfinishFragment
import com.edvard.poseestimation.contrast.contfinishFragment
import com.edvard.poseestimation.degree.degfinishFragment
import com.edvard.poseestimation.macular.macufinishFragment
import com.edvard.poseestimation.menufragment.afterloginFragment
import com.edvard.poseestimation.menufragment.homeFragment
import com.edvard.poseestimation.menufragment.userFragment
import com.edvard.poseestimation.presbyopia.presfinishFragment

class MainpageActivity : Activity() {

    override fun onBackPressed() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mainpage)

        val user = userFragment()
        val home = homeFragment()

        val jump_fragment = this.intent.getStringExtra("fragment")

        if(jump_fragment == "asti"){
            val asti_result = astifinishFragment()
            var bundle = Bundle()
            bundle.putInt("first", this.intent.getIntExtra("first", 2))
            bundle.putInt("second", this.intent.getIntExtra("second", 2))
            asti_result.arguments = bundle
            setcurrentfragment(asti_result)
        }
        else if(jump_fragment == "macu"){
            val macu_result = macufinishFragment()
            var bundle = Bundle()
            bundle.putInt("first", this.intent.getIntExtra("first", 2))
            bundle.putInt("second", this.intent.getIntExtra("second", 2))
            macu_result.arguments = bundle
            setcurrentfragment(macu_result)
        }
        else if(jump_fragment == "pres"){
            val pres_result = presfinishFragment()
            var bundle = Bundle()
            bundle.putInt("first", this.intent.getIntExtra("first", 2))
            bundle.putInt("second", this.intent.getIntExtra("second", 2))
            pres_result.arguments = bundle
            setcurrentfragment(pres_result)
        }
        else if(jump_fragment == "deg"){
            val deg_result = degfinishFragment()
            var bundle = Bundle()
            bundle.putInt("eye", this.intent.getIntExtra("eye", 0))
            bundle.putDouble("score", this.intent.getDoubleExtra("score", 1.2))
            deg_result.arguments = bundle
            setcurrentfragment(deg_result)
        }
        else if(jump_fragment == "cont"){
            val cont_result = contfinishFragment()
            var bundle = Bundle()
            bundle.putInt("answer", this.intent.getIntExtra("answer", 0))
            cont_result.arguments = bundle
            setcurrentfragment(cont_result)
        }
        else if(jump_fragment == "col"){
            val col_result = colfinishFragment()
            var bundle = Bundle()
            bundle.putInt("weak_red", this.intent.getIntExtra("weak_red", 3))
            bundle.putInt("weak_green", this.intent.getIntExtra("weak_green", 3))
            col_result.arguments = bundle
            setcurrentfragment(col_result)
        }
        else{
            if(this.intent.getBooleanExtra("start", false)){
                val after_login = afterloginFragment()
                setcurrentfragment(after_login)
                start.start()
            }
            else {
                setcurrentfragment(home)
            }
        }

        val navi = findViewById<BottomNavigationView>(R.id.navigationView)
        navi.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    start.cancel()
                    setcurrentfragment(home)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    start.cancel()
                    setcurrentfragment(user)
                    return@setOnNavigationItemSelectedListener true
                }
            }
        }
    }

    private fun setcurrentfragment(fragment: Fragment) {
        val trans = fragmentManager.beginTransaction()
        trans.replace(R.id.navi_container, fragment)
        trans.commit()
    }

    val start = object : CountDownTimer(2000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
        }

        override fun onFinish() {
            val home = homeFragment()
            setcurrentfragment(home)
        }
    }
}
