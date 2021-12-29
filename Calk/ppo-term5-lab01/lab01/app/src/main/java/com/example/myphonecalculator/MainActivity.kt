package com.example.myphonecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.content.res.Configuration
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {

    private val simpleCalculator = SimpleCalculator()
    private val scientificCalculator = ScientificCalculator()
    private var currentMod:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.math_operation).setText(currentMod)
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, scientificCalculator,"SCIENCE")
                commit()
            }
        }
        else {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, simpleCalculator,"SIMPLE")
                commit()
                }
        }
        val inputLine:TextView = findViewById(R.id.math_operation)
        inputLine.text = "0"
    }
    fun setMod(str:String){

    }
    override fun onSaveInstanceState(outState: Bundle) {
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            if(supportFragmentManager.findFragmentByTag("SIMPLE") != null){
                currentMod = "Simple"
            }
            else if (supportFragmentManager.findFragmentByTag("SCIENCE") != null) currentMod = "Science"
        outState.putString("my_text", findViewById<TextView>(R.id.math_operation).text.toString())
        outState.putString("mod", currentMod)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedIntanceState: Bundle) {
        super.onRestoreInstanceState(savedIntanceState)
        this.currentMod = savedIntanceState.getString("mod").toString()
        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            if(savedIntanceState.getString("mod") == "Science")
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, scientificCalculator,"SCIENCE")
                    commit()
                }
            else
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.flFragment, simpleCalculator,"SIMPLE")
                    commit()
                }
        }
        findViewById<TextView>(R.id.math_operation).setText(savedIntanceState.getString("my_text"))

    }
}