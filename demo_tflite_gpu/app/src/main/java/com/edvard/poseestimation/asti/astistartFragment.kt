package com.edvard.poseestimation.asti

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.edvard.poseestimation.R
import com.edvard.poseestimation.degree.degintroFragment
import com.edvard.poseestimation.menufragment.homeFragment
import com.edvard.poseestimation.menufragment.userFragment

class astistartFragment : Fragment() {
    lateinit var back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_astistart, container, false)
        val button1 = root.findViewById(R.id.but1) as Button

        back = root.findViewById(R.id.back_to_user)

        button1.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val astiintro = astiintroFragment()
            trans.replace(R.id.navi_container, astiintro)
            trans.commit()
        }

        back.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val home = homeFragment()
            trans.replace(R.id.navi_container, home)
            trans.commit()
        }

        return root
    }
}