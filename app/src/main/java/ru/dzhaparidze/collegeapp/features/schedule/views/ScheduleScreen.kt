package ru.dzhaparidze.collegeapp.features.schedule.views

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import androidx.core.content.edit
import ru.dzhaparidze.collegeapp.features.schedule.data.ScheduleAPI
import ru.dzhaparidze.collegeapp.features.schedule.data.ScheduleRepository
import ru.dzhaparidze.collegeapp.features.schedule.utils.GroupSubgroupCompatibility
import ru.dzhaparidze.collegeapp.features.schedule.utils.GroupsCatalog
import ru.dzhaparidze.collegeapp.features.schedule.viewmodels.ScheduleViewModel
import ru.dzhaparidze.collegeapp.features.schedule.utils.DateFormatters
import ru.dzhaparidze.collegeapp.features.schedule.utils.getFullSubgroupName
import java.util.*

enum class TimePeriod(val displayName: String) {
    TODAY("Сегодня"),
    THREE_DAYS("3 дня"),
    WEEK("Неделя")
}

fun getDateRangeText(timePeriod: TimePeriod, weekOffset: Int = 0): String {
    val today = Date()
    val calendar = Calendar.getInstance()

    return when (timePeriod) {
        TimePeriod.TODAY -> {
            DateFormatters.uiDate.format(today)
        }

        TimePeriod.THREE_DAYS -> {
            calendar.time = today
            calendar.add(Calendar.DAY_OF_MONTH, 2)
            val endDate = calendar.time

            "${DateFormatters.uiDate.format(today)} - ${DateFormatters.uiDate.format(endDate)}"
        }

        TimePeriod.WEEK -> {
            calendar.time = today
            calendar.firstDayOfWeek = Calendar.MONDAY
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            calendar.add(Calendar.WEEK_OF_YEAR, weekOffset)
            val startOfWeek = calendar.time

            calendar.add(Calendar.DAY_OF_MONTH, 6)
            val endOfWeek = calendar.time

            "${DateFormatters.uiDate.format(startOfWeek)} - ${DateFormatters.uiDate.format(endOfWeek)}"
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen() {
    val scheduleAPI = ScheduleAPI()
    val repository = ScheduleRepository(scheduleAPI)
    val viewModel = remember { ScheduleViewModel(repository) }
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("schedule_settings", android.content.Context.MODE_PRIVATE)
    }

    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showGroupSheet by remember { mutableStateOf(false) }
    var showSubgroupSheet by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf("") }
    var selectedSubgroup by remember { mutableStateOf("") }
    var selectedTimePeriod by remember { mutableStateOf(TimePeriod.TODAY) }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        selectedGroup = prefs.getString("selected_group", "ИТ25-11") ?: "ИТ25-11"
        selectedSubgroup = prefs.getString("selected_subgroup", "*") ?: "*"

        viewModel.updateSchedule(
            group = selectedGroup,
            subgroup = selectedSubgroup,
            timePeriod = selectedTimePeriod
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = "Расписание",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(top = 86.dp, bottom = 8.dp, start = 16.dp)
        )

        // ВЫПАДАЮЩИЕ СПИСКИ С ГРУППАМИ
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showGroupSheet = true },
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Группа",
                        fontSize = 12.sp,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedGroup,
                            fontSize = 18.sp,
                            color = androidx.compose.ui.graphics.Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.Gray
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showSubgroupSheet = true },
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Подгруппа",
                        fontSize = 12.sp,
                        color = androidx.compose.ui.graphics.Color.Gray
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedSubgroup == "*") "Все" else getFullSubgroupName(
                                selectedSubgroup
                            ),
                            fontSize = 18.sp,
                            color = androidx.compose.ui.graphics.Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.Gray
                        )
                    }
                }
            }
        }

        // КНОПКИ ВЫБОРА ДАТЫ
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            contentPadding = PaddingValues(start = 16.dp, bottom = 16.dp)
        ) {
            item {
                Button(
                    onClick = {
                        selectedTimePeriod = TimePeriod.TODAY
                        viewModel.updateSchedule(timePeriod = TimePeriod.TODAY)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTimePeriod == TimePeriod.TODAY)
                            androidx.compose.ui.graphics.Color(0xFF3B86F6)
                        else
                            androidx.compose.ui.graphics.Color.Transparent,
                        contentColor = if (selectedTimePeriod == TimePeriod.TODAY)
                            androidx.compose.ui.graphics.Color.White
                        else
                            androidx.compose.ui.graphics.Color.Gray
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Сегодня",
                        fontSize = 15.sp
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        selectedTimePeriod = TimePeriod.THREE_DAYS
                        viewModel.updateSchedule(timePeriod = TimePeriod.THREE_DAYS)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTimePeriod == TimePeriod.THREE_DAYS)
                            androidx.compose.ui.graphics.Color(0xFF3B86F6)
                        else
                            androidx.compose.ui.graphics.Color.Transparent,
                        contentColor = if (selectedTimePeriod == TimePeriod.THREE_DAYS)
                            androidx.compose.ui.graphics.Color.White
                        else
                            androidx.compose.ui.graphics.Color.Gray
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    Text(
                        text = "3 дня",
                        fontSize = 15.sp
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        selectedTimePeriod = TimePeriod.WEEK
                        viewModel.updateSchedule(timePeriod = TimePeriod.WEEK)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTimePeriod == TimePeriod.WEEK)
                            androidx.compose.ui.graphics.Color(0xFF3B86F6)
                        else
                            androidx.compose.ui.graphics.Color.Transparent,
                        contentColor = if (selectedTimePeriod == TimePeriod.WEEK)
                            androidx.compose.ui.graphics.Color.White
                        else
                            androidx.compose.ui.graphics.Color.Gray
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    Text(
                        text = "Неделя",
                        fontSize = 15.sp
                    )
                }
            }

            item {
                Button(
                    onClick = { showDatePicker = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFFDCE6F7),
                        contentColor = androidx.compose.ui.graphics.Color(0xFF3B86F6)
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp, 0.dp, 0.dp, 0.dp, 0.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "Выбрать",
                            fontSize = 15.sp
                        )
                    }
                }
            }
        }

        // ОТОБРАЖЕНИЕ ТЕКУЩЕЙ ДАТЫ
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.DateRange,
                contentDescription = null,
                tint = androidx.compose.ui.graphics.Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = getDateRangeText(selectedTimePeriod, viewModel.selectedWeekOffset),
                fontSize = 14.sp,
                color = androidx.compose.ui.graphics.Color.Gray,
                fontWeight = FontWeight.Medium
            )
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = errorMessage!!,
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                }
            }

            else -> {
                if (events.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.EventBusy,
                                contentDescription = null,
                                modifier = Modifier.size(80.dp),
                                tint = androidx.compose.ui.graphics.Color.Gray
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = when (selectedTimePeriod) {
                                    TimePeriod.TODAY -> "Нет занятий"
                                    TimePeriod.THREE_DAYS -> "В ближайшие 3 дня нет занятий"
                                    TimePeriod.WEEK -> "На этой неделе нет занятий"
                                },
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = androidx.compose.ui.graphics.Color.Black,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "На выбранные даты занятия не найдены",
                                style = MaterialTheme.typography.bodyMedium,
                                color = androidx.compose.ui.graphics.Color.Gray,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (selectedTimePeriod != TimePeriod.WEEK) {
                                Button(
                                    onClick = {
                                        selectedTimePeriod = TimePeriod.WEEK
                                        viewModel.updateSchedule(timePeriod = TimePeriod.WEEK)
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = androidx.compose.ui.graphics.Color(0xFF3B86F6),
                                        contentColor = androidx.compose.ui.graphics.Color.White
                                    ),
                                    shape = RoundedCornerShape(20.dp),
                                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Default.CalendarMonth,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "Показать неделю",
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        val eventsByDay = events.groupBy { it.day }

                        items(eventsByDay.entries.toList()) { (day, dayEvents) ->
                            DayScheduleCard(day = day, events = dayEvents)
                        }
                    }
                }
            }
        }

        if (showGroupSheet) {
            ModalBottomSheet(
                onDismissRequest = { showGroupSheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = androidx.compose.ui.graphics.Color(0xFFF2F2F6),
                dragHandle = null
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 36.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Button(
                                onClick = { showGroupSheet = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = androidx.compose.ui.graphics.Color(0xFFFBFBFF),
                                    contentColor = androidx.compose.ui.graphics.Color.Black
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    2.dp,
                                    2.dp,
                                    0.dp,
                                    2.dp,
                                    2.dp
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Отмена",
                                    fontSize = 16.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier.weight(2f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Выбор группы",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }

                        Box(modifier = Modifier.weight(1f))
                    }

                    LazyColumn {
                        items(GroupsCatalog.allGroups) { group ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        selectedGroup = group
                                        val validatedSubgroup =
                                            GroupSubgroupCompatibility.validatedSubgroup(
                                                selectedSubgroup, group
                                            )
                                        selectedSubgroup = validatedSubgroup

                                        prefs.edit {
                                            putString("selected_group", group)
                                            putString("selected_subgroup", validatedSubgroup)
                                        }

                                        viewModel.updateSchedule(
                                            group = group,
                                            subgroup = validatedSubgroup
                                        )
                                        showGroupSheet = false
                                    }, colors = CardDefaults.cardColors(
                                    containerColor = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
                                )
                            ) {
                                Text(
                                    text = group,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showSubgroupSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSubgroupSheet = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = androidx.compose.ui.graphics.Color(0xFFF2F2F6),
                dragHandle = null
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 36.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Button(
                                onClick = { showSubgroupSheet = false },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = androidx.compose.ui.graphics.Color(0xFFFBFBFF),
                                    contentColor = androidx.compose.ui.graphics.Color.Black
                                ),
                                elevation = ButtonDefaults.buttonElevation(
                                    2.dp,
                                    2.dp,
                                    0.dp,
                                    2.dp,
                                    2.dp
                                ),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Отмена",
                                    fontSize = 16.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier.weight(2f),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Выбор подгруппы",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }

                        Box(modifier = Modifier.weight(1f))
                    }

                    LazyColumn {
                        items(GroupSubgroupCompatibility.availableSubgroups(selectedGroup)) { subgroup ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        selectedSubgroup = subgroup
                                        prefs.edit { putString("selected_subgroup", subgroup) }
                                        viewModel.updateSchedule(subgroup = subgroup)
                                        showSubgroupSheet = false
                                    }, colors = CardDefaults.cardColors(
                                    containerColor = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
                                )
                            ) {
                                Text(
                                    text = if (subgroup == "*") "Все" else getFullSubgroupName(
                                        subgroup
                                    ),
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


