package com.example.diaryapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.diaryapp.data.local.dao.MomentDao
import com.example.diaryapp.data.local.entities.Moment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.Flow

class MomentRepository(private val momentDao: MomentDao) {

    // Flow для получения всех моментов
    val allMoments: Flow<List<Moment>> = momentDao.getAllMoments()

    // Вставка нового момента в базу данных
    suspend fun insertMoment(moment: Moment) {
        withContext(Dispatchers.IO) {
            Log.d("MomentRepository", "Inserting moment: $moment")
            momentDao.insertMoment(moment)
        }
    }

    // Обновление существующего момента
    suspend fun updateMoment(moment: Moment) {
        withContext(Dispatchers.IO) {
            momentDao.updateMoment(moment)
        }
    }

    // Удаление момента
    suspend fun deleteMoment(moment: Moment) {
        withContext(Dispatchers.IO) {
            momentDao.deleteMoment(moment)
        }
    }

    // Получение момента по id
    suspend fun getMomentById(id: Long): Moment? {
        return withContext(Dispatchers.IO) {
            momentDao.getMomentById(id)
        }
    }
}
