package com.example.mediaplayer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.VideoView

import android.net.Uri
import java.io.FileNotFoundException
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import java.lang.Exception


class PageVideoFragment(file: String, context : Context) : PageFragment(file) {
    val requestCode = 1
    override var mode: String = "video"
    lateinit var videoPlayer : VideoView
    var currentPosition : Int = 0
    var startPosition : Long = 0
    lateinit var textView : TextView
    private var canNowPlayMusic : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        file = if (arguments != null) requireArguments().getString("file")!! else throw FileNotFoundException()
        if(savedInstanceState != null){
            this.file = savedInstanceState.getString("file") as String
            this.currentPosition = savedInstanceState.getInt("currentPosition")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val result: View = inflater.inflate(R.layout.video_fragment_page, container, false)
        textView = result.findViewById<TextView>(R.id.displayVideo)
        var name : String? = null
        try{
            name = getFileNameFromURI(Uri.parse(file))
        }
        catch (e:Exception){}
        textView.text = name ?: file
        videoPlayer = result.findViewById<VideoView>(R.id.videoPlayer)
        return result
    }

    companion object {
        fun newInstance(fragment: PageVideoFragment): PageVideoFragment {
            val args = Bundle()
            args.putString("file", fragment.file)
            fragment.arguments = args
            return fragment
        }
    }

    private fun fullScreenMode(){
        var intent = Intent(context, FullScreenActivity::class.java)
        intent.putExtra("file", file)
        intent.putExtra("currentPosition", videoPlayer.currentPosition)
        startActivityForResult(intent, requestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data == null)
            return
        this.currentPosition = data.getIntExtra("currentPosition", 0)
        videoPlayer.seekTo(currentPosition)
        videoPlayer.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("currentPosition", currentPosition)
        outState.putString("file", file)
        super.onSaveInstanceState(outState)
    }

    override fun onStart() {
        super.onStart()
        val myVideoUri = Uri.parse(file)
        videoPlayer.setVideoURI(myVideoUri)
        val mediaController =
            CustomMediaController(requireContext())
        mediaController.setListener {
            fullScreenMode()
        }
        videoPlayer.setMediaController(mediaController)
        mediaController.setMediaPlayer(videoPlayer)

    }

    override fun onStop() {
        super.onStop()
        videoPlayer.stopPlayback()
    }

    override fun onResume() {
        super.onResume()
        if(canNowPlayMusic){
            context?.stopService(Intent(context, MediaService::class.java))
            NotificationManagerCompat.from(requireContext()).cancel(NotificationHandler.NOTIFICATION_ID)
        }
        context?.stopService(Intent(context, MediaService::class.java))
        NotificationManagerCompat.from(requireContext()).cancel(NotificationHandler.NOTIFICATION_ID)
        videoPlayer.seekTo(currentPosition)
        videoPlayer.start()
        startPosition = System.currentTimeMillis()
    }

    override fun onPause() {
        videoPlayer.pause()
        currentPosition += (System.currentTimeMillis() - startPosition).toInt()
        super.onPause()
    }
}