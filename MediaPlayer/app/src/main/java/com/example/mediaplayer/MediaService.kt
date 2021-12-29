package com.example.mediaplayer


import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import java.io.FileNotFoundException
import java.lang.UnsupportedOperationException


class MediaService : Service() {
    private var ambientMediaPlayer: MediaPlayer? = null

    override fun onBind(intent: Intent?): IBinder {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val file = intent?.getStringExtra("file")
        val currentPosition = intent?.getIntExtra("currentPosition", 0) as Int
        if(file.isNullOrEmpty())
            throw FileNotFoundException()
        ambientMediaPlayer = MediaPlayer.create(this, Uri.parse(file))
        if(ambientMediaPlayer == null)
            throw Exception("mediaplayer is null")
        ambientMediaPlayer!!.isLooping = true
        if(currentPosition < ambientMediaPlayer!!.duration && currentPosition > 0)
            ambientMediaPlayer!!.seekTo(currentPosition)
        ambientMediaPlayer!!.start()
        return START_STICKY
    }

    override fun onDestroy() {
        ambientMediaPlayer!!.stop()
    }
}