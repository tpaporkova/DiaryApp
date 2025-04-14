package com.example.diaryapp.ui.stats

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.diaryapp.data.local.AppDatabase
import com.example.diaryapp.data.repository.MomentRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class StatsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MomentRepository(AppDatabase.getDatabase(application).momentDao())

    private val _stats = MutableLiveData<String>()
    val stats: LiveData<String> = _stats

    init {
        calculateStats()
    }

    private fun calculateStats() {
        viewModelScope.launch {
            repository.allMoments.collectLatest { moments ->
                val formatter = DateTimeFormatter.ISO_LOCAL_DATE
                val now = LocalDate.now()

                val intervals = mapOf(
                    "Неделя" to now.minusWeeks(1),
                    "Месяц" to now.minusMonths(1),
                    "Квартал" to now.minusMonths(3),
                    "Год" to now.minusYears(1)
                )

                val types = listOf("Свидание", "Кино", "Театр", "Путешествие", "Сюрприз", "Цветы")

                val result = StringBuilder()

                for ((intervalName, startDate) in intervals) {
                    result.append("$intervalName:\n")
                    for (type in types) {
                        val count = moments.count { moment ->
                            try {
                                val momentDate = LocalDate.parse(moment.date, formatter)
                                momentDate.isAfter(startDate) && moment.type == type
                            } catch (e: Exception) {
                                false
                            }
                        }
                        result.append("  $type: $count\n")
                    }
                    result.append("\n")
                }

                _stats.postValue(result.toString().trim())
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
