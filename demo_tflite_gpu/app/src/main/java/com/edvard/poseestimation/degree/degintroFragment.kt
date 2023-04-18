package com.edvard.poseestimation.degree

import android.app.Activity
import android.os.Bundle
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.edvard.poseestimation.R
import com.edvard.poseestimation.asti.astistartFragment


class degintroFragment : Fragment() {
    lateinit var back: Button

    private var button: Button? = null
    private var judge_label: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_degintro, container, false)

        val radiogroup = root.findViewById<RadioGroup>(R.id.radiogroup)
        val jump = root.findViewById<CheckBox>(R.id.jump)
        var is_left = 0
        judge_label = root.findViewById(R.id.judge)
        button = root.findViewById(R.id.but1)
        var UserHeight : Int = 0

        val sensorManager = activity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)

        back = root.findViewById(R.id.back_to_user)

        if(sensor != null)
            sensorManager.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        else{
            button?.isEnabled = true
        }

        radiogroup.setOnCheckedChangeListener { group, checkedId ->
            is_left = if(checkedId == R.id.radio_left){
                1
            } else {
                0
            }
        }


        button!!.setOnClickListener {
            val intent = Intent()
            intent.setClass(activity, CameraActivity::class.java)
            if(jump.isChecked)
                intent.putExtra("jump",true)
            else
                intent.putExtra("jump",false)
            intent.putExtra("UH" , UserHeight)
            intent.putExtra("left", is_left)
            startActivity(intent)
        }

        back.setOnClickListener {
            val trans = fragmentManager.beginTransaction()
            val degstart = degstartFragment()
            trans.replace(R.id.navi_container, degstart)
            trans.commit()
        }

        return root
    }

    private val sensorListener = object: SensorEventListener {
        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

        override fun onSensorChanged(event: SensorEvent?) {
            if(event != null){
                if(event.values[1] > 9.0){
                    button?.isEnabled = true
                    judge_label!!.setImageResource(R.drawable.good)
                }
                else{
                    button?.isEnabled = false
                    judge_label!!.setImageResource(R.drawable.bad)
                }
            }
        }
    }
}