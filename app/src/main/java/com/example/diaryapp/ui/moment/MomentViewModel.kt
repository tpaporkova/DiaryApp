package com.example.diaryapp.ui.moment

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.diaryapp.data.local.AppDatabase
import com.example.diaryapp.data.local.entities.Moment
import com.example.diaryapp.data.repository.MomentRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.asLiveData

class MomentViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MomentRepository(AppDatabase.getDatabase(application).momentDao())

    val allMoments: LiveData<List<Moment>> = repository.allMoments.asLiveData()

    private val _currentMoment = MutableLiveData<Moment?>()
    val currentMoment: LiveData<Moment?> = _currentMoment

    private val _insertionSuccess = MutableLiveData<Boolean?>()
    val insertionSuccess: LiveData<Boolean?> = _insertionSuccess

    fun loadMoment(id: Long) {
        viewModelScope.launch {
            try {
                _currentMoment.value = repository.getMomentById(id)
                Log.d("MomentViewModel", "Loaded moment with ID: $id")
            } catch (e: Exception) {
                Log.e("MomentViewModel", "Error loading moment", e)
            }
        }
    }

    fun insert(moment: Moment) = viewModelScope.launch {
        try {
            repository.insertMoment(moment)
            Log.d("MomentViewModel", "Moment inserted: $moment")
        } catch (e: Exception) {
            Log.e("MomentViewModel", "Error inserting moment: ${e.message}", e)
        }
    }

    fun update(moment: Moment) = viewModelScope.launch {
        try {
            repository.updateMoment(moment)
            Log.d("MomentViewModel", "Moment updated: $moment")  // Логируем обновление момента
        } catch (e: Exception) {
            Log.e("MomentViewModel", "Error updating moment", e)
        }
    }

    fun resetInsertionStatus() {
        _insertionSuccess.value = null
    }

    fun delete(moment: Moment) = viewModelScope.launch {
        try {
            repository.deleteMoment(moment)
            Log.d("MomentViewModel", "Moment deleted: $moment")
        } catch (e: Exception) {
            Log.e("MomentViewModel", "Error deleting moment", e)
        }
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
