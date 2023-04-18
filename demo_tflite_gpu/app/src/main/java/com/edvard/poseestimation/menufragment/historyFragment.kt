package com.edvard.poseestimation.menufragment

import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.edvard.poseestimation.R
import com.edvard.poseestimation.data.history
import com.edvard.poseestimation.data.user_DB

class historyFragment : Fragment() {

    internal lateinit var db: user_DB
    internal var user:List<history> = ArrayList<history>()
    lateinit var listhistory: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_history, container, false)

        val tmp_data = activity.getSharedPreferences("template_data", Activity.MODE_PRIVATE)
        var back = root.findViewById<Button>(R.id.back_to_user_in_history)

        db = user_DB(activity)
        listhistory = root.findViewById<ListView>(R.id.list_history)

        val spinner = root.findViewById<View>(R.id.project_spinner) as Spinner
        val project_name = arrayListOf("全部", "度數", "黃斑部病變", "色弱", "散光", "老花眼", "對比度")
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, project_name)
        spinner.adapter = adapter
        spinner.setSelection(0)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                user = if(position == 0)
                    db.all_data(tmp_data.getInt("id", 0))
                else
                    db.all_project_data(project_name[position], tmp_data.getInt("id", 0))

                refresh()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        back.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val userdata = userFragment()
            trans.replace(R.id.navi_container, userdata)
            trans.commit()
        }

        return root
    }

    private fun refresh() {
        val adapter = history_adapter(activity, user)
        listhistory.adapter = adapter
    }
}