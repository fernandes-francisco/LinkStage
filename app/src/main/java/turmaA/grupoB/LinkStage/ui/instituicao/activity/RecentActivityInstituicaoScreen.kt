package turmaA.grupoB.LinkStage.ui.instituicao.activity

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import turmaA.grupoB.LinkStage.ui.common.LinkStageTabRow
import turmaA.grupoB.LinkStage.ui.instituicao.InstitutionInternship
import turmaA.grupoB.LinkStage.ui.instituicao.InstitutionMentorItem
import turmaA.grupoB.LinkStage.ui.instituicao.InternshipStatus
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
import turmaA.grupoB.LinkStage.ui.instituicao.InstituicaoRoutes
import turmaA.grupoB.LinkStage.ui.theme.LightBlue

@Composable
fun ActivityInstituicaoScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

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

            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
            )

            Spacer(modifier = Modifier.height(8.dp))

            LinkStageTabRow(
                tabs = listOf("Estágios", "Orientadores"),
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it },
            )

            when (selectedTab) {
                0 -> InternshipsTab(searchQuery = searchQuery, navController = navController)
                1 -> MentorsTab(searchQuery = searchQuery, navController = navController)
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
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
private fun InternshipsTab(searchQuery: String, navController: NavController) {
    val filtered = sampleInstitutionInternships.filter { internship ->
        searchQuery.isEmpty() ||
            internship.studentName.contains(searchQuery, ignoreCase = true) ||
            internship.offerTitle.contains(searchQuery, ignoreCase = true)
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
private fun MentorsTab(searchQuery: String, navController: NavController) {
    val filtered = sampleInstitutionMentors.filter { mentor ->
        searchQuery.isEmpty() ||
            mentor.name.contains(searchQuery, ignoreCase = true) ||
            mentor.institution.contains(searchQuery, ignoreCase = true)
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
