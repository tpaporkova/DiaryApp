package com.example.diaryapp.ui.home

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel : ViewModel() {

    private val _userName = MutableLiveData("")
    val userName: LiveData<String> get() = _userName

    private val _partnerName = MutableLiveData("")
    val partnerName: LiveData<String> get() = _partnerName

    private val _startDate = MutableLiveData("")
    val startDate: LiveData<String> get() = _startDate

    private val _daysTogether = MutableLiveData("")
    val daysTogether: LiveData<String> get() = _daysTogether

    private val _weeksTogether = MutableLiveData("")
    val weeksTogether: LiveData<String> get() = _weeksTogether

    private val _monthsTogether = MutableLiveData("")
    val monthsTogether: LiveData<String> get() = _monthsTogether

    private val _yearsTogether = MutableLiveData("")
    val yearsTogether: LiveData<String> get() = _yearsTogether

    private val _anniversaryIn = MutableLiveData("")
    val anniversaryIn: LiveData<String> get() = _anniversaryIn

    private val _profileImageUri = MutableLiveData<Uri?>()
    val profileImageUri: LiveData<Uri?> get() = _profileImageUri

    fun updateStartDate(startDate: String) {
        _startDate.value = startDate
        calculateRelationshipStats()
    }

    fun updateUserData(userName: String, partnerName: String, startDate: String) {
        _userName.value = userName
        _partnerName.value = partnerName
        _startDate.value = startDate
        calculateRelationshipStats()
    }

    fun updateProfileImage(uri: Uri?) {
        _profileImageUri.value = uri
    }

    private fun calculateRelationshipStats() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val startDateValue = _startDate.value ?: return

        try {
            val startDate = dateFormat.parse(startDateValue) ?: return
            val currentDate = Calendar.getInstance().time

            val diffInMillis = currentDate.time - startDate.time
            val days = (diffInMillis / (1000 * 60 * 60 * 24)).toInt()
            val weeks = days / 7
            val months = days / 30
            val years = days / 365

            val anniversary = Calendar.getInstance().apply {
                time = startDate
                add(Calendar.YEAR, years + 1)
            }.time
            val daysToAnniversary = ((anniversary.time - currentDate.time) / (1000 * 60 * 60 * 24)).toInt()

            _daysTogether.value = "Дней вместе: $days дн."
            _weeksTogether.value = "Недель вместе: $weeks нед."
            _monthsTogether.value = "Месяцев вместе: $months мес."
            _yearsTogether.value = "Лет вместе: $years лет."
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
