package com.edvard.poseestimation.presbyopia

import android.app.Activity
import android.os.Bundle
import android.app.Fragment
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

class presfinishFragment : Fragment() {

    internal lateinit var db: user_DB
    internal var user:List<history> = ArrayList<history>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_presfinish, container, false)

        db = user_DB(activity)

        val bundle = arguments
        val first = bundle.getInt("first", 2)
        val second = bundle.getInt("second", 2)

        val left = root.findViewById<ImageView>(R.id.lefteye)
        val right = root.findViewById<ImageView>(R.id.righteye)
        val result = root.findViewById<TextView>(R.id.tv2)
        val las = root.findViewById<TextView>(R.id.tv3)
        val ras = root.findViewById<TextView>(R.id.tv4)
        val recommand = root.findViewById<TextView>(R.id.textView22)

        val result_string = db.calling_other_result(search_other_result(
                "老花眼",
                -1,
                first,
                second,
                ""
        ))
        result.text = result_string.result

        if(first == 0 && second == 0){
            left.setImageResource(R.drawable.asti_red)
            las.setVisibility(View.VISIBLE)
            right.setImageResource(R.drawable.asti_red)
            ras.setVisibility(View.VISIBLE)
        }
        else if(first == 0 && second == 1){
            left.setImageResource(R.drawable.asti_green)
            right.setImageResource(R.drawable.asti_red)
            ras.setVisibility(View.VISIBLE)
        }
        else if(first == 1 && second == 0){
            left.setImageResource(R.drawable.asti_red)
            las.setVisibility(View.VISIBLE)
            right.setImageResource(R.drawable.asti_green)
        }
        else if(first == 1 && second == 1){
            left.setImageResource(R.drawable.asti_green)
            right.setImageResource(R.drawable.asti_green)
            recommand.visibility = View.INVISIBLE
            result.setTextColor(-0xB66A79)
        }

        val history = history(
                activity.getSharedPreferences("template_data", Activity.MODE_PRIVATE).getInt("id", 0),
                "老花眼",
                result_string.result_id
        )
        db.add_data(history)

        val button = root.findViewById<Button>(R.id.bt)
        button.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val home = homeFragment()
            trans.replace(R.id.navi_container, home)
            trans.commit()
        }

        return root
    }
}