package com.example.diaryapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName

    private val _partnerName = MutableLiveData<String>()
    val partnerName: LiveData<String> get() = _partnerName

    private val _daysTogether = MutableLiveData<String>()
    val daysTogether: LiveData<String> get() = _daysTogether

    private val _anniversaryIn = MutableLiveData<String>()
    val anniversaryIn: LiveData<String> get() = _anniversaryIn

    private val _startDate = MutableLiveData<String>()
    val startDate: LiveData<String> get() = _startDate

    // Метод для обновления данных пользователя и вычисления дней
    fun updateUserData(userName: String, partnerName: String, startDate: String) {
        _userName.value = userName
        _partnerName.value = partnerName
        _startDate.value = startDate
        calculateDaysTogetherAndAnniversary()
    }

    // Метод для обновления только даты начала отношений
    fun updateStartDate(startDate: String) {
        _startDate.value = startDate
        calculateDaysTogetherAndAnniversary()
    }

    // Логика для вычисления количества дней вместе и дней до годовщины
    private fun calculateDaysTogetherAndAnniversary() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDateValue = _startDate.value ?: return

        try {
            val startDate = dateFormat.parse(startDateValue)
            val currentDate = Calendar.getInstance().time

            val diffInMillis = currentDate.time - startDate.time
            val daysTogether = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()

            // Количество дней до годовщины (через год)
            val anniversary = Calendar.getInstance().apply {
                time = startDate
                add(Calendar.YEAR, 1)
            }.time
            val diffToAnniversary = anniversary.time - currentDate.time
            val daysToAnniversary = (diffToAnniversary / (1000 * 60 * 60 * 24)).toInt()

            _daysTogether.value = "Дней вместе: $daysTogether дн."
            _anniversaryIn.value = "Годовщина через: $daysToAnniversary дн."
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}