package com.edvard.poseestimation.menufragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.edvard.poseestimation.*
import com.edvard.poseestimation.asti.astistartFragment
import com.edvard.poseestimation.colorblind.colstartFragment
import com.edvard.poseestimation.contrast.contstartFragment
import com.edvard.poseestimation.degree.degstartFragment
import com.edvard.poseestimation.macular.macustartFragment
import com.edvard.poseestimation.presbyopia.presstartFragment


class homeFragment : Fragment() {
    var degree: ImageButton? = null
    var macular: ImageButton? = null
    var colorblind: ImageButton? = null
    var asti: ImageButton? = null
    var pres: ImageButton? = null
    var cont: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        degree = root.findViewById<ImageButton>(R.id.eyedegree)
        macular = root.findViewById<ImageButton>(R.id.macular)
        colorblind = root.findViewById<ImageButton>(R.id.colorblind)
        asti = root.findViewById<ImageButton>(R.id.asti)
        pres = root.findViewById<ImageButton>(R.id.prebyopia)
        cont = root.findViewById<ImageButton>(R.id.constrast)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        degree?.setOnClickListener {
            val degstart = degstartFragment()
            setcurrentfragment(degstart)
        }
        macular?.setOnClickListener {
            val macular = macustartFragment()
            setcurrentfragment(macular)
        }
        colorblind?.setOnClickListener {
            val colorblind = colstartFragment()
            setcurrentfragment(colorblind)
        }
        asti?.setOnClickListener {
            val asti = astistartFragment()
            setcurrentfragment(asti)
        }
        pres?.setOnClickListener {
            val pres = presstartFragment()
            setcurrentfragment(pres)
        }
        cont?.setOnClickListener {
            val cons = contstartFragment()
            setcurrentfragment(cons)
        }
    }

    private fun setcurrentfragment(fragment: Fragment) {
        val trans = fragmentManager.beginTransaction()
        trans.replace(R.id.navi_container, fragment)
        trans.commit()
    }
}