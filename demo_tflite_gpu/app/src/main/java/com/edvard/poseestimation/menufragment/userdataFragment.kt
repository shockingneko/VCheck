package com.edvard.poseestimation.menufragment

import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.os.Build
import android.support.annotation.RequiresApi
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.edvard.poseestimation.*
import com.edvard.poseestimation.data.personal
import com.edvard.poseestimation.data.user_DB

class userdataFragment : Fragment() {

    lateinit var cancel: Button
    lateinit var complete: Button
    lateinit var edit: Button
    lateinit var name: TextView
    lateinit var spinner: Spinner
    lateinit var height: EditText
    lateinit var back: Button
    lateinit var change_password: Button
    var userimage : Int = 0

    internal lateinit var db: user_DB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_userdata, container, false)

        db = user_DB(activity)

        cancel = root.findViewById(R.id.userdata_cancel)
        complete = root.findViewById(R.id.userdata_complete)
        edit = root.findViewById(R.id.userdata_edit)
        name = root.findViewById(R.id.userdata_name)
        height = root.findViewById(R.id.userdata_height)
        back = root.findViewById(R.id.back_to_user)
        spinner = root.findViewById<View>(R.id.spinner) as Spinner
        change_password = root.findViewById(R.id.change_password)

        val tmp_data = activity.getSharedPreferences("template_data", Activity.MODE_PRIVATE)
        name.text = tmp_data.getString("name", "")
        height.setText(tmp_data.getInt("height", 0).toString())


        var IMG = arrayOf(R.drawable.head1, R.drawable.head2, R.drawable.head3, R.drawable.head4, R.drawable.head5, R.drawable.head6,
                R.drawable.head7, R.drawable.head8, R.drawable.head9, R.drawable.head10, R.drawable.head11, R.drawable.head12)
        val adapter = image_adapter(activity, IMG)
        spinner.adapter = adapter
        for(i in 0..IMG.size){
            if(tmp_data.getInt("image", 0) == IMG[i]){
                spinner.setSelection(i)
                break
            }
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                userimage = IMG[position] //取使用者選了哪張圖
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()

        cancel.setOnClickListener {
            val tmp_data = activity.getSharedPreferences("template_data", Activity.MODE_PRIVATE)
            edit.visibility = View.VISIBLE
            cancel.visibility = View.INVISIBLE
            complete.visibility = View.INVISIBLE
            spinner.isClickable = false
            height.focusable = View.NOT_FOCUSABLE
            height.setText(tmp_data.getInt("height", 0).toString())
            var IMG = arrayOf(R.drawable.head1, R.drawable.head2, R.drawable.head3, R.drawable.head4, R.drawable.head5, R.drawable.head6,
                    R.drawable.head7, R.drawable.head8, R.drawable.head9, R.drawable.head10, R.drawable.head11, R.drawable.head12)
            val adapter = image_adapter(activity, IMG)
            spinner.adapter = adapter
            for(i in 0..IMG.size){
                if(tmp_data.getInt("image", 0) == IMG[i]){
                    spinner.setSelection(i)
                    break
                }
            }
        }
        complete.setOnClickListener {

            val tmp_data = activity.getSharedPreferences("template_data", Activity.MODE_PRIVATE)
            val editor = tmp_data.edit()
            val person = personal(
                    tmp_data.getInt("id", 0),
                    name.text.toString(),
                    userimage,
                    height.text.toString().toInt(),
                    tmp_data.getString("password", "").toString()
            )
            db.updateperson(person)

            editor.putInt("image", userimage)
            editor.putInt("height", height.text.toString().toInt())
            editor.apply()

            edit.visibility = View.VISIBLE
            cancel.visibility = View.INVISIBLE
            complete.visibility = View.INVISIBLE
            spinner.isClickable = false
            height.focusable = View.NOT_FOCUSABLE
        }
        edit.setOnClickListener {
            edit.visibility = View.INVISIBLE
            cancel.visibility = View.VISIBLE
            complete.visibility = View.VISIBLE
            height.focusable = View.FOCUSABLE
            height.isFocusableInTouchMode = true
            spinner.isClickable = true
        }
        change_password.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val change_password = change_passwordFragment()
            trans.replace(R.id.navi_container, change_password)
            trans.commit()
        }

        back.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val userdata = userFragment()
            trans.replace(R.id.navi_container, userdata)
            trans.commit()
        }
    }
}