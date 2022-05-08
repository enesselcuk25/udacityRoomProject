package com.eneselcuk.room.sleeptracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.eneselcuk.room.R
import com.eneselcuk.room.databinding.TextItemViewBinding

class TextViewViewHolder(val binding: TextItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

    companion object {
        fun from(parent: ViewGroup): TextViewViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                DataBindingUtil.inflate<TextItemViewBinding>(layoutInflater,
                    R.layout.text_item_view,
                    parent,
                    false)
            return TextViewViewHolder(binding)
        }
    }
}