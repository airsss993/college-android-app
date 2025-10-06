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
import ru.dzhaparidze.collegeapp.features.schedule.utils.DateFormatters
import ru.dzhaparidze.collegeapp.features.schedule.utils.GroupSubgroupCompatibility
import ru.dzhaparidze.collegeapp.features.schedule.utils.GroupsCatalog
import ru.dzhaparidze.collegeapp.features.schedule.utils.getFullSubgroupName
import ru.dzhaparidze.collegeapp.features.schedule.viewmodels.ScheduleViewModel
import java.util.*

enum class TimePeriod(val displayName: String) {
    TODAY("Сегодня"),
    THREE_DAYS("3 дня"),
    WEEK("Неделя")
}

fun getDateRangeText(timePeriod: TimePeriod, weekOffset: Int = 0): String {
    val calendar = Calendar.getInstance()
    val today = calendar.time

    return when (timePeriod) {
        TimePeriod.TODAY -> {
            DateFormatters.uiDate.format(today)
        }

        TimePeriod.THREE_DAYS -> {
            calendar.add(Calendar.DAY_OF_MONTH, 2)
            val endDate = calendar.time

            "${DateFormatters.uiDate.format(today)} - ${DateFormatters.uiDate.format(endDate)}"
        }

        TimePeriod.WEEK -> {
            calendar.firstDayOfWeek = Calendar.MONDAY
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            } else {
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
            }

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
    var customStartDate by remember { mutableStateOf<String?>(null) }
    var customEndDate by remember { mutableStateOf<String?>(null) }

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
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Text(
            text = "Расписание",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onBackground,
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
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Группа",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedGroup,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showSubgroupSheet = true },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Подгруппа",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (selectedSubgroup == "*") "Все" else getFullSubgroupName(
                                selectedSubgroup
                            ),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                        customStartDate = null
                        customEndDate = null
                        viewModel.updateSchedule(timePeriod = TimePeriod.TODAY)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTimePeriod == TimePeriod.TODAY && customStartDate == null)
                            MaterialTheme.colorScheme.onSecondary
                        else
                            androidx.compose.ui.graphics.Color.Transparent,
                        contentColor = if (selectedTimePeriod == TimePeriod.TODAY && customStartDate == null)
                            androidx.compose.ui.graphics.Color.White
                        else
                            MaterialTheme.colorScheme.onSurface
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
                        customStartDate = null
                        customEndDate = null
                        viewModel.updateSchedule(timePeriod = TimePeriod.THREE_DAYS)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTimePeriod == TimePeriod.THREE_DAYS && customStartDate == null)
                            MaterialTheme.colorScheme.onSecondary
                        else
                            androidx.compose.ui.graphics.Color.Transparent,
                        contentColor = if (selectedTimePeriod == TimePeriod.THREE_DAYS && customStartDate == null)
                            androidx.compose.ui.graphics.Color.White
                        else
                            MaterialTheme.colorScheme.onSurface
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
                        customStartDate = null
                        customEndDate = null
                        viewModel.updateSchedule(timePeriod = TimePeriod.WEEK)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedTimePeriod == TimePeriod.WEEK && customStartDate == null)
                            MaterialTheme.colorScheme.onSecondary
                        else
                            androidx.compose.ui.graphics.Color.Transparent,
                        contentColor = if (selectedTimePeriod == TimePeriod.WEEK && customStartDate == null)
                            androidx.compose.ui.graphics.Color.White
                        else
                            MaterialTheme.colorScheme.onSurface
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
                        containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondary
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
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (customStartDate != null && customEndDate != null) {
                    "${
                        DateFormatters.uiDate.format(
                            java.text.SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).parse(customStartDate!!)!!
                        )
                    } - ${
                        DateFormatters.uiDate.format(
                            java.text.SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.getDefault()
                            ).parse(customEndDate!!)!!
                        )
                    }"
                } else {
                    getDateRangeText(selectedTimePeriod, viewModel.selectedWeekOffset)
                },
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }

        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Загрузка расписания...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Произошла ошибка, попробуйте еще раз",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { viewModel.loadSchedule() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onSecondary,
                                contentColor = androidx.compose.ui.graphics.Color.White
                            ),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(
                                horizontal = 24.dp,
                                vertical = 12.dp
                            )
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Повторить попытку",
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
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
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
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
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "На выбранные даты занятия не найдены",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                                        containerColor = MaterialTheme.colorScheme.onSecondary,
                                        contentColor = androidx.compose.ui.graphics.Color.White
                                    ),
                                    shape = RoundedCornerShape(20.dp),
                                    contentPadding = PaddingValues(
                                        horizontal = 20.dp,
                                        vertical = 8.dp
                                    )
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
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
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
                containerColor = MaterialTheme.colorScheme.background,
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
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurface
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
                                    containerColor = MaterialTheme.colorScheme.surface
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
                containerColor = MaterialTheme.colorScheme.background,
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
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                    contentColor = MaterialTheme.colorScheme.onSurface
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
                                    containerColor = MaterialTheme.colorScheme.surface
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

        if (showDatePicker) {
            ModalBottomSheet(
                onDismissRequest = { showDatePicker = false },
                sheetState = rememberModalBottomSheetState(),
                containerColor = MaterialTheme.colorScheme.background,
                dragHandle = null
            ) {
                DatePickerContent(
                    onDatesSelected = { startDate, endDate ->
                        customStartDate = startDate
                        customEndDate = endDate
                        viewModel.updateSchedule(
                            startDate = startDate,
                            endDate = endDate
                        )
                        showDatePicker = false
                    },
                    onDismiss = { showDatePicker = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerContent(
    onDatesSelected: (String, String) -> Unit,
    onDismiss: () -> Unit,
) {
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }
    var selectingStartDate by remember { mutableStateOf(true) }

    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                elevation = ButtonDefaults.buttonElevation(2.dp, 2.dp, 0.dp, 2.dp, 2.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Отмена", fontSize = 16.sp)
            }

            Text(
                text = "Выбор периода",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Button(
                onClick = {
                    if (startDate != null && endDate != null) {
                        val start =
                            java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                Date(startDate!!)
                            )
                        val end =
                            java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                Date(endDate!!)
                            )
                        onDatesSelected(start, end)
                    }
                },
                enabled = startDate != null && endDate != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ),
                elevation = ButtonDefaults.buttonElevation(2.dp, 2.dp, 0.dp, 2.dp, 2.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text("Применить", fontSize = 16.sp)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { selectingStartDate = true },
                colors = CardDefaults.cardColors(
                    containerColor = if (selectingStartDate)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Начало",
                        fontSize = 12.sp,
                        color = if (selectingStartDate)
                            MaterialTheme.colorScheme.surface
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (startDate != null)
                            DateFormatters.uiDate.format(Date(startDate!!))
                        else
                            "Не выбрано",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (selectingStartDate)
                            MaterialTheme.colorScheme.surface
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { selectingStartDate = false },
                colors = CardDefaults.cardColors(
                    containerColor = if (!selectingStartDate)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Конец",
                        fontSize = 12.sp,
                        color = if (!selectingStartDate)
                            MaterialTheme.colorScheme.surface
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = if (endDate != null)
                            DateFormatters.uiDate.format(Date(endDate!!))
                        else
                            "Не выбрано",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (!selectingStartDate)
                            MaterialTheme.colorScheme.surface
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        CompactDatePicker(
            state = datePickerState,
            modifier = Modifier.fillMaxWidth()
        )

        LaunchedEffect(datePickerState.selectedDateMillis) {
            datePickerState.selectedDateMillis?.let { selectedMillis ->
                if (selectingStartDate) {
                    startDate = selectedMillis
                    selectingStartDate = false
                } else {
                    endDate = selectedMillis
                    if (startDate != null && selectedMillis < startDate!!) {
                        endDate = startDate
                        startDate = selectedMillis
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompactDatePicker(
    state: DatePickerState,
    modifier: Modifier = Modifier,
) {
    DatePicker(
        state = state,
        modifier = modifier
            .height(400.dp)
            .padding(horizontal = 8.dp),
        title = null,
        headline = null,
        showModeToggle = false,
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}


