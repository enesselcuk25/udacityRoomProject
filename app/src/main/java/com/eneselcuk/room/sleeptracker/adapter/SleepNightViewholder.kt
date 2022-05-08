package com.eneselcuk.room.sleeptracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eneselcuk.room.database.SleepNight
import com.eneselcuk.room.databinding.ListItemSleepNightBinding

class SleepNightViewholder(val binding: ListItemSleepNightBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(sleepNight: SleepNight, clickListener: SleepNightAdapter.SleepNightListener) {
        binding.sleep = sleepNight
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)

            return SleepNightViewholder(binding)
        }
    }
}

/**
 *  tek bir layout ile çalıştığında bu ViewHoldırı kullanabilirsin
 */

/*
    class MyViewholder(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(sleepNight: SleepNight, clickListener: SleepNightAdapter.SleepNightListener) {
        binding.sleep = sleepNight
        binding.clickListener = clickListener
        binding.executePendingBindings()
    }
}

 */