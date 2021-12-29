package com.example.busschedule

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import java.io.InputStream
import android.view.ContextMenu.ContextMenuInfo
import android.view.ContextMenu
import android.view.MenuItem
import android.widget.*
import android.widget.AdapterView.AdapterContextMenuInfo
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    val requestcode = 1
    var busList : BusList = BusList()
    lateinit var listView : ListView
    lateinit var spinner: Spinner
    lateinit var inputTime: TimePicker
    lateinit var buttonFind: Button
    var unqlst = ArrayList<String>()
    var findFlag = ""


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)
            findFlag = this.getString(R.string.find)
            listView = findViewById<ListView>(R.id.busList)
            spinner = findViewById<Spinner>(R.id.spinner)
            inputTime = findViewById<TimePicker>(R.id.inputTime)
            inputTime.setIs24HourView(true)
            buttonFind = findViewById<Button>(R.id.buttonFind)
            registerForContextMenu(listView)
        }
        catch (e: Exception){
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
        }

//        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//
//            }
//        }

        if(intent.extras == null){
            _setBusList()
        }
        else{
            try {
                busList.List = intent.extras!!.get("list") as ArrayList<Bus>
                updateLists()
            }
            catch (e: Exception){
                Toast.makeText(applicationContext, e.message, Toast.LENGTH_LONG).show()
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestcode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestcode, resultCode, data)
        if(requestcode == this.requestcode && resultCode == Activity.RESULT_OK){
            if(data == null)
                return
            val uri = data.data
            if (uri != null) {
                readFile(uri)
                //Toast.makeText(applicationContext, uri.path, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openFileChooser(view: View){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("*/*")
        startActivityForResult(intent, requestcode)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun readFile(uri : Uri){
        try {
            val inputStream: InputStream? = getContentResolver().openInputStream(uri)
            val lineList = mutableListOf<String>()
            inputStream?.bufferedReader()?.forEachLine { lineList.add(it) }
            _setBusList(lineList)
        }
        catch (e:Exception){
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun _setBusList(lineList: MutableList<String>? = null){
        if(lineList == null){
            busList.generate()
        }
        else{
            busList.clear()
            lineList.forEach{busList.add(it)}
        }
        busList.sort()
        updateLists()
    }

    private fun updateLists(){
        listView.adapter = BusAdapter(this, R.layout.bus, busList)
        unqlst.clear()
        busList.List.forEach{unqlst.add(it.Destination)}
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, unqlst.distinct())
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.context_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        if(findFlag != getString(R.string.find))
            return false
        val info = item.getMenuInfo() as AdapterContextMenuInfo
        return when (item.getItemId()) {
            R.id.edit -> {
                editItem(info.position)
                true
            }
            R.id.delete -> {
                deleteItem(info.position)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun editItem(position : Int){
        startEditActivity(position)
    }

    fun addItem(view: View){
        startEditActivity()
    }

    private fun deleteItem(position : Int){
        busList.List.removeAt(position)
        updateLists()
    }

    private fun startEditActivity(position: Int? = null){
        val intent = Intent(this, EditActivity::class.java)

        intent.putExtra("list", busList.List)

        if(position != null){
            intent.putExtra("position", position)
            intent.putExtra("mode", "edit")
        }
        else{
            intent.putExtra("mode", "add")
        }
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun findBus(view: View){
        if(findFlag == getString(R.string.find)) {
            try {
                var destination = spinner.selectedItem as String
                if (destination.isNullOrEmpty()) throw Exception("!destination")
                var destList = ArrayList<Bus>()
                var timeList = BusList()
                busList.List.forEach { if (it.Destination == destination) destList.add(it) }
                var stmp = if(inputTime.hour >= 10) inputTime.hour.toString() else "0${inputTime.hour}"
                stmp += ":"
                stmp += if(inputTime.minute >= 10) inputTime.minute.toString() else "0${inputTime.minute}"
                var arrival = LocalTime.parse(stmp, DateTimeFormatter.ofPattern("HH:mm"))
                destList.forEach { if(arrival >= it.Arrival) timeList.List.add(it) }
                listView.adapter = BusAdapter(this, R.layout.bus, timeList)
            } catch (e: Exception) {
                Toast.makeText(applicationContext, "findBus:" + e.message, Toast.LENGTH_SHORT).show()
            }
            findFlag = getString(R.string.back)
            spinner.isEnabled = false
        }
        else if (findFlag == getString(R.string.back)){
            findFlag = getString(R.string.find)
            spinner.isEnabled = true
            updateLists()
        }
        buttonFind.text = findFlag
    }
}