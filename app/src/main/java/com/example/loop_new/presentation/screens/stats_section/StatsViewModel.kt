package com.example.loop_new.presentation.screens.stats_section

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loop_new.domain.model.firebase.Statistics
import com.example.loop_new.domain.model.firebase.StatsSummary
import com.example.loop_new.domain.services.FirebaseService
import kotlinx.coroutines.launch

class StatsViewModel(private val firebaseService: FirebaseService) : ViewModel() {

    private val _statistics = MutableLiveData<Statistics>()

    val statistics: LiveData<Statistics>
        get() = _statistics

    private val _statsSummary = MutableLiveData<StatsSummary>()
    val statsSummary: LiveData<StatsSummary>
        get() = _statsSummary

    init {
        fetchDataOfStats()
    }

    private fun fetchDataOfStats() {
        viewModelScope.launch {
            try {
                val (statistics, statsSummary) = firebaseService.fetchDataOfStats()
                _statistics.postValue(statistics)
                _statsSummary.postValue(statsSummary)
            } catch (e: Exception) {
                println("Error fetching data: $e")
                // Obsłuż błędy
            }
        }
    }
}