package com.example.mediaplayer

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.VideoView
import androidx.core.app.NotificationManagerCompat
import java.io.FileNotFoundException
import java.lang.Exception


class PageAudioFragment(file: String, context: Context) : PageFragment(file), View.OnClickListener {
    public override var mode: String = "audio"
    lateinit var textView: TextView
    var currentPosition : Int = 0
    var startPosition : Long = 0
    var isPlaying : Boolean = false
    lateinit var buttonPlay : Button
    var filename : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        file = if (arguments != null) requireArguments().getString("file")!! else throw FileNotFoundException()
        if(savedInstanceState != null){
            this.file = savedInstanceState.getString("file") as String
            this.currentPosition = savedInstanceState.getInt("currentPosition")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val result: View = inflater.inflate(R.layout.audio_fragment_page, container, false)
        textView = result.findViewById<TextView>(R.id.displayAudio)
        try{
            filename = getFileNameFromURI(Uri.parse(file))
        }
        catch (e: Exception){}
        filename = filename ?: file
        textView.text = filename

        result.findViewById<Button>(R.id.buttonSub).setOnClickListener(this)
        result.findViewById<Button>(R.id.buttonAdd).setOnClickListener(this)
        buttonPlay = result.findViewById<Button>(R.id.buttonPlay)
        buttonPlay.setOnClickListener(this)

        return result
    }

    companion object {
        fun newInstance(fragment: PageAudioFragment): PageAudioFragment {
            val args = Bundle()
            args.putString("file", fragment.file)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        val intent = Intent(context, MediaService::class.java)
        intent.putExtra("file", file)
        intent.putExtra("currentPosition", currentPosition)
        context?.stopService(intent)
        context?.startService(intent)
        isPlaying = true
        buttonPlay.text = getString(R.string.pause)
        NotificationHandler().displayNotification(this.context, filename)
        startPosition = System.currentTimeMillis()
    }

    override fun onPause() {
        isPlaying = false
        buttonPlay.text = getString(R.string.play)
        currentPosition += (System.currentTimeMillis() - startPosition).toInt()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("currentPosition", currentPosition)
        outState.putString("file", file)
        super.onSaveInstanceState(outState)
    }


    override fun onClick(view: View){
        var intent = Intent(context, MediaService::class.java)
        when(view.id){
            R.id.buttonPlay -> {
                if(!isPlaying){
                    isPlaying = true
                    buttonPlay.text = getString(R.string.pause)
                    startPosition = System.currentTimeMillis()
                }
                else{
                    isPlaying = false
                    buttonPlay.text = getString(R.string.play)
                    currentPosition += (System.currentTimeMillis() - startPosition).toInt()
                    context?.stopService(intent)
                    return
                }
            }
            R.id.buttonAdd -> {
                if(!isPlaying)
                    return
                currentPosition += (System.currentTimeMillis() - startPosition).toInt()
                currentPosition += 10000
                startPosition = System.currentTimeMillis()
                Toast.makeText(context?.applicationContext, "+10", Toast.LENGTH_SHORT).show()
                context?.stopService(intent)
            }
            R.id.buttonSub -> {
                if(!isPlaying)
                    return
                currentPosition += (System.currentTimeMillis() - startPosition).toInt()
                currentPosition -= 10000
                startPosition = System.currentTimeMillis()
                if(currentPosition < 0)
                    currentPosition = 0
                Toast.makeText(context?.applicationContext, "-10", Toast.LENGTH_SHORT).show()
                context?.stopService(intent)
            }
        }
        intent.putExtra("file", file)
        intent.putExtra("currentPosition", currentPosition)
        context?.startService(intent)
    }
}