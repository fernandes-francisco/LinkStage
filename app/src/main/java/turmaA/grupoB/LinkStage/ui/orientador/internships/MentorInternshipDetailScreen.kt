package turmaA.grupoB.LinkStage.ui.orientador.internships

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import turmaA.grupoB.LinkStage.ui.admin.AdminStudent
import turmaA.grupoB.LinkStage.ui.admin.avatarColors
import turmaA.grupoB.LinkStage.ui.aluno.offers.BenefitChip
import turmaA.grupoB.LinkStage.ui.aluno.offers.MetaChip
import turmaA.grupoB.LinkStage.ui.aluno.offers.ResponsibilityItem
import turmaA.grupoB.LinkStage.ui.common.CheckItem
import turmaA.grupoB.LinkStage.ui.common.ContentSection
import turmaA.grupoB.LinkStage.ui.common.ContentSectionColored
import turmaA.grupoB.LinkStage.ui.orientador.MentorInternship
import turmaA.grupoB.LinkStage.ui.orientador.OrientadorRoutes
import turmaA.grupoB.LinkStage.ui.orientador.sampleMentorInternships
import turmaA.grupoB.LinkStage.ui.orientador.sampleMentorStudents
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.Fade1
import turmaA.grupoB.LinkStage.ui.theme.LightBlue

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MentorInternshipDetailScreen(
    internshipId: String = "i1",
    navController: NavController,
    internship: MentorInternship = sampleMentorInternships.find { it.id == internshipId }
        ?: sampleMentorInternships.first(),
    student: AdminStudent = sampleMentorStudents.find { it.id == internship.studentId }
        ?: sampleMentorStudents.first(),
) {
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = DarkBlue,
                        )
                    }
                },
                title = {
                    Text(
                        text = "Estágios Envolvidos",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = DarkBlue,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
            )
        },
        containerColor = BackgroundLight,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(internship.logoColor),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = internship.logoInitial,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column {
                    Text(
                        text = internship.offerTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                    )
                    Text(
                        text = internship.institutionName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LightBlue,
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Meta chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                MetaChip(
                    icon = Icons.Outlined.LocationOn,
                    label = "Localização",
                    value = internship.location,
                    modifier = Modifier.weight(1f),
                )
                MetaChip(
                    icon = Icons.Outlined.Schedule,
                    label = "Duração",
                    value = internship.duration,
                    modifier = Modifier.weight(1f),
                )
                MetaChip(
                    icon = Icons.Outlined.Work,
                    label = "Tipo",
                    value = internship.type,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sobre a empresa
            if (internship.aboutCompany.isNotEmpty()) {
                ContentSection(title = "Sobre a empresa") {
                    Text(
                        text = internship.aboutCompany,
                        fontSize = 14.sp,
                        color = DarkGrey,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Responsabilidades
            if (internship.responsibilities.isNotEmpty()) {
                ContentSection(title = "Responsabilidades") {
                    internship.responsibilities.forEach { item ->
                        ResponsibilityItem(text = item)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Requisitos
            if (internship.requirements.isNotEmpty()) {
                ContentSectionColored(title = "Requisitos") {
                    internship.requirements.forEach { item ->
                        CheckItem(text = item)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Benefícios
            if (internship.benefits.isNotEmpty()) {
                ContentSection(title = "Benefícios") {
                    FlowRow(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        internship.benefits.forEach { benefit ->
                            BenefitChip(text = benefit)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Aluno estagiário
            InternshipStudentSection(student = student, navController = navController)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InternshipStudentSection(
    student: AdminStudent,
    navController: NavController,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 14.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(18.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Fade1),
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Aluno estagiário",
                    color = LightBlue,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = BorderGrey)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(OrientadorRoutes.mentorStudentDetail(student.id))
                    }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
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

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                ) {
                    Text(
                        text = student.name,
                        color = DarkBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = student.institutionCode,
                        color = DarkGrey,
                        fontSize = 12.sp,
                    )
                }

                TextButton(
                    onClick = {
                        navController.navigate(OrientadorRoutes.mentorStudentDetail(student.id))
                    },
                ) {
                    Text(
                        text = "Mais",
                        color = LightBlue,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                    )
                    Icon(
                        Icons.AutoMirrored.Outlined.ArrowForward,
                        contentDescription = null,
                        tint = LightBlue,
                        modifier = Modifier
                            .size(16.dp)
                            .padding(start = 2.dp),
                    )
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MentorInternshipDetailPreview() {
    MaterialTheme {
        MentorInternshipDetailScreen(navController = rememberNavController())
    }
}

@Preview(
    showSystemUi = true,
    device = "spec:width=891dp,height=411dp,orientation=landscape",
)
@Composable
private fun MentorInternshipDetailLandscapePreview() {
    MaterialTheme {
        MentorInternshipDetailScreen(navController = rememberNavController())
    }
}
