package com.example.diaryapp.data.repository

import com.example.diaryapp.data.local.dao.MomentDao
import com.example.diaryapp.data.local.entities.Moment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MomentRepository(private val momentDao: MomentDao) {

    val allMoments = momentDao.getAllMoments()

    suspend fun addMoment(moment: Moment) {
        withContext(Dispatchers.IO) {
            momentDao.addMoment(moment)
        }
    }

    suspend fun updateMoment(moment: Moment) {
        withContext(Dispatchers.IO) {
            momentDao.updateMoment(moment)
        }
    }

    suspend fun deleteMoment(moment: Moment) {
        withContext(Dispatchers.IO) {
            momentDao.deleteMoment(moment)
        }
    }

    suspend fun getMomentById(id: Long): Moment? {
        return withContext(Dispatchers.IO) {
            momentDao.getMomentById(id)
        }
    }
}
