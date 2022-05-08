package com.eneselcuk.room.sleeptracker.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eneselcuk.room.database.SleepNight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class SleepNightAdapter(var clickListener: SleepNightListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> SleepNightViewholder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is SleepNightViewholder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(nightItem.sleepNight, clickListener)
            }
        }

    }

    class SleepNightListener(val clickListener: (sleepId: Int) -> Unit) {
        fun onCLick(night: SleepNight) = clickListener(night.nightId)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_ITEM
        }
    }


    // UI threadlemeden adapterı çalıştırmamız gerekiyor.

    private val adapterScope = CoroutineScope(Dispatchers.Default)
    fun addHeaderAndSubmitList(list: List<SleepNight>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SleepNightItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }
}

// dataItımı kontrol etmek amacı ile bu kodu yazdık
sealed class DataItem {
    data class SleepNightItem(
        val sleepNight: SleepNight,
    ) : DataItem() {
        override val id: Int = sleepNight.nightId
    }

    object Header : DataItem() {
        override val id = Int.MIN_VALUE
    }

    abstract val id: Int
}


        /**
         *  tek bir layout ile çalıştığın da bu adaptırı kullanabilirisin
         */
/*
    class SleepNightAdapter(val clickListener:SleepNightListener) : ListAdapter<SleepNight,MyViewholder>(SleepNightDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewholder {
        return MyViewholder(DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.list_item_sleep_night,
            parent,
            false))
    }

    override fun onBindViewHolder(holder: MyViewholder, position: Int) {
        val item = getItem(position)
        holder.bind(item,clickListener)
    }

    class SleepNightListener(val clickListener:(sleepId:Int) -> Unit){
        fun onCLick(night: SleepNight) = clickListener(night.nightId)
    }





}
 */