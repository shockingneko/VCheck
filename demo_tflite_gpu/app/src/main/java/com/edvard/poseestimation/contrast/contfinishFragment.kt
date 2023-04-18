package com.edvard.poseestimation.contrast

import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.edvard.poseestimation.R
import com.edvard.poseestimation.data.history
import com.edvard.poseestimation.data.search_other_result
import com.edvard.poseestimation.data.user_DB
import com.edvard.poseestimation.menufragment.homeFragment

class contfinishFragment : Fragment() {

    internal lateinit var db: user_DB
    internal var user:List<history> = ArrayList<history>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_contfinish, container, false)

        db = user_DB(activity)

        val bundle = arguments
        val answer = bundle.getInt("answer", 0)

        val buttonB = root.findViewById(R.id.conbt) as Button
        val judge2 = root.findViewById<TextView>(R.id.contrast2)
        val recommand = root.findViewById<TextView>(R.id.zzz123)

        val result_string = db.calling_other_result(search_other_result(
                "對比度",
                -1,
                answer,
                answer,
                ""
        ))
        judge2.text = result_string.result

        if(answer == 1){
            recommand.visibility = View.VISIBLE
            judge2.setTextColor(-0x00F5F6)
        }

        val history = history(
                activity.getSharedPreferences("template_data", Activity.MODE_PRIVATE).getInt("id", 0),
                "對比度",
                result_string.result_id
        )
        db.add_data(history)

        buttonB.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val home = homeFragment()
            trans.replace(R.id.navi_container, home)
            trans.commit()
        }
        return root
    }

}