package com.example.busschedule

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.ArrayList

class BusList {
    var List : ArrayList<Bus> = ArrayList()
    private val delimeters = " "
    var isValid = false

    @RequiresApi(Build.VERSION_CODES.O)
    fun add(bus : String){
        List.add(Bus(bus.split(delimeters)))
    }

    fun sort(){
        this.List.sort()
    }

    fun clear(){
        this.List.clear()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generate(){
        List.add(Bus("415 МАЗ Минск 14:55 18:30".split(delimeters)))
        List.add(Bus("418 ГАЗ Брест 13:15 16:38".split(delimeters)))
        List.add(Bus("412 МАЗ Вильнюс 10:00 16:54".split(delimeters)))
        List.add(Bus("623 ВАЗ Москва 06:58 23:20".split(delimeters)))
        List.add(Bus("415 МАЗ Минск 10:45 14:20".split(delimeters)))
        List.add(Bus("415 МАЗ Минск 11:45 15:20".split(delimeters)))
        List.add(Bus("415 МАЗ Минск 12:45 16:20".split(delimeters)))
        List.add(Bus("420 МАЗ Минск 12:55 15:30".split(delimeters)))
        List.add(Bus("418 МАЗ Брест 18:15 21:38".split(delimeters)))
        List.add(Bus("418 МАЗ Брест 09:15 12:38".split(delimeters)))
    }
}