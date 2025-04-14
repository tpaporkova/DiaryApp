package com.example.diaryapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.diaryapp.data.local.converters.DateTimeConverter
import java.time.LocalDateTime

@Entity(tableName = "moments")
@TypeConverters(DateTimeConverter::class) // Указываем, что используем конвертер
data class Moment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val date: String, // Дата может быть сохранена в строковом формате
    val note: String, // Описание или заметки
    val location: String, // Место
    val type: String, // Тип момента (например, дата, событие)
    val dateTime: LocalDateTime // Поле для времени должно быть в типе LocalDateTime
)