package com.example.diaryapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moments")
data class Moment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // дата в формате yyyy-MM-dd
    val location: String,
    val note: String,
    val type: String // один из заранее заданных типов
)
