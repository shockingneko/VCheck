package com.edvard.poseestimation.menufragment

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.edvard.poseestimation.R

class image_adapter(internal var activity: Activity,
                    internal var image: Array<Int>): BaseAdapter() {

    internal var inflater: LayoutInflater

    init{
        inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row: View
        row = inflater.inflate(R.layout.image_adapter, null)
        var img = row.findViewById<ImageView>(R.id.image_in_adapter)
        img.setImageResource(image[position])
        return row
    }

    override fun getItem(position: Int): Any {
        return image[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return image.size
    }

}