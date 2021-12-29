package com.example.mediaplayer

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.media.MediaPlayer;
import android.util.Log
import androidx.viewpager2.widget.ViewPager2
import android.widget.*
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback


class MainActivity : AppCompatActivity() {
    lateinit var chooseAudioButton : Button
    lateinit var chooseVideoButton: Button
    lateinit var viewPager: ViewPager2
    lateinit var mediaPlayer: MediaPlayer
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById<ViewPager2>(R.id.viewPager)
        viewPager.adapter = FragmentAdapter(this)
        chooseAudioButton = findViewById<Button>(R.id.buttonChooseAudio)
        chooseVideoButton = findViewById<Button>(R.id.buttonChooseVideo)
        mediaPlayer = MediaPlayer()

        textView = findViewById(R.id.textView)

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                textView.text = "Страница ${position + 1}"
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
            }
        })
    }

    fun chooseAudio(view: View){
        val adapter = viewPager.adapter as FragmentAdapter
        var file = "android.resource://" + packageName + "/" + R.raw.test_music
        adapter.addItem(PageAudioFragment(file, this))
    }

    fun chooseVideo(view: View){
        val adapter = viewPager.adapter as FragmentAdapter
        var file = "android.resource://" + packageName + "/" + R.raw.test_video
        adapter.addItem(PageVideoFragment(file, this))
    }

    override fun onActivityResult(requestcode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestcode, resultCode, data)
        val path: String = data?.data?.toString() ?: return
        Log.d("FFFFFFFpath", path)
        Log.d("Uripath", data.data.toString())
        val adapter = viewPager.adapter as FragmentAdapter
        if(resultCode != RESULT_OK)
            return
        when(requestcode){
            1 ->{
                adapter.addItem(PageAudioFragment(path, this))
            }
            2 ->{
                adapter.addItem(PageVideoFragment(path, this))
            }
        }
    }

    fun openFileChooser(view: View) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        when(view.id){
            R.id.buttonChooseAudio -> {
                intent.type = "audio/*"
                startActivityForResult(intent, 1)
            }
            R.id.buttonChooseVideo -> {
                intent.type = "video/*"
                startActivityForResult(intent, 2)
            }
        }

    }
}