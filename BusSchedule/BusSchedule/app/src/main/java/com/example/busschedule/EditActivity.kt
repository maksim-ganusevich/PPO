package com.example.busschedule

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList


class EditActivity : AppCompatActivity() {
    lateinit var editNumber: EditText
    lateinit var editType: EditText
    lateinit var editDestination: EditText
    lateinit var timeDeparture: TimePicker
    lateinit var timeArrival: TimePicker
    lateinit var busList: ArrayList<Bus>
    private var _mode = ""
    private var _position = -1
    @RequiresApi(Build.VERSION_CODES.O)
    private var _bus = Bus()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        editNumber = findViewById<EditText>(R.id.editNumber)
        editType = findViewById<EditText>(R.id.editType)
        editDestination = findViewById<EditText>(R.id.editDestination)
        timeDeparture = findViewById<TimePicker>(R.id.timeDeparture)
        timeDeparture.setIs24HourView(true)
        timeArrival = findViewById<TimePicker>(R.id.timeArrival)
        timeArrival.setIs24HourView(true)

        try {
            busList = intent.extras?.get("list") as ArrayList<Bus>
            _mode = intent.extras?.get("mode") as String
            if (_mode == "edit") {
                _position = intent.extras?.get("position") as Int
                _bus = busList[_position]

                editNumber.hint = _bus.Number
                editDestination.hint = _bus.Destination
                editType.hint = _bus.Type
                timeDeparture.hour = _bus.Departure?.hour!!
                timeDeparture.minute = _bus.Departure?.minute!!
                timeArrival.hour = _bus.Arrival?.hour!!
                timeArrival.minute = _bus.Arrival?.minute!!
            }
            else if(_mode == "add"){
                _bus = Bus()
            }
            else throw Exception("!mode")
        }
        catch (e: Exception){
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun confirm(view: View){
        try {
            if (!editNumber.text.isNullOrEmpty()) _bus.Number = editNumber.text.toString()
            if (!editDestination.text.isNullOrEmpty()) _bus.Destination = editDestination.text.toString()
            if (!editType.text.isNullOrEmpty()) _bus.Type = editType.text.toString()
            var stmp = intParse(timeDeparture.hour)
            stmp += ":"
            stmp += intParse(timeDeparture.minute)
            _bus.Departure = LocalTime.parse(stmp, DateTimeFormatter.ofPattern("HH:mm"))
            stmp = intParse(timeArrival.hour)
            stmp += ":"
            stmp += intParse(timeArrival.minute)
            _bus.Arrival = LocalTime.parse(stmp, DateTimeFormatter.ofPattern("HH:mm"))
            if(checkTime() >= 0){
                throw RuntimeException(getString(R.string.timeException))
            }
            when (_mode) {
                "add" -> {
                    busList.add(_bus)
                    busList.sort()
                }
                "edit" -> {
                    busList.removeAt(_position)
                    busList.add(_bus)
                    busList.sort()
                }
                else -> throw Exception("!mode")
            }
        }
        catch (e: RuntimeException){
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            return
        }
        catch (e: Exception){
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
        }
        getBackToMainActivity()
    }

    fun intParse(n: Int) : String{
        return if(n >= 10) n.toString() else "0$n"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun checkTime() : Int {
        return _bus.Departure!!.compareTo(_bus.Arrival)
    }

    fun getBackToMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("list", busList)
        startActivity(intent)
    }
}