package turmaA.grupoB.LinkStage.ui.instituicao.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import turmaA.grupoB.LinkStage.ui.aluno.chat.avatarColors
import turmaA.grupoB.LinkStage.ui.common.ConfirmationDialog
import turmaA.grupoB.LinkStage.ui.common.SecondaryTopBar
import turmaA.grupoB.LinkStage.ui.instituicao.InstitutionInternship
import turmaA.grupoB.LinkStage.ui.instituicao.InternshipOrigin
import turmaA.grupoB.LinkStage.ui.instituicao.InternshipStatus
import turmaA.grupoB.LinkStage.ui.instituicao.internshipStatusColor
import turmaA.grupoB.LinkStage.ui.instituicao.internshipStatusLabel
import turmaA.grupoB.LinkStage.ui.instituicao.sampleInstitutionInternships
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue

@Composable
fun InternshipDetailInstituicaoScreen(
    internshipId: String,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val internship = sampleInstitutionInternships.find { it.id == internshipId }
        ?: sampleInstitutionInternships.first()

    var showEvaluateDialog by remember { mutableStateOf(false) }

    if (showEvaluateDialog) {
        ConfirmationDialog(
            title = "Estágio não concluído",
            body = "O estágio ainda está em progresso. Tem a certeza que pretende iniciar a avaliação?",
            confirmLabel = "Continuar",
            onConfirm = {
                showEvaluateDialog = false
            },
            onDismiss = { showEvaluateDialog = false },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight),
    ) {
        SecondaryTopBar(
            title = "Detalhes de estágio",
            onBack = { navController.popBackStack() },
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            InternshipInfoCard(internship = internship)

            Spacer(modifier = Modifier.height(16.dp))

            // Activity section
            Text(
                text = "Atividade Recente",
                color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))

            ActivityPlaceholderCard("Reunião de acompanhamento", "Concluído", LightBlue)
            ActivityPlaceholderCard("Entrega do relatório parcial", "Pendente", Color(0xFFF5C518))
            ActivityPlaceholderCard("Apresentação de progresso", "Agendado", DarkGrey)

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom action bar
        Surface(color = Color.White, shadowElevation = 8.dp) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                ) {
                    Icon(Icons.Outlined.CalendarMonth, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver atividades", fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = {
                        if (internship.status != InternshipStatus.COMPLETED) {
                            showEvaluateDialog = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                        brush = androidx.compose.ui.graphics.SolidColor(DarkBlue),
                    ),
                ) {
                    Icon(Icons.Outlined.RateReview, contentDescription = null, tint = DarkBlue, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Avaliar estágio", color = DarkBlue, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun InternshipInfoCard(internship: InstitutionInternship) {
    val statusColor = internshipStatusColor(internship.status)
    val statusLabel = internshipStatusLabel(internship.status)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(avatarColors[internship.studentAvatarColorIndex % avatarColors.size]),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        internship.studentAvatarInitials,
                        color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(internship.studentName, color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text(internship.offerTitle, color = DarkGrey, fontSize = 13.sp)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(statusColor.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                ) {
                    Text(statusLabel, color = statusColor, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                }
            }

            HorizontalDivider(color = BorderGrey, modifier = Modifier.padding(horizontal = 14.dp))

            // Metadata grid
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Origem", color = DarkGrey, fontSize = 11.sp)
                    Text(
                        if (internship.origin == InternshipOrigin.SCHOOL) "Escola" else "Empresa",
                        color = DarkBlue, fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Orientador escolar", color = DarkGrey, fontSize = 11.sp)
                    Text(
                        internship.schoolMentorName.ifEmpty { "Por definir" },
                        color = DarkBlue, fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
                    )
                }
            }
            if (internship.origin == InternshipOrigin.COMPANY) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Orientador da empresa", color = DarkGrey, fontSize = 11.sp)
                        Text(
                            internship.companyMentorName.ifEmpty { "Por definir" },
                            color = DarkBlue, fontWeight = FontWeight.SemiBold, fontSize = 13.sp,
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Data de início", color = DarkGrey, fontSize = 11.sp)
                    Text("01 Set 2026", color = DarkBlue, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Progresso", color = DarkGrey, fontSize = 11.sp)
                    Text("${internship.progressPercent}%", color = DarkBlue, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { internship.progressPercent / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .padding(horizontal = 14.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = LightBlue,
                trackColor = BorderGrey,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Relatório final:", color = DarkGrey, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF4CAF50).copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                ) {
                    Text("Pendente", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun ActivityPlaceholderCard(
    title: String,
    status: String,
    statusColor: Color,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = DarkBlue, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(statusColor.copy(alpha = 0.15f))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            ) {
                Text(status, color = statusColor, fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
        }
    }
}

// region Previews

@Preview(showSystemUi = true)
@Composable
private fun InternshipDetailInstituicaoScreenPreview() {
    MaterialTheme {
        InternshipDetailInstituicaoScreen(internshipId = "int1", navController = rememberNavController())
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun InternshipDetailInstituicaoScreenLandscapePreview() {
    MaterialTheme {
        InternshipDetailInstituicaoScreen(internshipId = "int1", navController = rememberNavController())
    }
}

// endregion
