package turmaA.grupoB.LinkStage.ui.instituicao.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
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
import turmaA.grupoB.LinkStage.ui.aluno.chat.avatarColors
import turmaA.grupoB.LinkStage.ui.common.CommonTopBar
import turmaA.grupoB.LinkStage.ui.common.LinkStageDialog
import turmaA.grupoB.LinkStage.ui.common.LinkStageTabRow
import turmaA.grupoB.LinkStage.ui.common.SectionLabel
import turmaA.grupoB.LinkStage.ui.instituicao.InstituicaoRoutes
import turmaA.grupoB.LinkStage.ui.instituicao.InstitutionInternship
import turmaA.grupoB.LinkStage.ui.instituicao.InstitutionMentorItem
import turmaA.grupoB.LinkStage.ui.instituicao.InternshipStatus
import turmaA.grupoB.LinkStage.ui.instituicao.MentorStatus
import turmaA.grupoB.LinkStage.ui.instituicao.internshipStatusColor
import turmaA.grupoB.LinkStage.ui.instituicao.internshipStatusLabel
import turmaA.grupoB.LinkStage.ui.instituicao.mentorStatusColor
import turmaA.grupoB.LinkStage.ui.instituicao.mentorStatusLabel
import turmaA.grupoB.LinkStage.ui.instituicao.sampleInstitutionInternships
import turmaA.grupoB.LinkStage.ui.instituicao.sampleInstitutionMentors
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue

@Composable
fun ActivityInstituicaoScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }

    // Internship filters
    var filterInternshipStatus by rememberSaveable { mutableStateOf("") }
    var filterInternshipMentor by rememberSaveable { mutableStateOf("") }

    // Mentor filters
    var filterMentorStatus by rememberSaveable { mutableStateOf("") }
    var filterMentorInstitution by rememberSaveable { mutableStateOf("") }

    if (showFilterDialog) {
        when (selectedTab) {
            0 -> InternshipFilterDialog(
                currentStatus = filterInternshipStatus,
                currentMentor = filterInternshipMentor,
                onApply = { status, mentor ->
                    filterInternshipStatus = status
                    filterInternshipMentor = mentor
                    showFilterDialog = false
                },
                onDismiss = { showFilterDialog = false },
            )
            1 -> MentorFilterDialog(
                currentStatus = filterMentorStatus,
                currentInstitution = filterMentorInstitution,
                onApply = { status, institution ->
                    filterMentorStatus = status
                    filterMentorInstitution = institution
                    showFilterDialog = false
                },
                onDismiss = { showFilterDialog = false },
            )
        }
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { },
                containerColor = LightBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = if (selectedTab == 0) "Associar Estágio" else "Adicionar Orientador")
            }
        },
        containerColor = BackgroundLight,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            CommonTopBar()

            Text(
                text = "Atividade Recente",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                ),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp),
            )

            Spacer(modifier = Modifier.height(12.dp))

            SearchBarWithFilter(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onFilterClick = { showFilterDialog = true },
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinkStageTabRow(
                tabs = listOf("Estágios", "Orientadores"),
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it },
            )

            when (selectedTab) {
                0 -> InternshipsTab(
                    searchQuery = searchQuery,
                    filterStatus = filterInternshipStatus,
                    filterMentor = filterInternshipMentor,
                    navController = navController,
                )
                1 -> MentorsTab(
                    searchQuery = searchQuery,
                    filterStatus = filterMentorStatus,
                    filterInstitution = filterMentorInstitution,
                    navController = navController,
                )
            }
        }
    }
}

