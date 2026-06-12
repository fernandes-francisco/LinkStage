package turmaA.grupoB.LinkStage.ui.admin.students

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.lifecycle.viewmodel.compose.viewModel
import turmaA.grupoB.LinkStage.ui.admin.AdminMentor
import turmaA.grupoB.LinkStage.ui.admin.AdminRoutes
import turmaA.grupoB.LinkStage.ui.admin.AdminStudent
import turmaA.grupoB.LinkStage.ui.admin.avatarColors
import turmaA.grupoB.LinkStage.ui.admin.sampleMentors
import turmaA.grupoB.LinkStage.ui.admin.sampleStudents
import turmaA.grupoB.LinkStage.viewmodel.admin.AdminUsersUiState
import turmaA.grupoB.LinkStage.viewmodel.admin.AdminUsersViewModel
import turmaA.grupoB.LinkStage.viewmodel.admin.AdminUsersViewModelFactory
import turmaA.grupoB.LinkStage.ui.common.CommonTopBar
import turmaA.grupoB.LinkStage.ui.common.LinkStageDialog
import turmaA.grupoB.LinkStage.ui.common.LinkStageTabRow
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.MediumBlue

@Composable
fun StudentsAdminScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val usersViewModel: AdminUsersViewModel = viewModel(factory = AdminUsersViewModelFactory())
    val usersUiState by usersViewModel.uiState.collectAsState()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }

    // State for filters
    var studentFilterStatus by rememberSaveable { mutableStateOf("") }
    var studentFilterInstitution by rememberSaveable { mutableStateOf("") }
    var studentFilterCourse by rememberSaveable { mutableStateOf("") }

    var mentorFilterInstitution by rememberSaveable { mutableStateOf("") }
    var mentorFilterDepartment by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        usersViewModel.loadUsers()
    }

    LaunchedEffect(searchQuery, studentFilterStatus, studentFilterInstitution, studentFilterCourse) {
        usersViewModel.filterStudents(
            query = searchQuery,
            institution = studentFilterInstitution,
            course = studentFilterCourse,
            internshipStatus = studentFilterStatus,
        )
    }

    LaunchedEffect(searchQuery, mentorFilterInstitution, mentorFilterDepartment) {
        usersViewModel.filterMentors(
            query = searchQuery,
            institution = mentorFilterInstitution,
            department = mentorFilterDepartment,
        )
    }

    if (showFilterDialog) {
        if (selectedTab == 0) {
                StudentFilterDialog(
                    currentStatus = studentFilterStatus,
                    currentInstitution = studentFilterInstitution,
                    currentCourse = studentFilterCourse,
                    institutionOptions = filteredStudentInstitutions(usersUiState),
                    courseOptions = filteredStudentCourses(usersUiState),
                    onApply = { status, institution, course ->
                    studentFilterStatus = status
                    studentFilterInstitution = institution
                    studentFilterCourse = course
                    showFilterDialog = false
                },
                onDismiss = { showFilterDialog = false },
            )
        } else {
            MentorFilterDialog(
                currentInstitution = mentorFilterInstitution,
                currentDepartment = mentorFilterDepartment,
                institutionOptions = filteredMentorInstitutions(usersUiState),
                onApply = { institution, department ->
                    mentorFilterInstitution = institution
                    mentorFilterDepartment = department
                    showFilterDialog = false
                },
                onDismiss = { showFilterDialog = false },
            )
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = BackgroundLight,
        topBar = {
            Column(modifier = Modifier.background(BackgroundLight)) {
                CommonTopBar()

                Text(
                    text = "Utilizadores",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                    ),
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                )

                Spacer(modifier = Modifier.height(8.dp))
                SearchBarWithFilter(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = if (selectedTab == 0) "Pesquisar alunos..." else "Pesquisar orientadores...",
                    onFilterClick = { showFilterDialog = true },
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
                searchQuery = searchQuery,
                filterStatus = studentFilterStatus,
                filterInstitution = studentFilterInstitution,
                filterCourse = studentFilterCourse,
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
            )
            1 -> MentorsTabContent(
                navController = navController,
                searchQuery = searchQuery,
                filterInstitution = mentorFilterInstitution,
                filterDepartment = mentorFilterDepartment,
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
            )
        }
    }
}

