package com.edvard.poseestimation.menufragment

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.edvard.poseestimation.R
import com.edvard.poseestimation.data.history

class history_adapter(internal var activity: Activity,
                      internal var tmphistory: List<history>): BaseAdapter() {

    internal var inflater: LayoutInflater

    init{
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row: View
        row = inflater.inflate(R.layout.history_adapter, null)

        val project = row.findViewById<TextView>(R.id.txt_project)
        val time = row.findViewById<TextView>(R.id.txt_time)
        val result = row.findViewById<TextView>(R.id.txt_result)

        project.text = tmphistory[position].project
        time.text = tmphistory[position].time
        result.text = tmphistory[position].result

        return row
    }

    override fun getItem(position: Int): Any {
        return tmphistory[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return tmphistory.size
    }
}