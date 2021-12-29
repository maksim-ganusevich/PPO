package com.example.mediaplayer

import android.content.Intent
import android.graphics.Point
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Display
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.VideoView
import java.lang.NullPointerException
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.Toast


class FullScreenActivity : AppCompatActivity() {
    var file = ""
    var currentPosition = 0
    lateinit var videoView: VideoView
    private lateinit var gestureDetector : GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            this.supportActionBar!!.hide()
        }
        catch (e: NullPointerException) {
        }
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_full_screen)
        videoView = findViewById(R.id.fullScreenVideoView)

        val display: Display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
        val height: Int = size.y
        gestureDetector = GestureDetector(this, GestureListener())
        gestureDetector.setOnDoubleTapListener(object : GestureListener(){
            override fun onDoubleTap(e: MotionEvent): Boolean {
                val x = e.x
                val y = e.y
                try {
                    when{
                        x > 0.6 * height ->{
                            if(videoView.currentPosition + 10000 < videoView.duration)
                                currentPosition += 10000
                            else
                                currentPosition = videoView.duration - 1000
                            Toast.makeText(applicationContext, "+10", Toast.LENGTH_SHORT).show()
                        }
                        x < 0.4 * height ->{
                            if(videoView.currentPosition - 10000 > 0)
                                currentPosition -= 10000
                            else
                                currentPosition = 0
                            Toast.makeText(applicationContext, "-10", Toast.LENGTH_SHORT).show()
                        }
                    }
                    videoView.seekTo(currentPosition)
                    videoView.start()
                }
                finally {}
                return true
            }
        })

        val mediaController =
            CustomMediaController(this)
        mediaController.setListener {
            back()
        }
        videoView.setMediaController(mediaController)
        mediaController.setMediaPlayer(videoView)

        if(intent.extras != null){
            file = intent.extras!!.getString("file") as String
            currentPosition = intent.extras!!.get("currentPosition") as Int
        }
        else
            return
        videoView.setVideoURI(Uri.parse(file))
        videoView.seekTo(currentPosition)
        videoView.start()
    }

    fun back(){
        val intent = Intent()
        intent.putExtra("currentPosition", videoView.currentPosition)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private open class GestureListener : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            return true
        }
    }
}