private fun filteredStudentInstitutions(uiState: AdminUsersUiState): List<String> = when (uiState) {
    is AdminUsersUiState.Success -> uiState.students.map { it.institution }.distinct()
    is AdminUsersUiState.StudentsSuccess -> uiState.students.map { it.institution }.distinct()
    else -> sampleStudents.map { it.institution }.distinct()
}

private fun filteredStudentCourses(uiState: AdminUsersUiState): List<String> = when (uiState) {
    is AdminUsersUiState.Success -> uiState.students.map { it.course }.distinct()
    is AdminUsersUiState.StudentsSuccess -> uiState.students.map { it.course }.distinct()
    else -> sampleStudents.map { it.course }.distinct()
}

private fun filteredMentorInstitutions(uiState: AdminUsersUiState): List<String> = when (uiState) {
    is AdminUsersUiState.Success -> uiState.mentors.map { it.institution }.distinct()
    is AdminUsersUiState.MentorsSuccess -> uiState.mentors.map { it.institution }.distinct()
    else -> sampleMentors.map { it.institution }.distinct()
}

// region Students Tab

@Composable
private fun StudentsTabContent(
    navController: NavController,
    searchQuery: String,
    filterStatus: String,
    filterInstitution: String,
    filterCourse: String,
    modifier: Modifier = Modifier,
) {
    var showAddDialog by remember { mutableStateOf(false) }

    val usersUiState by viewModel<AdminUsersViewModel>(factory = AdminUsersViewModelFactory()).uiState.collectAsState()
    val filtered = when (val state = usersUiState) {
        is AdminUsersUiState.StudentsSuccess -> state.students
        is AdminUsersUiState.Success -> state.students
        else -> emptyList()
    }

    val grouped = filtered
        .groupBy { it.institution }
        .mapValues { (_, students) -> students.groupBy { it.course } }

    val expandedInstitutions = remember(filtered) {
        filtered.map { it.institution }.distinct().map { it to true }.toMutableStateMap()
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
                containerColor = LightBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Aluno")
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
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
    searchQuery: String,
    filterInstitution: String,
    filterDepartment: String,
    modifier: Modifier = Modifier,
) {
    var showAddDialog by remember { mutableStateOf(false) }

    val usersUiState by viewModel<AdminUsersViewModel>(factory = AdminUsersViewModelFactory()).uiState.collectAsState()
    val filtered = when (val state = usersUiState) {
        is AdminUsersUiState.MentorsSuccess -> state.mentors
        is AdminUsersUiState.Success -> state.mentors
        else -> emptyList()
    }

    val grouped = filtered.groupBy { it.institution }

    val expandedInstitutions = remember(filtered) {
        filtered.map { it.institution }.distinct().map { it to true }.toMutableStateMap()
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
                containerColor = LightBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Orientador")
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
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
    onFilterClick: () -> Unit,
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
                .clickable { onFilterClick() },
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
private fun StudentFilterDialog(
    currentStatus: String,
    currentInstitution: String,
    currentCourse: String,
    institutionOptions: List<String>,
    courseOptions: List<String>,
    onApply: (status: String, institution: String, course: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var status by remember { mutableStateOf(currentStatus) }
    var institution by remember { mutableStateOf(currentInstitution) }
    var course by remember { mutableStateOf(currentCourse) }
    var statusExpanded by remember { mutableStateOf(false) }
    var institutionExpanded by remember { mutableStateOf(false) }

    val statusOptions = listOf("Todos", "Em estágio", "Sem estágio")
    val institutionOptions = institutionOptions.ifEmpty { sampleStudents.map { it.institution }.distinct() }
    val courseOptions = courseOptions.ifEmpty { sampleStudents.map { it.course }.distinct() }

    LinkStageDialog(
        title = "Filtros",
        onConfirm = {
            onApply(
                if (status == "Todos") "" else status,
                institution,
                course,
            )
        },
        onDismiss = onDismiss,
        confirmText = "Filtrar",
        dismissText = "Cancelar",
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    turmaA.grupoB.LinkStage.ui.common.SectionLabel("Estado")
                    ExposedDropdownMenuBox(
                        expanded = statusExpanded,
                        onExpandedChange = { statusExpanded = it },
                    ) {
                        OutlinedTextField(
                            value = status.ifEmpty { "Todos" },
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LightBlue,
                                unfocusedBorderColor = BorderGrey,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                            ),
                            singleLine = true,
                        )
                        ExposedDropdownMenu(
                            expanded = statusExpanded,
                            onDismissRequest = { statusExpanded = false },
                        ) {
                            statusOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        status = if (option == "Todos") "" else option
                                        statusExpanded = false
                                    },
                                )
                            }
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    turmaA.grupoB.LinkStage.ui.common.SectionLabel("Instituição")
                    ExposedDropdownMenuBox(
                        expanded = institutionExpanded,
                        onExpandedChange = { institutionExpanded = it },
                    ) {
                        OutlinedTextField(
                            value = institution.ifEmpty { "Todas" },
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = institutionExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LightBlue,
                                unfocusedBorderColor = BorderGrey,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                            ),
                            singleLine = true,
                        )
                        ExposedDropdownMenu(
                            expanded = institutionExpanded,
                            onDismissRequest = { institutionExpanded = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text("Todas") },
                                onClick = {
                                    institution = ""
                                    institutionExpanded = false
                                },
                            )
                            institutionOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        institution = option
                                        institutionExpanded = false
                                    },
                                )
                            }
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    turmaA.grupoB.LinkStage.ui.common.SectionLabel("Curso")
                    OutlinedTextField(
                        value = course,
                        onValueChange = { course = it },
                        placeholder = { Text("Escreva aqui.", color = DarkGrey, fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LightBlue,
                            unfocusedBorderColor = BorderGrey,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                        ),
                        singleLine = true,
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MentorFilterDialog(
    currentInstitution: String,
    currentDepartment: String,
    institutionOptions: List<String>,
    onApply: (institution: String, department: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var institution by remember { mutableStateOf(currentInstitution) }
    var department by remember { mutableStateOf(currentDepartment) }
    var institutionExpanded by remember { mutableStateOf(false) }

    val institutionOptions = institutionOptions.ifEmpty { sampleMentors.map { it.institution }.distinct() }

    LinkStageDialog(
        title = "Filtros",
        onConfirm = { onApply(institution, department) },
        onDismiss = onDismiss,
        confirmText = "Filtrar",
        dismissText = "Cancelar",
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    turmaA.grupoB.LinkStage.ui.common.SectionLabel("Instituição")
                    ExposedDropdownMenuBox(
                        expanded = institutionExpanded,
                        onExpandedChange = { institutionExpanded = it },
                    ) {
                        OutlinedTextField(
                            value = institution.ifEmpty { "Todas" },
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = institutionExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = LightBlue,
                                unfocusedBorderColor = BorderGrey,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                            ),
                            singleLine = true,
                        )
                        ExposedDropdownMenu(
                            expanded = institutionExpanded,
                            onDismissRequest = { institutionExpanded = false },
                        ) {
                            DropdownMenuItem(
                                text = { Text("Todas") },
                                onClick = {
                                    institution = ""
                                    institutionExpanded = false
                                },
                            )
                            institutionOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        institution = option
                                        institutionExpanded = false
                                    },
                                )
                            }
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    turmaA.grupoB.LinkStage.ui.common.SectionLabel("Departamento")
                    OutlinedTextField(
                        value = department,
                        onValueChange = { department = it },
                        placeholder = { Text("Escreva aqui.", color = DarkGrey, fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = LightBlue,
                            unfocusedBorderColor = BorderGrey,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                        ),
                        singleLine = true,
                    )
                }
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddStudentDialog(onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var selectedInstitution by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var institutionExpanded by remember { mutableStateOf(false) }

    val institutions = listOf("ESTG-IPVC", "ESE-IPVC", "ESDL-IPVC")

    LinkStageDialog(
        onDismiss = onDismiss,
        title = "Adicionar Aluno",
        onConfirm = onDismiss,
        confirmText = "Adicionar",
        dismissText = "Cancelar",
        content = {
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
        }
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

    LinkStageDialog(
        onDismiss = onDismiss,
        title = "Adicionar Orientador",
        onConfirm = onDismiss,
        confirmText = "Adicionar",
        dismissText = "Cancelar",
        content = {
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
        }
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
