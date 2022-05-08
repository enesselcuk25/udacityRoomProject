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


package com.eneselcuk.room.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eneselcuk.room.database.SleepDatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class SleepQualityViewModel @Inject constructor(
    private val sleepDao: SleepDatabaseDao,
) : ViewModel() {

    var key:Int = 0
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _navigateSleepTracker = MutableLiveData<Boolean?>()
    val navigateSleepTracker: LiveData<Boolean?>
        get() = _navigateSleepTracker


    fun doneNavigating() {
        _navigateSleepTracker.postValue(null)
    }

    fun onSetSleepQuality(quality: Int) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val tonight = sleepDao.get(key)
                tonight.sleepQuality = quality
                sleepDao.update(tonight)
            }
            _navigateSleepTracker.postValue(true)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
