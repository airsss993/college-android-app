package ru.dzhaparidze.collegeapp.features.schedule.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.dzhaparidze.collegeapp.features.schedule.data.ScheduleRepositoryInterface
import ru.dzhaparidze.collegeapp.features.schedule.models.ScheduleEvent
import ru.dzhaparidze.collegeapp.features.schedule.utils.GroupsCatalog
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ScheduleViewModel(private val repository: ScheduleRepositoryInterface) : ViewModel() {
    private val _events = MutableStateFlow<List<ScheduleEvent>>(emptyList())
    val events = _events.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private fun getLatestGroup(): String {
        val availableGroups = GroupsCatalog.allGroups
        return availableGroups.filter { it.contains("ИТ") }.maxBy { group ->
            val year = group.substringAfter("ИТ").substringBefore("-")
            year.toIntOrNull() ?: 0
        }
    }

    private fun getCurrentDate(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(currentDate)
    }

    private fun getDatePlusDays(i: Int): String {
        val currentDate = Date()
        val calendar = java.util.Calendar.getInstance()
        calendar.time = currentDate
        calendar.add(java.util.Calendar.DAY_OF_MONTH, i)
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(calendar.time)
    }

    var selectedGroup by mutableStateOf(getLatestGroup())
    var selectedSubgroup by mutableStateOf("*")
    var selectedStartDate by mutableStateOf(getCurrentDate())
    var selectedEndDate by mutableStateOf(getDatePlusDays(2))

    fun loadSchedule() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val scheduleEvents = repository.getSchedule(
                    group = selectedGroup,
                    subgroup = selectedSubgroup,
                    start = selectedStartDate,
                    end = selectedEndDate
                )
                _events.value = scheduleEvents
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "failed to load schedule"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateSelectedGroup(group: String) {
        selectedGroup = group
        loadSchedule()
    }

    fun updateSelectedSubgroup(subgroup: String) {
        selectedSubgroup = subgroup
        loadSchedule()
    }

    fun updateSelectedDates(startDate: String, endDate: String) {
        selectedStartDate = startDate
        selectedEndDate = endDate
        loadSchedule()
    }

    fun clearError() {
        _errorMessage.value = null
    }
}