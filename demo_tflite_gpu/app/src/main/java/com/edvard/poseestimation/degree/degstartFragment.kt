package com.edvard.poseestimation.degree

import android.content.Intent
import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.edvard.poseestimation.R
import com.edvard.poseestimation.menufragment.homeFragment


/**
 * A simple [Fragment] subclass.
 * Use the [degstartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class degstartFragment : Fragment() {
    lateinit var back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_degstart, container, false)
        val button1 = root.findViewById(R.id.but1) as Button

        back = root.findViewById(R.id.back_to_user)

        button1.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val degintro = degintroFragment()
            trans.replace(R.id.navi_container, degintro)
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