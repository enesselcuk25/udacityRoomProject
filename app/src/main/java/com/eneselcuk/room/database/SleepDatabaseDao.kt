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

package com.eneselcuk.room.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SleepDatabaseDao {

    @Insert
    fun insertSleep(night: SleepNight)

    @Update
    fun update(night: SleepNight)

    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    fun get(key: Int): SleepNight

    @Query("DELETE FROM daily_sleep_quality_table")
    fun clear()


    /*
     daily_sleep_quality_table tablosunu ger dönüş değeri olan bir sorgu döndürür.
     order by azalan bir şekilde çağırır
     liveData ile güncel veriyi getirir
     */
    @Query("SELECT * FROM daily_sleep_quality_table Order by nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    /*
     * sadece bir tane element döndürür
     */
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    fun getTonight(): SleepNight?

    // seç ve verilen night id geri dön
    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    fun getNightWithId(key: Int): LiveData<SleepNight>


}