@Composable
private fun SearchBarWithFilter(
    query: String,
    onQueryChange: (String) -> Unit,
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
            placeholder = { Text("Pesquisar...", color = DarkGrey) },
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

// region Filter Dialogs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InternshipFilterDialog(
    currentStatus: String,
    currentMentor: String,
    onApply: (status: String, mentor: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var status by remember { mutableStateOf(currentStatus) }
    var mentor by remember { mutableStateOf(currentMentor) }
    var statusExpanded by remember { mutableStateOf(false) }

    val statusOptions = listOf(
        "Todos", "Em acompanhamento", "Por avaliar", "Concluído", "Sem orientador"
    )

    LinkStageDialog(
        title = "Filtros — Estágios",
        onConfirm = {
            onApply(
                if (status == "Todos") "" else status,
                mentor,
            )
        },
        onDismiss = onDismiss,
        confirmText = "Filtrar",
        dismissText = "Cancelar",
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionLabel("Estado")
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
                    SectionLabel("Orientador")
                    OutlinedTextField(
                        value = mentor,
                        onValueChange = { mentor = it },
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
    currentStatus: String,
    currentInstitution: String,
    onApply: (status: String, institution: String) -> Unit,
    onDismiss: () -> Unit,
) {
    var status by remember { mutableStateOf(currentStatus) }
    var institution by remember { mutableStateOf(currentInstitution) }
    var statusExpanded by remember { mutableStateOf(false) }

    val statusOptions = listOf("Todos", "Em acompanhamento", "Inativo", "Sem estágios")

    LinkStageDialog(
        title = "Filtros — Orientadores",
        onConfirm = {
            onApply(
                if (status == "Todos") "" else status,
                institution,
            )
        },
        onDismiss = onDismiss,
        confirmText = "Filtrar",
        dismissText = "Cancelar",
        content = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    SectionLabel("Estado")
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
                    SectionLabel("Instituição")
                    OutlinedTextField(
                        value = institution,
                        onValueChange = { institution = it },
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

// endregion

// region Tabs

@Composable
private fun InternshipsTab(
    searchQuery: String,
    filterStatus: String,
    filterMentor: String,
    navController: NavController,
) {
    val filtered = sampleInstitutionInternships.filter { internship ->
        val matchesSearch = searchQuery.isEmpty() ||
            internship.studentName.contains(searchQuery, ignoreCase = true) ||
            internship.offerTitle.contains(searchQuery, ignoreCase = true)
        val matchesStatus = filterStatus.isEmpty() || when (filterStatus) {
            "Em acompanhamento" -> internship.status == InternshipStatus.IN_PROGRESS
            "Por avaliar" -> internship.status == InternshipStatus.PENDING_REVIEW
            "Concluído" -> internship.status == InternshipStatus.COMPLETED
            "Sem orientador" -> internship.status == InternshipStatus.NO_MENTOR
            else -> true
        }
        val matchesMentor = filterMentor.isEmpty() ||
            internship.mentorName.contains(filterMentor, ignoreCase = true)
        matchesSearch && matchesStatus && matchesMentor
    }

    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
    ) {
        items(filtered, key = { it.id }) { internship ->
            InternshipCard(
                internship = internship,
                onClick = { navController.navigate(InstituicaoRoutes.internshipDetailRoute(internship.id)) },
            )
        }
    }
}

@Composable
private fun InternshipCard(internship: InstitutionInternship, onClick: () -> Unit = {}) {
    val statusColor = internshipStatusColor(internship.status)
    val statusLabel = internshipStatusLabel(internship.status)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(avatarColors[internship.studentAvatarColorIndex % avatarColors.size]),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = internship.studentAvatarInitials,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = internship.studentName,
                        color = DarkBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = internship.offerTitle,
                        color = DarkGrey,
                        fontSize = 12.sp,
                    )
                    Text(
                        text = if (internship.mentorName.isNotEmpty()) "Orientador: ${internship.mentorName}"
                               else "Orientador: Por definir",
                        color = DarkGrey,
                        fontSize = 11.sp,
                    )
                }
                StatusBadge(label = statusLabel, color = statusColor)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = DarkGrey,
                    modifier = Modifier.size(18.dp),
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = { internship.progressPercent / 100f },
                    modifier = Modifier
                        .weight(1f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = statusColor,
                    trackColor = BorderGrey,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${internship.progressPercent}% concluído",
                    color = DarkGrey,
                    fontSize = 10.sp,
                )
            }
        }
    }
}

@Composable
private fun MentorsTab(
    searchQuery: String,
    filterStatus: String,
    filterInstitution: String,
    navController: NavController,
) {
    val filtered = sampleInstitutionMentors.filter { mentor ->
        val matchesSearch = searchQuery.isEmpty() ||
            mentor.name.contains(searchQuery, ignoreCase = true) ||
            mentor.institution.contains(searchQuery, ignoreCase = true)
        val matchesStatus = filterStatus.isEmpty() || when (filterStatus) {
            "Em acompanhamento" -> mentor.status == MentorStatus.ACTIVE
            "Inativo" -> mentor.status == MentorStatus.INACTIVE
            "Sem estágios" -> mentor.status == MentorStatus.NO_STUDENTS
            else -> true
        }
        val matchesInstitution = filterInstitution.isEmpty() ||
            mentor.institution.contains(filterInstitution, ignoreCase = true)
        matchesSearch && matchesStatus && matchesInstitution
    }

    LazyColumn(
        contentPadding = PaddingValues(top = 8.dp, bottom = 16.dp),
    ) {
        items(filtered, key = { it.id }) { mentor ->
            MentorCard(
                mentor = mentor,
                onClick = { navController.navigate(InstituicaoRoutes.mentorDetailRoute(mentor.id)) },
            )
        }
    }
}

@Composable
private fun MentorCard(mentor: InstitutionMentorItem, onClick: () -> Unit = {}) {
    val statusColor = mentorStatusColor(mentor.status)
    val statusLabel = mentorStatusLabel(mentor.status)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(avatarColors[mentor.avatarColorIndex % avatarColors.size]),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = mentor.avatarInitials,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = mentor.name,
                    color = DarkBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )
                Text(
                    text = "Instituição: ${mentor.institution}",
                    color = DarkGrey,
                    fontSize = 12.sp,
                )
            }
            StatusBadge(label = statusLabel, color = statusColor)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = DarkGrey,
                modifier = Modifier.size(18.dp),
            )
        }
    }
}

// endregion

@Composable
private fun StatusBadge(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(
            text = label,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ActivityInstituicaoScreenPreview() {
    MaterialTheme {
        ActivityInstituicaoScreen(navController = rememberNavController())
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun ActivityInstituicaoScreenLandscapePreview() {
    MaterialTheme {
        ActivityInstituicaoScreen(navController = rememberNavController())
    }
}
