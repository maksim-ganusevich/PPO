package com.example.busschedule

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.lang.Exception
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class Bus : Comparable<Bus>, Serializable {
    var Number = ""
    var Type = ""
    var Destination = ""
    var Departure : LocalTime? = null
    var Arrival : LocalTime? = null
    var isValid = false

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(){
        this.Number = "0"
        this.Type = "def"
        this.Destination = "def"
        this.Departure = LocalTime.parse("00:00", DateTimeFormatter.ofPattern("HH:mm"))
        this.Arrival = LocalTime.parse("00:00", DateTimeFormatter.ofPattern("HH:mm"))
        isValid = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    constructor(argsList: List<String>){
        try {
            if(argsList.size != 5)
                throw IllegalArgumentException()
            Number = argsList[0]
            Type = argsList[1]
            Destination = argsList[2]
            Departure = LocalTime.parse(argsList[3], DateTimeFormatter.ofPattern("HH:mm"))
            Arrival = LocalTime.parse(argsList[4], DateTimeFormatter.ofPattern("HH:mm"))
        }
        catch(e : Exception){
            return
        }
        isValid = true
        return
    }

    fun getStringDeparture() : String{
        return Departure.toString()
    }

    fun getStringArrival() :String{
        return Arrival.toString()
    }

    fun getList() : List<String>{
        return listOf<String>(Number.toString(), Type, Destination, Departure.toString(), Arrival.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun compareTo(other: Bus): Int {
        if(this.Departure!! > other.Departure)
            return 1
        else if(this.Departure!! < other.Departure)
            return -1
        else
            return 0
    }
}