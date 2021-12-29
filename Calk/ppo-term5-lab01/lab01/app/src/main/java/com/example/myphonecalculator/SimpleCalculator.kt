package com.example.myphonecalculator

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.text.isDigitsOnly


class SimpleCalculator : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_simple_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<TextView>(R.id.btn_change)
            .setOnClickListener {changeModToScience()}
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
    }

    companion object {
        @JvmStatic
        fun newInstance() = SimpleCalculator()
    }

    private fun changeModToScience(){

        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, ScientificCalculator(), "SCIENCE")
            commit()
        }
    }

    private fun workingWithLines(view: View, str:String){
        Handler.handler(view,str,
            requireActivity().findViewById<TextView>(R.id.math_operation),
            requireActivity().findViewById<TextView>(R.id.result_text)
        )
    }
}