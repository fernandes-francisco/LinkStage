package turmaA.grupoB.LinkStage.ui.admin.institutions

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import turmaA.grupoB.LinkStage.ui.admin.sampleInstitutions
import turmaA.grupoB.LinkStage.ui.admin.sampleMentors
import turmaA.grupoB.LinkStage.ui.admin.sampleStudents
import turmaA.grupoB.LinkStage.ui.admin.students.DeleteConfirmDialog
import turmaA.grupoB.LinkStage.ui.admin.students.MentorListItem
import turmaA.grupoB.LinkStage.ui.admin.students.StudentListItem
import turmaA.grupoB.LinkStage.ui.common.ContentSection
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.Red

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InstitutionDetailAdminScreen(
    institutionId: String,
    onBack: () -> Unit,
) {
    val institution = sampleInstitutions.find { it.id == institutionId } ?: run {
        onBack()
        return
    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    val relatedStudents = sampleStudents.filter {
        it.institutionCode == institution.code
    }.take(3)

    val relatedMentors = sampleMentors.filter {
        it.institution == institution.code
    }

    if (showDeleteDialog) {
        DeleteConfirmDialog(
            title = "Remover Instituição",
            message = "Tens a certeza que queres remover \"${institution.name}\"? Esta ação não pode ser revertida.",
            onConfirm = {
                showDeleteDialog = false
                onBack()
            },
            onDismiss = { showDeleteDialog = false },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalhes da Instituição",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = DarkBlue,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = DarkBlue,
                        )
                    }
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
            // Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(institution.logoColor),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = institution.logoInitial,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = institution.name,
                            color = DarkBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                        Text(
                            text = institution.code,
                            color = DarkGrey,
                            fontSize = 13.sp,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(LightBlue)
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text = institution.type,
                                color = DarkBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                            )
                        }
                    }
                }
            }

            // Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                StatCard(
                    value = "${institution.studentsCount}",
                    label = "Alunos",
                    valueColor = DarkBlue,
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    value = "${institution.mentorsCount}",
                    label = "Orientadores",
                    valueColor = DarkBlue,
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    value = "${institution.activeInternshipsCount}",
                    label = "Estágios ativos",
                    valueColor = LightBlue,
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Contact Information
            ContentSection(title = "Informação de Contacto") {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                    DetailRow("Email", institution.email)
                    DetailRow("Website", institution.website)
                    DetailRow("Localização", institution.location)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Recent Students
            ContentSection(title = "Alunos Recentes") {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    if (relatedStudents.isEmpty()) {
                        Text(
                            text = "Sem alunos registados",
                            color = DarkGrey,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    } else {
                        relatedStudents.forEach { student ->
                            StudentListItem(
                                student = student,
                                onClick = { },
                                showStatus = false,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Mentors
            ContentSection(title = "Orientadores") {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    if (relatedMentors.isEmpty()) {
                        Text(
                            text = "Sem orientadores registados",
                            color = DarkGrey,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        )
                    } else {
                        relatedMentors.forEach { mentor ->
                            MentorListItem(
                                mentor = mentor,
                                onClick = { },
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Remove Button
            Button(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Red,
                    contentColor = Color.White,
                ),
            ) {
                Text("Remover Instituição", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatCard(
    value: String,
    label: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = value,
                color = valueColor,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                color = DarkGrey,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(text = label, color = DarkGrey, fontSize = 13.sp)
        Text(text = value, color = DarkBlue, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun InstitutionDetailPreview() {
    MaterialTheme {
        InstitutionDetailAdminScreen(institutionId = "i1", onBack = {})
    }
}
