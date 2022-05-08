package com.eneselcuk.room.sleepDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eneselcuk.room.database.SleepDatabaseDao
import com.eneselcuk.room.database.SleepNight
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SleepDetailViewModel @Inject constructor(dataSource: SleepDatabaseDao) : ViewModel() {

    val dataBase = dataSource
    var sleepNightKey = 0


    private val night = MediatorLiveData<SleepNight>()
    fun getNight() = night

    init {
        night.addSource(dataBase.getNightWithId(sleepNightKey), night::setValue)
    }

    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?>
        get() = _navigateToSleepTracker

    fun doneNavigating() {
        _navigateToSleepTracker.postValue(null)
    }


    fun onClose() {
        _navigateToSleepTracker.postValue(true)
    }


}