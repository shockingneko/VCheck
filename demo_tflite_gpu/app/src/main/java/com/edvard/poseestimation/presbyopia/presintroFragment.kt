package com.edvard.poseestimation.presbyopia

import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.edvard.poseestimation.R
import com.edvard.poseestimation.asti.astistartFragment

class presintroFragment : Fragment() {
    lateinit var back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_presintro, container, false)
        val button1 = root.findViewById(R.id.button) as Button

        back = root.findViewById(R.id.back_to_user)

        button1.setOnClickListener {
            val intent = Intent()
            intent.setClass(activity,presbyopiaActivity::class.java)
            startActivity(intent)
        }

        back.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val presstart = presstartFragment()
            trans.replace(R.id.navi_container, presstart)
            trans.commit()
        }

        return root
    }
}