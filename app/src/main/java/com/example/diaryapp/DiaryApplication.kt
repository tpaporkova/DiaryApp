package com.example.diaryapp

import android.app.Application
import com.example.diaryapp.data.local.AppDatabase

class DiaryApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}