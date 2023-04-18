package com.edvard.poseestimation.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import com.edvard.poseestimation.MainpageActivity
import com.edvard.poseestimation.R
import com.edvard.poseestimation.data.user_DB


class login : Activity() {

    internal lateinit var db: user_DB

    override fun onBackPressed() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val del_data = getSharedPreferences("template_data", MODE_PRIVATE)
        val editor = del_data.edit()
        editor.clear().apply()
        db = user_DB(this)

        if(!db.check_result_data()){
            db.degree_result_data()
            db.colorblind_result_data()
            db.other_result_data()
        }

        val intent = this.intent
        if(intent.getBooleanExtra("start",true)){
            setContentView(R.layout.activity_firstin)

            object : CountDownTimer(3000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                }

                override fun onFinish() {
                    setContentView(R.layout.activity_login)
                    going()
                }
            }.start()
        }
        else{
            setContentView(R.layout.activity_login)
            going()
        }
    }

    fun going() {
        val gotomainpage = findViewById<Button>(R.id.login)
        val gotoregisterpage = findViewById<Button>(R.id.register)
        val forget_password = findViewById<Button>(R.id.forget_password)
        val IMG = findViewById<ImageView>(R.id.login_img)
        val password = findViewById<EditText>(R.id.login_Password)
        val notice = findViewById<TextView>(R.id.login_password_notice)
        val tmp_data = getSharedPreferences("template_data", MODE_PRIVATE)
        val editor = tmp_data.edit()
        val intent = Intent()
        var tmp_position = 0

        var people = db.allpeoplename
        val spinner = findViewById<View>(R.id.login_username) as Spinner

        var adapter = login_adapter(this, people)
        spinner.adapter = adapter
        spinner.setSelection(0)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if(people.isNotEmpty()){
                    IMG.setImageResource(people[position].image)
                    tmp_position = position
                    editor.putInt("id", people[tmp_position].id).apply()
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        gotomainpage.setOnClickListener {
            if(tmp_data.getInt("id", -1) == -1) {
                Toast.makeText(this, "無使用者!", Toast.LENGTH_SHORT).show()
            }
            else if(password.text.toString() != people[tmp_position].password){
                notice.visibility = View.VISIBLE
            }
            else{
                editor.putInt("position", tmp_position)
                editor.putString("name", people[tmp_position].name)
                editor.putInt("image", people[tmp_position].image)
                editor.putInt("height", people[tmp_position].height)
                editor.putString("password", people[tmp_position].password)
                editor.apply()
                Toast.makeText(this, "歡迎回來!" + tmp_data.getString("name", ""), Toast.LENGTH_LONG).show()
                intent.putExtra("start", true)
                intent.setClass(this, MainpageActivity::class.java)
                startActivity(intent)
            }

        }
        gotoregisterpage.setOnClickListener {
            intent.setClass(this, register::class.java)
            startActivity(intent)
        }
        forget_password.setOnClickListener {
            if(tmp_data.getInt("id", -1) == -1) {
                Toast.makeText(this, "無使用者!", Toast.LENGTH_SHORT).show()
            }
            else if(people.isNotEmpty()){
                intent.putExtra("id", people[tmp_position].id)
                intent.setClass(this, forget_passwordActivity::class.java)
                startActivity(intent)
            }
        }
    }
}