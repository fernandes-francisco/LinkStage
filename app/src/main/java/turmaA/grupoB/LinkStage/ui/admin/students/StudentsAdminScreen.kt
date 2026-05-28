package turmaA.grupoB.LinkStage.ui.admin.students

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import turmaA.grupoB.LinkStage.ui.admin.AdminMentor
import turmaA.grupoB.LinkStage.ui.admin.AdminRoutes
import turmaA.grupoB.LinkStage.ui.admin.AdminStudent
import turmaA.grupoB.LinkStage.ui.admin.avatarColors
import turmaA.grupoB.LinkStage.ui.admin.sampleMentors
import turmaA.grupoB.LinkStage.ui.admin.sampleStudents
import turmaA.grupoB.LinkStage.ui.common.LinkStageLogo
import turmaA.grupoB.LinkStage.ui.common.LinkStageTabRow
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.MediumBlue

private val studentChipFilters = listOf("Todos", "Em estágio", "Sem estágio")
private val mentorChipFilters = listOf("Todos", "ESTG-IPVC", "ESE-IPVC")

@Composable
fun StudentsAdminScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = modifier,
        containerColor = BackgroundLight,
        topBar = {
            Column(modifier = Modifier.background(Color.White)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    LinkStageLogo()
                }

                Text(
                    text = if (selectedTab == 0) "Alunos" else "Orientadores",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
                )

                Spacer(modifier = Modifier.height(8.dp))

                LinkStageTabRow(
                    tabs = listOf("Alunos", "Orientadores"),
                    selectedIndex = selectedTab,
                    onTabSelected = { selectedTab = it },
                )
            }
        },
    ) { innerPadding ->
        when (selectedTab) {
            0 -> StudentsTabContent(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
            )
            1 -> MentorsTabContent(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

// region Students Tab

@Composable
private fun StudentsTabContent(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedChip by rememberSaveable { mutableStateOf("Todos") }
    var showAddDialog by remember { mutableStateOf(false) }

    val filtered = sampleStudents.filter { student ->
        val matchesSearch = searchQuery.isBlank() ||
            student.name.contains(searchQuery, ignoreCase = true) ||
            student.course.contains(searchQuery, ignoreCase = true) ||
            student.institution.contains(searchQuery, ignoreCase = true)
        val matchesChip = when (selectedChip) {
            "Em estágio" -> student.hasActiveInternship
            "Sem estágio" -> !student.hasActiveInternship
            else -> true
        }
        matchesSearch && matchesChip
    }

    val grouped = filtered
        .groupBy { it.institution }
        .mapValues { (_, students) -> students.groupBy { it.course } }

    val expandedInstitutions = remember {
        sampleStudents.map { it.institution }.distinct().map { it to true }.toMutableStateMap()
    }

    if (showAddDialog) {
        AddStudentDialog(onDismiss = { showAddDialog = false })
    }

    Scaffold(
        modifier = modifier,
        containerColor = BackgroundLight,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = DarkBlue,
                shape = CircleShape,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Aluno", tint = Color.White)
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SearchBarWithFilter(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = "Pesquisar alunos...",
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                QuickFilterChips(
                    filters = studentChipFilters,
                    selectedFilter = selectedChip,
                    onFilterSelected = { selectedChip = it },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            grouped.forEach { (institution, courseMap) ->
                val totalStudents = courseMap.values.sumOf { it.size }
                val isExpanded = expandedInstitutions[institution] == true

                item(key = "inst_$institution") {
                    InstitutionHeader(
                        name = institution,
                        count = totalStudents,
                        label = "alunos",
                        isExpanded = isExpanded,
                        onClick = {
                            expandedInstitutions[institution] = !isExpanded
                        },
                    )
                }

                if (isExpanded) {
                    courseMap.forEach { (course, students) ->
                        item(key = "course_${institution}_$course") {
                            CourseHeader(name = course, count = students.size)
                        }
                        items(students, key = { "student_${it.id}" }) { student ->
                            StudentListItem(
                                student = student,
                                onClick = {
                                    navController.navigate(AdminRoutes.studentDetail(student.id))
                                },
                            )
                        }
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
private fun CourseHeader(name: String, count: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.7f))
            .padding(start = 32.dp, end = 16.dp, top = 10.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            color = MediumBlue,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "$count",
            color = DarkGrey,
            fontSize = 12.sp,
        )
    }
}

@Composable
fun StudentListItem(
    student: AdminStudent,
    onClick: () -> Unit,
    showStatus: Boolean = true,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(avatarColors[student.avatarColorIndex % avatarColors.size]),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = student.avatarInitials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    color = DarkBlue,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
                Text(
                    text = student.email,
                    color = DarkGrey,
                    fontSize = 12.sp,
                )
            }
            if (showStatus) {
                Column(horizontalAlignment = Alignment.End) {
                    if (student.hasActiveInternship) {
                        Text(
                            text = "Em estágio",
                            color = LightBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                        )
                    } else {
                        Text(
                            text = "${student.applicationCount} candidaturas",
                            color = DarkGrey,
                            fontSize = 11.sp,
                        )
                    }
                }
            }
        }
        HorizontalDivider(color = BorderGrey)
    }
}

// endregion

// region Mentors Tab

@Composable
private fun MentorsTabContent(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedChip by rememberSaveable { mutableStateOf("Todos") }
    var showAddDialog by remember { mutableStateOf(false) }

    val filtered = sampleMentors.filter { mentor ->
        val matchesSearch = searchQuery.isBlank() ||
            mentor.name.contains(searchQuery, ignoreCase = true) ||
            mentor.email.contains(searchQuery, ignoreCase = true) ||
            mentor.institution.contains(searchQuery, ignoreCase = true)
        val matchesChip = when (selectedChip) {
            "Todos" -> true
            else -> mentor.institution == selectedChip
        }
        matchesSearch && matchesChip
    }

    val grouped = filtered.groupBy { it.institution }

    val expandedInstitutions = remember {
        sampleMentors.map { it.institution }.distinct().map { it to true }.toMutableStateMap()
    }

    if (showAddDialog) {
        AddMentorDialog(onDismiss = { showAddDialog = false })
    }

    Scaffold(
        modifier = modifier,
        containerColor = BackgroundLight,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = DarkBlue,
                shape = CircleShape,
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Orientador", tint = Color.White)
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SearchBarWithFilter(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = "Pesquisar orientadores...",
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                QuickFilterChips(
                    filters = mentorChipFilters,
                    selectedFilter = selectedChip,
                    onFilterSelected = { selectedChip = it },
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            grouped.forEach { (institution, mentors) ->
                val isExpanded = expandedInstitutions[institution] == true

                item(key = "mentor_inst_$institution") {
                    InstitutionHeader(
                        name = institution,
                        count = mentors.size,
                        label = "orientadores",
                        isExpanded = isExpanded,
                        onClick = {
                            expandedInstitutions[institution] = !isExpanded
                        },
                    )
                }

                if (isExpanded) {
                    items(mentors, key = { "mentor_${it.id}" }) { mentor ->
                        MentorListItem(
                            mentor = mentor,
                            onClick = {
                                navController.navigate(AdminRoutes.mentorDetail(mentor.id))
                            },
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun MentorListItem(
    mentor: AdminMentor,
    onClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(avatarColors[mentor.avatarColorIndex % avatarColors.size]),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = mentor.avatarInitials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mentor.name,
                    color = DarkBlue,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
                Text(
                    text = mentor.department,
                    color = DarkGrey,
                    fontSize = 12.sp,
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${mentor.activeStudentsCount} alunos",
                    color = LightBlue,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                )
                Text(
                    text = "ativos",
                    color = DarkGrey,
                    fontSize = 11.sp,
                )
            }
        }
        HorizontalDivider(color = BorderGrey)
    }
}

// endregion

// region Shared Components

@Composable
private fun SearchBarWithFilter(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text(placeholder, color = DarkGrey) },
            leadingIcon = {
                Icon(Icons.Outlined.Search, contentDescription = null, tint = DarkGrey)
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BorderGrey,
                focusedBorderColor = DarkBlue,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
            ),
            singleLine = true,
        )

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DarkBlue)
                .clickable { },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Outlined.FilterList,
                contentDescription = "Filtros",
                tint = Color.White,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@Composable
private fun QuickFilterChips(
    filters: List<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        filters.forEach { filter ->
            val isSelected = filter == selectedFilter
            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = filter,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        ),
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DarkBlue,
                    selectedLabelColor = Color.White,
                    containerColor = Color.White,
                    labelColor = DarkGrey,
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = isSelected,
                    borderColor = BorderGrey,
                    selectedBorderColor = Color.Transparent,
                ),
            )
        }
    }
}

@Composable
private fun InstitutionHeader(
    name: String,
    count: Int,
    label: String,
    isExpanded: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BackgroundLight)
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            color = DarkBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = "$count $label",
            color = DarkGrey,
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            imageVector = if (isExpanded) Icons.Outlined.KeyboardArrowUp
            else Icons.Outlined.KeyboardArrowDown,
            contentDescription = null,
            tint = DarkGrey,
            modifier = Modifier.size(20.dp),
        )
    }
}

