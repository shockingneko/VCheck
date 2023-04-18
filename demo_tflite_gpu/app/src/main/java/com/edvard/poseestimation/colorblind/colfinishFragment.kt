package com.edvard.poseestimation.colorblind

import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.edvard.poseestimation.R
import com.edvard.poseestimation.data.history
import com.edvard.poseestimation.data.search_colorblind_result
import com.edvard.poseestimation.data.user_DB
import com.edvard.poseestimation.menufragment.homeFragment

class colfinishFragment : Fragment() {

    internal lateinit var db: user_DB
    internal var user:List<history> = ArrayList<history>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_colfinish, container, false)

        db = user_DB(activity)

        val bundle = arguments
        val weak_red = bundle.getInt("weak_red")
        val weak_green = bundle.getInt("weak_green")
        val result_text = root.findViewById<TextView>(R.id.cbt1)

        val result_string = db.calling_colorblind_result(search_colorblind_result(
                -1,
                weak_red,
                weak_green,
                ""
        ))
        var tmp_string: String
        if(weak_red == 0 && weak_green == 0){
            tmp_string = "您的辨色能力正常"
        }
        else{
            tmp_string = "您"
            if(weak_green == 1){
                tmp_string += "\n可能有綠色色弱"
            }
            else if(weak_green == 2){
                tmp_string += "\n可能有綠色色盲"
            }

            if(weak_red == 1){
                tmp_string += "\n可能有紅色色弱"
            }
            else if(weak_red == 2){
                tmp_string += "\n可能有紅色色盲"
            }
        }

        result_text.text = tmp_string

        val history = history(
                activity.getSharedPreferences("template_data", Activity.MODE_PRIVATE).getInt("id", 0),
                "色弱",
                result_string.result_id
        )
        db.add_data(history)

        val button = root.findViewById(R.id.cbbut1) as Button
        button.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val home = homeFragment()
            trans.replace(R.id.navi_container, home)
            trans.commit()
        }

        return root
    }

}