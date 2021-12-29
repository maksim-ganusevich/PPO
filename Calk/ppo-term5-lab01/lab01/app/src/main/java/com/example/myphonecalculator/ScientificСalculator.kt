package com.example.myphonecalculator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.isDigitsOnly
import android.content.pm.ActivityInfo

import android.app.Activity
import android.content.res.Configuration


class ScientificCalculator : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scientific_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.btn_change)
            .setOnClickListener {changeModToSimple()}
        view.findViewById<TextView>(R.id.btn_0)
            .setOnClickListener {workingWithLines(view,"0")}
        view.findViewById<TextView>(R.id.btn_dot)
            .setOnClickListener {workingWithLines(view,".")}
        view.findViewById<TextView>(R.id.btn_eq)
            .setOnClickListener {Handler.printResult(view,
            requireActivity().findViewById<TextView>(R.id.math_operation),
            requireActivity().findViewById<TextView>(R.id.result_text))}
        view.findViewById<TextView>(R.id.btn_1)
            .setOnClickListener {workingWithLines(view,"1")}
        view.findViewById<TextView>(R.id.btn_2)
            .setOnClickListener {workingWithLines(view,"2")}
        view.findViewById<TextView>(R.id.btn_3)
            .setOnClickListener {workingWithLines(view,"3")}
        view.findViewById<TextView>(R.id.btn_add)
            .setOnClickListener {workingWithLines(view,"+")}
        view.findViewById<TextView>(R.id.btn_4)
            .setOnClickListener {workingWithLines(view,"4")}
        view.findViewById<TextView>(R.id.btn_5)
            .setOnClickListener {workingWithLines(view,"5")}
        view.findViewById<TextView>(R.id.btn_6)
            .setOnClickListener {workingWithLines(view,"6")}
        view.findViewById<TextView>(R.id.btn_sub)
            .setOnClickListener {workingWithLines(view,"-")}
        view.findViewById<TextView>(R.id.btn_7)
            .setOnClickListener {workingWithLines(view,"7")}
        view.findViewById<TextView>(R.id.btn_8)
            .setOnClickListener {workingWithLines(view,"8")}
        view.findViewById<TextView>(R.id.btn_9)
            .setOnClickListener {workingWithLines(view,"9")}
        view.findViewById<TextView>(R.id.btn_mul)
            .setOnClickListener {workingWithLines(view,"×")}
        view.findViewById<TextView>(R.id.btn_AC)
            .setOnClickListener {Handler.clean(view,
            requireActivity().findViewById<TextView>(R.id.math_operation),
            requireActivity().findViewById<TextView>(R.id.result_text))}
        view.findViewById<TextView>(R.id.btn_back)
            .setOnClickListener {Handler.stepBack(view,
            requireActivity().findViewById<TextView>(R.id.math_operation),
            requireActivity().findViewById<TextView>(R.id.result_text))}
        view.findViewById<TextView>(R.id.btn_proc)
            .setOnClickListener {workingWithLines(view,"%")}
        view.findViewById<TextView>(R.id.btn_div)
            .setOnClickListener {workingWithLines(view,"÷")}
        view.findViewById<TextView>(R.id.btn_e)
            .setOnClickListener {workingWithLines(view,"e")}
        view.findViewById<TextView>(R.id.btn_pi)
            .setOnClickListener {workingWithLines(view,"π")}
        view.findViewById<TextView>(R.id.btn_inver)
            .setOnClickListener {workingWithLines(view,"^(-1)")}
        view.findViewById<TextView>(R.id.btn_fac)
            .setOnClickListener {workingWithLines(view,"!")}
        view.findViewById<TextView>(R.id.btn_root)
            .setOnClickListener {workingWithLines(view,"√")}
        view.findViewById<TextView>(R.id.btn_degree)
            .setOnClickListener {workingWithLines(view,"^")}
        view.findViewById<TextView>(R.id.btn_lg)
            .setOnClickListener {workingWithLines(view,"lg")}
        view.findViewById<TextView>(R.id.btn_ln)
            .setOnClickListener {workingWithLines(view,"ln")}
        view.findViewById<TextView>(R.id.btn_open_br)
            .setOnClickListener {workingWithLines(view,"(")}
        view.findViewById<TextView>(R.id.btn_close_br)
            .setOnClickListener {workingWithLines(view,")")}
        view.findViewById<TextView>(R.id.btn_dr)
            .setText(Handler.angleConf)
        view.findViewById<TextView>(R.id.btn_sin)
            .setText(Handler.trigonometryConf + "sin")
        view.findViewById<TextView>(R.id.btn_sin)
            .setOnClickListener {workingWithLines(view,"sin")}
        view.findViewById<TextView>(R.id.btn_cos)
            .setText(Handler.trigonometryConf + "cos")
        view.findViewById<TextView>(R.id.btn_cos)
            .setOnClickListener {workingWithLines(view,"cos")}
        view.findViewById<TextView>(R.id.btn_tan)
            .setText(Handler.trigonometryConf + "tan")
        view.findViewById<TextView>(R.id.btn_tan)
            .setOnClickListener {workingWithLines(view,"tan")}
        if(Handler.isArcAvailable()){
            view.findViewById<TextView>(R.id.btn_2nd).setTextColor(resources.getColor(R.color.black))
            view.findViewById<TextView>(R.id.btn_2nd)
                .setOnClickListener {changeTrigCong(view)}
        }
        else {
            view.findViewById<TextView>(R.id.btn_2nd).setTextColor(resources.getColor(android.R.color.darker_gray))
            view.findViewById<TextView>(R.id.btn_2nd)
                .setOnClickListener {}
        }
        if(Handler.isRadAvailable()){
            view.findViewById<TextView>(R.id.btn_dr).setTextColor(resources.getColor(R.color.black))
            view.findViewById<TextView>(R.id.btn_dr)
                .setOnClickListener {changeAngleConf(view)}
        }
        else {
            view.findViewById<TextView>(R.id.btn_dr).setTextColor(resources.getColor(android.R.color.darker_gray))
            view.findViewById<TextView>(R.id.btn_dr)
                .setOnClickListener {}
        }
        if(resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE)
            Handler.orientation = "port"
        else Handler.orientation = "land"
    }

    companion object {
        @JvmStatic
        fun newInstance() = SimpleCalculator()
    }

    private fun changeModToSimple(){
        if(resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE){
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, SimpleCalculator(), "SIMPLE")
                commit()
            }
        }
    }

    private fun workingWithLines(view: View, str:String){
        Handler.handler(view,str,
            requireActivity().findViewById<TextView>(R.id.math_operation),
            requireActivity().findViewById<TextView>(R.id.result_text)
        )
    }

    private fun changeAngleConf(view:View){
        if(Handler.angleConf == "rad"){
            Handler.angleConf= "deg"
            view.findViewById<TextView>(R.id.btn_2nd).
                setTextColor(resources.getColor(android.R.color.darker_gray))
            view.findViewById<TextView>(R.id.btn_2nd)
                .setOnClickListener {}
        }
        else{
            Handler.angleConf = "rad"
            view.findViewById<TextView>(R.id.btn_2nd).setTextColor(resources.getColor(R.color.black))
            view.findViewById<TextView>(R.id.btn_2nd)
                .setOnClickListener {changeTrigCong(view)}
        }
        view.findViewById<TextView>(R.id.btn_dr).setText(Handler.angleConf)
    }

    private fun changeTrigCong(view:View){
        if(Handler.trigonometryConf == ""){
            Handler.trigonometryConf = "a"
            view.findViewById<TextView>(R.id.btn_dr).
            setTextColor(resources.getColor(android.R.color.darker_gray))
            view.findViewById<TextView>(R.id.btn_dr)
                .setOnClickListener {}
        }
        else{
            Handler.trigonometryConf = ""
            view.findViewById<TextView>(R.id.btn_dr).setTextColor(resources.getColor(R.color.black))
            view.findViewById<TextView>(R.id.btn_dr)
                .setOnClickListener {changeAngleConf(view)}
        }
        view.findViewById<TextView>(R.id.btn_sin).setText(Handler.trigonometryConf + "sin")
        view.findViewById<TextView>(R.id.btn_cos).setText(Handler.trigonometryConf + "cos")
        view.findViewById<TextView>(R.id.btn_tan).setText(Handler.trigonometryConf + "tan")
    }
}
