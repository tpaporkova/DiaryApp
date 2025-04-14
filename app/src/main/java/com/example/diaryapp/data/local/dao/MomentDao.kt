package com.example.diaryapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.diaryapp.data.local.entities.Moment
import java.time.LocalDateTime

@Dao
interface MomentDao {
    @Insert
    suspend fun addMoment(moment: Moment)

    @Update
    suspend fun updateMoment(moment: Moment)

    @Delete
    suspend fun deleteMoment(moment: Moment)

    @Query("SELECT * FROM moments")
    fun getAllMoments(): LiveData<List<Moment>>

    @Query("SELECT * FROM moments WHERE id = :id")
    suspend fun getMomentById(id: Long): Moment?

    @Query("SELECT * FROM moments WHERE dateTime >= :startDate")
    suspend fun getMomentsAfter(startDate: LocalDateTime): List<Moment>
}