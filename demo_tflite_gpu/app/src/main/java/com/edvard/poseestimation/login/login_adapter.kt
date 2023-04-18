package com.edvard.poseestimation.login

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.edvard.poseestimation.R
import com.edvard.poseestimation.data.personal

class login_adapter(internal var activity: Activity,
                    internal var tmpperson:List<personal>): BaseAdapter() {

    internal var inflater: LayoutInflater

    init{
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val row: View
        row = inflater.inflate(R.layout.login_adapter, null)

        var name = row.findViewById<TextView>(R.id.username_in_adapter)
        name.text = tmpperson[position].name

        return row
    }

    override fun getItem(position: Int): Any {
        return tmpperson[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return tmpperson.size
    }
}