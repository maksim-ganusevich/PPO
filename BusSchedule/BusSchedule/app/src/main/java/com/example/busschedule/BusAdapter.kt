package com.example.busschedule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import java.lang.Exception

class BusAdapter(context: Context?, private val layout: Int, private val busList: BusList) :
    ArrayAdapter<Bus>(context!!, layout, busList.List as List<Bus>) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val viewHolder: ViewHolder

        if (convertView == null) {
            convertView = inflater.inflate(layout, parent, false)
            viewHolder = ViewHolder(convertView)
            convertView.tag = viewHolder
        }
        else {
            viewHolder = convertView.tag as ViewHolder
        }
        val bus = busList.List[position]
        try {
            if (!bus.isValid) {
                throw Exception("Is not valid")
            }
            viewHolder.busNumber.text = bus.Number
            viewHolder.busType.text = bus.Type
            viewHolder.busDestination.text = bus.Destination
            viewHolder.busDeparture.text = bus.getStringDeparture()
            viewHolder.busArrival.text = bus.getStringArrival()
        }
        catch (e: Exception) {
            viewHolder.busNumber.text = "Err"
            viewHolder.busDestination.text = e.message
        }
        return convertView!!
    }

    private inner class ViewHolder internal constructor(view: View) {
        val busNumber: TextView
        val busType: TextView
        val busDestination: TextView
        val busDeparture: TextView
        val busArrival: TextView

        init {
            busNumber = view.findViewById(R.id.busNumber)
            busType = view.findViewById(R.id.busType)
            busDestination = view.findViewById(R.id.busDestination)
            busDeparture = view.findViewById(R.id.busDeparture)
            busArrival = view.findViewById(R.id.busArrival)
        }
    }

}