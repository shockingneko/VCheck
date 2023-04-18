package com.edvard.poseestimation.menufragment

import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.edvard.poseestimation.R
import com.edvard.poseestimation.asti.astistartFragment
import com.edvard.poseestimation.data.personal
import com.edvard.poseestimation.data.user_DB

class change_passwordFragment : Fragment() {
    lateinit var back: Button
    internal lateinit var db: user_DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_change_password, container, false)

        db = user_DB(activity)
        val tmp_data = activity.getSharedPreferences("template_data", Activity.MODE_PRIVATE)
        val question = root.findViewById<TextView>(R.id.question)
        val answer = root.findViewById<EditText>(R.id.answer)
        val next = root.findViewById<Button>(R.id.next)
        val wrong = root.findViewById<TextView>(R.id.wrong_answer)
        val password = root.findViewById<EditText>(R.id.Password)
        val password_check = root.findViewById<EditText>(R.id.Password_check)
        val password_notice = root.findViewById<TextView>(R.id.password_notice)
        val password_check_notice = root.findViewById<TextView>(R.id.password_check_notice)
        var button_click_counter = 0
        var id = tmp_data.getInt("id", -1)
        var tmp_person = db.getperson(id)

        back = root.findViewById(R.id.back_to_user)

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

                    Toast.makeText(activity, "更改成功!", Toast.LENGTH_SHORT).show()
                    val trans = fragmentManager.beginTransaction()
                    val user = userFragment()
                    trans.replace(R.id.navi_container, user)
                    trans.commit()
                }
            }
        }

        back.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val userdata = userdataFragment()
            trans.replace(R.id.navi_container, userdata)
            trans.commit()
        }

        return root
    }
}