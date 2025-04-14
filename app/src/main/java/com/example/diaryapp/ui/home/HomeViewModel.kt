package com.example.diaryapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {

    private val _userName = MutableLiveData<String>("")
    val userName: LiveData<String> get() = _userName

    private val _partnerName = MutableLiveData<String>("")
    val partnerName: LiveData<String> get() = _partnerName

    private val _daysTogether = MutableLiveData<String>("")
    val daysTogether: LiveData<String> get() = _daysTogether

    private val _weeksTogether = MutableLiveData<String>("")
    val weeksTogether: LiveData<String> get() = _weeksTogether

    private val _monthsTogether = MutableLiveData<String>("")
    val monthsTogether: LiveData<String> get() = _monthsTogether

    private val _yearsTogether = MutableLiveData<String>("")
    val yearsTogether: LiveData<String> get() = _yearsTogether

    private val _anniversaryIn = MutableLiveData<String>("")
    val anniversaryIn: LiveData<String> get() = _anniversaryIn

    private val _startDate = MutableLiveData<String>("")
    val startDate: LiveData<String> get() = _startDate

    // Отдельные методы для обновления
    fun updateUserName(name: String) {
        _userName.value = name
    }

    fun updatePartnerName(name: String) {
        _partnerName.value = name
    }

    fun updateStartDate(startDate: String) {
        _startDate.value = startDate
        calculateRelationshipStats()
    }

    // Метод для обновления всех данных (если нужно сразу)
    fun updateUserData(userName: String, partnerName: String, startDate: String) {
        _userName.value = userName
        _partnerName.value = partnerName
        _startDate.value = startDate
        calculateRelationshipStats()
    }

    private fun calculateRelationshipStats() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDateValue = _startDate.value ?: return

        try {
            val startDate = dateFormat.parse(startDateValue)
            val currentDate = Calendar.getInstance().time

            val diffInMillis = currentDate.time - startDate.time
            val daysTogether = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
            val weeksTogether = (daysTogether / 7).toInt()
            val monthsTogether = (daysTogether / 30).toInt()
            val yearsTogether = (daysTogether / 365).toInt()

            val anniversary = Calendar.getInstance().apply {
                time = startDate
                add(Calendar.YEAR, 1)
            }.time
            val diffToAnniversary = anniversary.time - currentDate.time
            val daysToAnniversary = (diffToAnniversary / (1000 * 60 * 60 * 24)).toInt()

            _daysTogether.value = "Дней вместе: $daysTogether дн."
            _weeksTogether.value = "Недель вместе: $weeksTogether нед."
            _monthsTogether.value = "Месяцев вместе: $monthsTogether мес."
            _yearsTogether.value = "Лет вместе: $yearsTogether лет."
            _anniversaryIn.value = "Годовщина через: $daysToAnniversary дн."
        } catch (e: Exception) {
            _daysTogether.value = ""
            _weeksTogether.value = ""
            _monthsTogether.value = ""
            _yearsTogether.value = ""
            _anniversaryIn.value = ""
            e.printStackTrace()
        }
    }
}
