package com.example.diaryapp.ui.moment

import android.app.Application
import androidx.lifecycle.*
import com.example.diaryapp.data.local.AppDatabase
import com.example.diaryapp.data.local.entities.Moment
import com.example.diaryapp.data.repository.MomentRepository
import kotlinx.coroutines.launch

class MomentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MomentRepository =
        MomentRepository(AppDatabase.getDatabase(application).momentDao())

    // Используем LiveData для моментов
    val allMoments: LiveData<List<Moment>> = repository.allMoments

    private val _currentMoment = MutableLiveData<Moment?>()
    val currentMoment: LiveData<Moment?> = _currentMoment

    fun loadMoment(id: Long) {
        viewModelScope.launch {
            _currentMoment.value = repository.getMomentById(id)
        }
    }

    fun insert(moment: Moment) = viewModelScope.launch {
        repository.addMoment(moment)
    }

    fun update(moment: Moment) = viewModelScope.launch {
        repository.updateMoment(moment)
    }

    fun delete(moment: Moment) = viewModelScope.launch {
        repository.deleteMoment(moment)
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MomentViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MomentViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}