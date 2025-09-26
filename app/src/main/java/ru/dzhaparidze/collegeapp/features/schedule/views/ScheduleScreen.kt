package ru.dzhaparidze.collegeapp.features.schedule.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.dzhaparidze.collegeapp.features.schedule.utils.GroupSubgroupCompatibility
import ru.dzhaparidze.collegeapp.features.schedule.utils.GroupsCatalog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen() {
    Column {
        Text(
            text = "Расписание",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(vertical = 80.dp)
        )

        var showGroupSheet by remember { mutableStateOf(false) }
        var showSubgroupSheet by remember { mutableStateOf(false) }
        var selectedGroup by remember { mutableStateOf("ИТ25-11") }
        var selectedSubgroup by remember { mutableStateOf("*") }

        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showGroupSheet = true },
                colors = CardDefaults.cardColors(
                    containerColor = androidx.compose.ui.graphics.Color.DarkGray
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
                            color = androidx.compose.ui.graphics.Color.White,
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
                    containerColor = androidx.compose.ui.graphics.Color.DarkGray
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
                            text = selectedSubgroup,
                            fontSize = 18.sp,
                            color = androidx.compose.ui.graphics.Color.White,
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

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
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
                                        selectedSubgroup = GroupSubgroupCompatibility.validatedSubgroup(selectedSubgroup, group)
                                        showGroupSheet = false
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = androidx.compose.ui.graphics.Color.LightGray
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
                                        showSubgroupSheet = false
                                    },
                                colors = CardDefaults.cardColors(
                                    containerColor = androidx.compose.ui.graphics.Color.LightGray
                                )
                            ) {
                                Text(
                                    text = subgroup,
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

