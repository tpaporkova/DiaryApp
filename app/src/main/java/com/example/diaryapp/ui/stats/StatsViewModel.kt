package com.example.diaryapp.ui.stats

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.diaryapp.data.local.AppDatabase
import com.example.diaryapp.data.repository.MomentRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class StatsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MomentRepository(AppDatabase.getDatabase(application).momentDao())

    private val _stats = MutableLiveData<String>()
    val stats: LiveData<String> = _stats

    init {
        calculateStats()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun calculateStats() {
        viewModelScope.launch {
            // Получаем список моментов из репозитория
            repository.allMoments.observeForever { moments ->
                moments?.let {
                    val now = LocalDateTime.now()
                    val weekAgo = now.minusWeeks(1)
                    val monthAgo = now.minusMonths(1)
                    val quarterAgo = now.minusMonths(3)
                    val yearAgo = now.minusYears(1)

                    // Подсчитываем количество моментов, попадающих в каждую категорию
                    val weekly = moments.count { it.dateTime.isAfter(weekAgo) }
                    val monthly = moments.count { it.dateTime.isAfter(monthAgo) }
                    val quarterly = moments.count { it.dateTime.isAfter(quarterAgo) }
                    val yearly = moments.count { it.dateTime.isAfter(yearAgo) }

                    // Обновляем статистику
                    _stats.postValue(
                        "Last week: $weekly moments\n" +
                                "Last month: $monthly moments\n" +
                                "Last quarter: $quarterly moments\n" +
                                "Last year: $yearly moments"
                    )
                }
            }
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(StatsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return StatsViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}