package com.edvard.poseestimation.login

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.edvard.poseestimation.R
import com.edvard.poseestimation.data.personal
import com.edvard.poseestimation.data.user_DB

class forget_passwordActivity : Activity() {

    internal lateinit var db: user_DB

    override fun onBackPressed() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        db = user_DB(this)
        val question = findViewById<TextView>(R.id.question)
        val answer = findViewById<EditText>(R.id.answer)
        val next = findViewById<Button>(R.id.next)
        val wrong = findViewById<TextView>(R.id.wrong_answer)
        val password = findViewById<EditText>(R.id.Password)
        val password_check = findViewById<EditText>(R.id.Password_check)
        val password_notice = findViewById<TextView>(R.id.password_notice)
        val password_check_notice = findViewById<TextView>(R.id.password_check_notice)
        val back = findViewById<Button>(R.id.back_to_user)
        var button_click_counter = 0
        var id = this.intent.getIntExtra("id", 0)
        var tmp_person = db.getperson(id)

        question.text = tmp_person.forget_question
        next.setOnClickListener {
            if(button_click_counter == 0){
                if(answer.text.toString() == tmp_person.forget_answer){
                    question.visibility = View.INVISIBLE
                    answer.visibility = View.INVISIBLE
                    wrong.visibility = View.INVISIBLE
                    next.text = "確定更改"
                    password.visibility = View.VISIBLE
                    password_check.visibility = View.VISIBLE
                    password_notice.visibility = View.VISIBLE
                    button_click_counter ++
                }
                else{
                    wrong.visibility = View.VISIBLE
                }
            }
            else{
                if(!Regex("^.*(?=.{8,12})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*\$").matches(password.text)){
                    password_notice.setTextColor(Color.parseColor("#ff0000"))
                }
                else if(password_check.text.toString() != password.text.toString()){
                    password_notice.setTextColor(Color.parseColor("#000000"))
                    password_check_notice.visibility = View.VISIBLE
                }
                else{
                    val person = personal(
                            tmp_person.id,
                            tmp_person.name.toString(),
                            tmp_person.image,
                            tmp_person.height,
                            password.text.toString()
                    )
                    db.updateperson(person)

                    Toast.makeText(this, "更改成功!", Toast.LENGTH_SHORT).show()
                    val intent = Intent()
                    intent.setClass(this, login::class.java)
                    startActivity(intent)
                }
            }
        }

        back.setOnClickListener {
            val intent = Intent()
            intent.setClass(this, login::class.java)
            intent.putExtra("start",false)
            startActivity(intent)
        }
    }

}