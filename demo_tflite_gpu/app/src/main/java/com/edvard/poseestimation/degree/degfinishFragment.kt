package com.edvard.poseestimation.degree

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
import com.edvard.poseestimation.data.search_degree_result
import com.edvard.poseestimation.data.user_DB
import com.edvard.poseestimation.menufragment.homeFragment

class degfinishFragment : Fragment() {

    internal lateinit var db: user_DB
    internal var user:List<history> = ArrayList<history>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_degfinish, container, false)

        db = user_DB(activity)

        val bundle = arguments
        var answer = bundle.getDouble("score", 1.2)
        val eye = bundle.getInt("eye", 0)

        var answer_text = root.findViewById<TextView>(R.id.answer)
        val recommand = root.findViewById<TextView>(R.id.textView9)

        val result_string = db.calling_degree_result(search_degree_result(
                -1,
                eye,
                answer,
                ""
        ))

        answer_text.text = result_string.result

        if(answer in 0.0..0.6){
            recommand.text = "建議您尋求專業醫師的幫助"
            recommand.setTextColor(-0x00DCDD)
        }
        else if(answer > 0.6 && answer <= 0.8){
            recommand.text = "建議您定時進行視力檢查追蹤"
            recommand.setTextColor(-0x096CCA)
        }
        else{
            recommand.text = "恭喜您！請繼續保持良好的視力！"
            recommand.setTextColor(-0xDD8D9D)
        }

        val history = history(
                activity.getSharedPreferences("template_data", Activity.MODE_PRIVATE).getInt("id", 0),
                "度數",
                result_string.result_id
        )
        db.add_data(history)

        val button = root.findViewById(R.id.but1) as Button
        button.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val home = homeFragment()
            trans.replace(R.id.navi_container, home)
            trans.commit()
        }
        return root
    }
}