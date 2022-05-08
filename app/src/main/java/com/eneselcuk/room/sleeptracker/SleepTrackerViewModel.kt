/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eneselcuk.room.sleeptracker

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.eneselcuk.room.database.SleepDatabaseDao
import com.eneselcuk.room.database.SleepNight
import com.eneselcuk.room.formatNights
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class SleepTrackerViewModel @Inject constructor(
    val database: SleepDatabaseDao,
    application: Application,
) : AndroidViewModel(application) {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private val tonight = MutableLiveData<SleepNight?>()
    val nights = database.getAllNights()

    private val _navigateToSleepQuality = MutableLiveData<SleepNight?>()
    val navigateToSleepQuality: LiveData<SleepNight?>
        get() = _navigateToSleepQuality

    private var _showSnacbarEvent = MutableLiveData<Boolean>()
    val showSnacbarEvent: LiveData<Boolean>
        get() = _showSnacbarEvent

    fun doneShowingSnackbar() {
        _showSnacbarEvent.postValue(false)
    }

    val startButtonVisibility = Transformations.map(tonight) {
        null == it
    }

    // startda btn tıklandığında stop btn true oluyor visivility(görünüyor) oluyor
    val stopButtonVisibility = Transformations.map(tonight) {
        null != it
    }

    // ekran da çalışan hiç bir şey yok ise btn görünmüyor(inVisible)
    val clearButtonVisibility = Transformations.map(nights) {
        it?.isNotEmpty()
    }

    // bu kod işlemi stringe çeviriyor ve dataBindinge veriyor ordan da textView setliyor.
    val nightString = Transformations.map(nights) {
        formatNights(it, application.resources)
    }

    //  viewModeli fragmentte çağırdığımzda; doneNavigating viewModel null yapıyor. kapatıyor.
    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }


    init {
        initializeTonight()
    }


    private fun initializeTonight() {
        uiScope.launch {
            tonight.postValue(getTonightFromDatabase())
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getTonight()

            if (night?.endTimeMilli != night?.startTimeMilli) {
                night = null
            }
            night
        }
    }

    fun onStartTacking() {
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)

            tonight.postValue(getTonightFromDatabase())
        }
    }

    private suspend fun insert(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.insertSleep(night)
        }
    }

    fun someWorkNeedsToBeDone() {
        uiScope.launch {
            suspendFunction()
        }
    }

    suspend fun suspendFunction() {
        withContext(Dispatchers.IO) {

        }
    }

    fun onStopTracking() {
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)

            // Stop tıklayınca dursun
            _navigateToSleepQuality.postValue(oldNight)
        }
    }

    private suspend fun update(night: SleepNight) {
        withContext(Dispatchers.IO) {
            database.update(night)
        }

    }

    fun onClear() {
        uiScope.launch {
            clear()
            tonight.postValue(null)
        }
    }

    suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
            _showSnacbarEvent.postValue(true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    // step rcView
    private val _navigateToSleepDataQuality = MutableLiveData<Int?>()
    val navigateToSleepDataQuality: LiveData<Int?>
        get() = _navigateToSleepDataQuality


    // navihation ayarlarına gitmek için id ile gidiyoruz
    fun onSleepNightClicked(id: Int) {
        _navigateToSleepDataQuality.postValue(id)
    }

    // navigation yaptıktan sonra viewModeli kapatıyoruz.
    fun onSleepDataQualityNavigated() {
        _navigateToSleepDataQuality.postValue(null)
    }


}

