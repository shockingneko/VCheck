package com.edvard.poseestimation.menufragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.edvard.poseestimation.*
import com.edvard.poseestimation.data.user_DB
import com.edvard.poseestimation.login.login

class userFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_user, container, false)

        val goto_userdata = root.findViewById<Button>(R.id.userdata)
        val history = root.findViewById<Button>(R.id.history)
        val delete = root.findViewById<Button>(R.id.delete)
        val logout = root.findViewById<Button>(R.id.logout)
        val userimg = root.findViewById<ImageView>(R.id.user_image)
        val username = root.findViewById<TextView>(R.id.user_name)

        val tmp_data = activity.getSharedPreferences("template_data", Activity.MODE_PRIVATE)

        username.text = tmp_data.getString("name", "")
        userimg.setImageResource(tmp_data.getInt("image", 0))

        goto_userdata.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val userdata = userdataFragment()
            trans.replace(R.id.navi_container, userdata)
            trans.commit()
        }
        history.setOnClickListener {

            val trans = fragmentManager.beginTransaction()
            val historypage = historyFragment()
            trans.replace(R.id.navi_container, historypage)
            trans.commit()
        }
        delete.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(activity!!)
            dialogBuilder.setTitle("是否確定要註銷帳號")
            dialogBuilder.setMessage("帳號一旦註銷，將沒有任何辦法能夠找回之前的測試記錄")
            dialogBuilder.setNegativeButton("取消"){
                _, _ ->
            }
            dialogBuilder.setPositiveButton("確定"){
                _, _ ->

                val db = user_DB(activity)
                db.delete_account(tmp_data.getInt("id", 0))

                val intent = Intent()
                intent.setClass(activity, login::class.java)
                intent.putExtra("start",false)
                startActivity(intent)
            }
            dialogBuilder.show()
        }
        logout.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(activity!!)
            dialogBuilder.setTitle("是否確定要登出")
            dialogBuilder.setMessage("登出後仍可以藉由圖片直接登入您的帳戶")
            dialogBuilder.setNegativeButton("取消"){
                _, _ ->
            }
            dialogBuilder.setPositiveButton("確定"){
                _, _ ->
                val intent = Intent()
                intent.putExtra("start",false)
                intent.setClass(activity, login::class.java)
                startActivity(intent)
            }
            dialogBuilder.show()
        }

        return root
    }
}