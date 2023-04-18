package com.edvard.poseestimation.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import com.edvard.poseestimation.R
import com.edvard.poseestimation.data.person_total_data
import com.edvard.poseestimation.data.user_DB
import com.edvard.poseestimation.menufragment.image_adapter
import org.opencv.videoio.VideoWriter


class register : Activity() {

    internal lateinit var db: user_DB
    internal lateinit var name_notice: TextView
    internal lateinit var password_notice: TextView
    internal lateinit var password_check_notice: TextView

    override fun onBackPressed() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        db = user_DB(this)

        val finishregister = findViewById<Button>(R.id.finishregister)
        val username = findViewById<EditText>(R.id.reg_name)
        val userheight = findViewById<EditText>(R.id.reg_height)
        val password = findViewById<EditText>(R.id.Password)
        val password_check = findViewById<EditText>(R.id.Password_check)
        val question_text = findViewById<TextView>(R.id.question_text)
        val question_answer = findViewById<EditText>(R.id.input_answer)
        name_notice = findViewById(R.id.name_notice)
        password_notice = findViewById(R.id.password_notice)
        password_check_notice = findViewById(R.id.password_check_notice)
        var IMG = arrayOf(R.drawable.head1, R.drawable.head2, R.drawable.head3, R.drawable.head4, R.drawable.head5, R.drawable.head6,
                R.drawable.head7, R.drawable.head8, R.drawable.head9, R.drawable.head10, R.drawable.head11, R.drawable.head12)
        var userimage : Int = 0
        var button_click_counter = 0
        lateinit var question_tmp:String

        val back = findViewById<Button>(R.id.back_to_user)
        val spinner = findViewById<View>(R.id.spinner) as Spinner
        val adapter = image_adapter(this, IMG)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                userimage = IMG[position] //取使用者選了哪張圖
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        finishregister.setOnClickListener {
            if(button_click_counter == 0){
                if(username.text.toString() == ""){
                    Toast.makeText(this, "請輸入使用者名稱!", Toast.LENGTH_LONG).show()
                }
                else if(userheight.text.toString() == ""){
                    Toast.makeText(this, "請輸入使用者身高!", Toast.LENGTH_LONG).show()
                }
                else if(username.text.toString().contains(" ")){
                    name_space_timer.start()
                }
                else if(!db.getpersonname(username.text.toString())){
                    name_notice_timer.start()
                }
                else if(!Regex("^.*(?=.{8,12})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*\$").matches(password.text)){
                    password_notice_timer.start()
                }
                else if(password.text.toString().contains(" ")){
                    password_notice_timer2.start()
                }
                else if(password_check.text.toString() != password.text.toString()){
                    password_check_notice_timer.start()
                }
                else if(password_check.text.toString() == password.text.toString()){
                    //spinner.visibility = View.INVISIBLE
                    spinner.isClickable = false
                    username.visibility = View.INVISIBLE
                    userheight.visibility = View.INVISIBLE
                    password.visibility = View.INVISIBLE
                    password_check.visibility = View.INVISIBLE
                    password_check_notice.visibility = View.INVISIBLE
                    password_notice.visibility = View.INVISIBLE
                    question_text.visibility = View.VISIBLE
                    question_answer.visibility = View.VISIBLE
                    button_click_counter ++
                }
            }
            else if(button_click_counter == 1){
                question_tmp = question_answer.text.toString()
                question_text.text = "請輸入該問題的答案"
                question_answer.text = null
                button_click_counter ++
                finishregister.text = "註冊完成"
            }
            else{
                val person = person_total_data(
                            -1,
                            username.text.toString(),
                            userimage,
                            userheight.text.toString().toInt(),
                            password.text.toString(),
                            question_tmp,
                            question_answer.text.toString()
                    )
                    db.addperson(person)

                    Toast.makeText(this, "註冊成功!", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.putExtra("start",false)
                    intent.setClass(this, login::class.java)
                    startActivity(intent)
            }
        }

        back.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, login::class.java)
            intent.putExtra("start",false)
            startActivity(intent)
        }
    }

    val name_space_timer = object : CountDownTimer(4000, 1000) {
        override fun onFinish() {
            name_notice.visibility = View.INVISIBLE
            name_notice.text = "使用者名稱重複!"
        }

        override fun onTick(millisUntilFinished: Long) {
            name_notice.text = "名字不可有空白"
            name_notice.visibility = View.VISIBLE
        }
    }

    val name_notice_timer = object : CountDownTimer(4000, 1000) {
        override fun onFinish() {
            name_notice.visibility = View.INVISIBLE
        }

        override fun onTick(millisUntilFinished: Long) {
            name_notice.visibility = View.VISIBLE
        }
    }

    val password_notice_timer = object : CountDownTimer(4000, 1000) {
        override fun onFinish() {
            password_notice.setTextColor(Color.parseColor("#000000"))
        }

        override fun onTick(millisUntilFinished: Long) {
            password_notice.setTextColor(Color.parseColor("#ff0000"))
        }
    }

    val password_notice_timer2 = object : CountDownTimer(4000, 1000) {
        override fun onFinish() {
            password_notice.text = "*長度8-12個字，需包含英文大小寫及數字"
            password_notice.setTextColor(Color.parseColor("#000000"))
        }

        override fun onTick(millisUntilFinished: Long) {
            password_notice.text = "*密碼不可有空白!"
            password_notice.setTextColor(Color.parseColor("#ff0000"))
        }
    }

    val password_check_notice_timer = object : CountDownTimer(4000, 1000) {
        override fun onFinish() {
            password_check_notice.visibility = View.INVISIBLE
        }

        override fun onTick(millisUntilFinished: Long) {
            password_notice.setTextColor(Color.parseColor("#000000"))
            password_check_notice.visibility = View.VISIBLE
        }
    }
}