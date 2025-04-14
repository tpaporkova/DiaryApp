package com.example.diaryapp.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.diaryapp.data.local.entities.Moment
import kotlinx.coroutines.flow.Flow

@Dao
interface MomentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMoment(moment: Moment)

    @Update
    suspend fun updateMoment(moment: Moment)

    @Delete
    suspend fun deleteMoment(moment: Moment)

    @Query("SELECT * FROM moments")
    fun getAllMoments(): Flow<List<Moment>>  // Используйте Flow вместо LiveData

    @Query("SELECT * FROM moments WHERE id = :id")
    suspend fun getMomentById(id: Long): Moment?
}
