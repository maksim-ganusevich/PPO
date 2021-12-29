package com.example.mediaplayer

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


class FragmentAdapter(fragmentActivity: FragmentActivity?) : FragmentStateAdapter(fragmentActivity!!) {
    val pages = ArrayList<PageFragment>()


    override fun createFragment(position: Int): Fragment {
        if(pages[position].mode == "audio")
            return PageAudioFragment.newInstance(pages[position] as PageAudioFragment)
        else if (pages[position].mode == "video")
            return PageVideoFragment.newInstance(pages[position] as PageVideoFragment)
        else
            throw Exception("!mode")
    }

    override fun getItemCount(): Int {
        return pages.size
    }

    fun addItem(fragment: PageFragment){
        pages.add(fragment)
        this.notifyDataSetChanged()
    }

    fun getData() : ArrayList<PageFragment>{
        return pages
    }

    fun getItemAt(position: Int) : PageFragment{
        return pages[position]
    }

    fun setData(data : ArrayList<PageFragment>){
        this.pages.clear()
        this.pages.addAll(data)
        this.notifyDataSetChanged()
    }
}