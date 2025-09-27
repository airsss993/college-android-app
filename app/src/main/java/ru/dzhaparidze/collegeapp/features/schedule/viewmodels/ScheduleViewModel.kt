package ru.dzhaparidze.collegeapp.features.schedule.viewmodels

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import ru.dzhaparidze.collegeapp.features.schedule.data.ScheduleRepositoryInterface
import ru.dzhaparidze.collegeapp.features.schedule.models.ScheduleEvent
import ru.dzhaparidze.collegeapp.features.schedule.utils.GroupsCatalog
import ru.dzhaparidze.collegeapp.features.schedule.views.TimePeriod
import java.text.SimpleDateFormat
import java.util.*

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

    private fun getWeekStart(weeksOffset: Int = 0): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.firstDayOfWeek = java.util.Calendar.MONDAY
        calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.MONDAY)
        calendar.add(java.util.Calendar.WEEK_OF_YEAR, weeksOffset)
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(calendar.time)
    }

    private fun getWeekEnd(weeksOffset: Int = 0): String {
        val calendar = java.util.Calendar.getInstance()
        calendar.firstDayOfWeek = java.util.Calendar.MONDAY
        calendar.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SUNDAY)
        calendar.add(java.util.Calendar.WEEK_OF_YEAR, weeksOffset)
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(calendar.time)
    }

    private suspend fun hasEventsInPeriod(startDate: String, endDate: String): Boolean {
        return try {
            val events = repository.getSchedule(
                group = selectedGroup,
                subgroup = selectedSubgroup,
                start = startDate,
                end = endDate
            )
            events.isNotEmpty()
        } catch (e: Exception) {
            false
        }
    }

    var selectedGroup by mutableStateOf(getLatestGroup())
    var selectedSubgroup by mutableStateOf("*")
    var selectedStartDate by mutableStateOf(getCurrentDate())
    var selectedEndDate by mutableStateOf(getDatePlusDays(2))
    var selectedWeekOffset by mutableStateOf(0)

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

    fun updateSchedule(
        group: String? = null,
        subgroup: String? = null,
        timePeriod: TimePeriod? = null,
        startDate: String? = null,
        endDate: String? = null
    ) {
        group?.let { selectedGroup = it }
        subgroup?.let { selectedSubgroup = it }

        timePeriod?.let { period ->
            when (period) {
                TimePeriod.TODAY -> {
                    val today = getCurrentDate()
                    selectedStartDate = today
                    selectedEndDate = today
                }
                TimePeriod.THREE_DAYS -> {
                    val today = getCurrentDate()
                    selectedStartDate = today
                    selectedEndDate = getDatePlusDays(2)
                }
                TimePeriod.WEEK -> {
                    viewModelScope.launch {
                        val currentWeekStart = getWeekStart(0)
                        val currentWeekEnd = getWeekEnd(0)
                        val nextWeekStart = getWeekStart(1)
                        val nextWeekEnd = getWeekEnd(1)

                        val calendar = java.util.Calendar.getInstance()
                        val today = calendar.get(java.util.Calendar.DAY_OF_WEEK)

                        val useNextWeek = when {
                            today == java.util.Calendar.MONDAY -> true

                            today == java.util.Calendar.SATURDAY || today == java.util.Calendar.SUNDAY -> {
                                hasEventsInPeriod(nextWeekStart, nextWeekEnd)
                            }

                            else -> !hasEventsInPeriod(currentWeekStart, currentWeekEnd)
                        }

                        if (useNextWeek) {
                            selectedWeekOffset = 1
                            selectedStartDate = nextWeekStart
                            selectedEndDate = nextWeekEnd
                        } else {
                            selectedWeekOffset = 0
                            selectedStartDate = currentWeekStart
                            selectedEndDate = currentWeekEnd
                        }

                        loadSchedule()
                    }
                    return
                }
            }
        }

        startDate?.let { selectedStartDate = it }
        endDate?.let { selectedEndDate = it }

        loadSchedule()
    }
}