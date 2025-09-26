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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.core.content.edit
import ru.dzhaparidze.collegeapp.features.schedule.utils.GroupSubgroupCompatibility
import ru.dzhaparidze.collegeapp.features.schedule.utils.GroupsCatalog

enum class TimePeriod(val displayName: String) {
    TODAY("Сегодня"),
    THREE_DAYS("3 дня"),
    WEEK("Неделя")
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen() {
    val context = LocalContext.current
    val prefs = remember {
        context.getSharedPreferences("schedule_settings", android.content.Context.MODE_PRIVATE)
    }

    var showGroupSheet by remember { mutableStateOf(false) }
    var showSubgroupSheet by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf("") }
    var selectedSubgroup by remember { mutableStateOf("") }
    var selectedTimePeriod by remember { mutableStateOf(TimePeriod.TODAY) }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        selectedGroup = prefs.getString("selected_group", "ИТ25-11") ?: "ИТ25-11"
        selectedSubgroup = prefs.getString("selected_subgroup", "*") ?: "*"
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
                            text = if (selectedSubgroup == "*") "Все" else selectedSubgroup,
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
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(start = 16.dp)
        ) {
            item {
                Button(
                    onClick = { selectedTimePeriod = TimePeriod.TODAY },
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
                        fontSize = 14.sp
                    )
                }
            }

            item {
                Button(
                    onClick = { selectedTimePeriod = TimePeriod.THREE_DAYS },
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
                        fontSize = 14.sp
                    )
                }
            }

            item {
                Button(
                    onClick = { selectedTimePeriod = TimePeriod.WEEK },
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
                        fontSize = 14.sp
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
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {

        }

        if (showGroupSheet) {
            ModalBottomSheet(
                onDismissRequest = { showGroupSheet = false },
                sheetState = rememberModalBottomSheetState()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Выберите группу",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyColumn {
                        items(GroupsCatalog.allGroups) { group ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        selectedGroup = group
                                        selectedSubgroup =
                                            GroupSubgroupCompatibility.validatedSubgroup(
                                                selectedSubgroup, group
                                            )
                                        prefs.edit {
                                            putString("selected_group", group)
                                            putString("selected_subgroup", selectedSubgroup)
                                        }
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
                sheetState = rememberModalBottomSheetState()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Выберите подгруппу",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyColumn {
                        items(GroupSubgroupCompatibility.availableSubgroups(selectedGroup)) { subgroup ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        selectedSubgroup = subgroup
                                        prefs.edit { putString("selected_subgroup", subgroup) }
                                        showSubgroupSheet = false
                                    }, colors = CardDefaults.cardColors(
                                    containerColor = androidx.compose.ui.graphics.Color(0xFFFFFFFF)
                                )
                            ) {
                                Text(
                                    text = if (subgroup == "*") "Все" else subgroup,
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