// endregion

// region Dialogs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddStudentDialog(onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedInstitution by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var institutionExpanded by remember { mutableStateOf(false) }

    val institutions = listOf("ESTG-IPVC", "ESE-IPVC", "ESDL-IPVC")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Adicionar Aluno", fontWeight = FontWeight.Bold, color = DarkBlue)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                )
                ExposedDropdownMenuBox(
                    expanded = institutionExpanded,
                    onExpandedChange = { institutionExpanded = it },
                ) {
                    OutlinedTextField(
                        value = selectedInstitution,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Instituição") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = institutionExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(10.dp),
                    )
                    ExposedDropdownMenu(
                        expanded = institutionExpanded,
                        onDismissRequest = { institutionExpanded = false },
                    ) {
                        institutions.forEach { inst ->
                            DropdownMenuItem(
                                text = { Text(inst) },
                                onClick = {
                                    selectedInstitution = inst
                                    institutionExpanded = false
                                },
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = course,
                    onValueChange = { course = it },
                    label = { Text("Curso") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
            ) {
                Text("Adicionar", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkBlue),
            ) {
                Text("Cancelar")
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddMentorDialog(onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedInstitution by remember { mutableStateOf("") }
    var department by remember { mutableStateOf("") }
    var institutionExpanded by remember { mutableStateOf(false) }

    val institutions = listOf("ESTG-IPVC", "ESE-IPVC", "ESDL-IPVC")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Adicionar Orientador", fontWeight = FontWeight.Bold, color = DarkBlue)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nome") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                )
                ExposedDropdownMenuBox(
                    expanded = institutionExpanded,
                    onExpandedChange = { institutionExpanded = it },
                ) {
                    OutlinedTextField(
                        value = selectedInstitution,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Instituição") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = institutionExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                        shape = RoundedCornerShape(10.dp),
                    )
                    ExposedDropdownMenu(
                        expanded = institutionExpanded,
                        onDismissRequest = { institutionExpanded = false },
                    ) {
                        institutions.forEach { inst ->
                            DropdownMenuItem(
                                text = { Text(inst) },
                                onClick = {
                                    selectedInstitution = inst
                                    institutionExpanded = false
                                },
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = department,
                    onValueChange = { department = it },
                    label = { Text("Departamento") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true,
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
            ) {
                Text("Adicionar", color = Color.White)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkBlue),
            ) {
                Text("Cancelar")
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp),
    )
}

// endregion

@Preview(showSystemUi = true)
@Composable
private fun StudentsAdminScreenPreview() {
    MaterialTheme {
        StudentsAdminScreen(navController = rememberNavController())
    }
}
