package turmaA.grupoB.LinkStage.ui.instituicao.activity

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import turmaA.grupoB.LinkStage.ui.admin.AdminMentor
import turmaA.grupoB.LinkStage.ui.admin.sampleMentors
import turmaA.grupoB.LinkStage.ui.aluno.chat.avatarColors
import turmaA.grupoB.LinkStage.ui.common.ConfirmationDialog
import turmaA.grupoB.LinkStage.ui.common.SecondaryTopBar
import turmaA.grupoB.LinkStage.ui.instituicao.InstituicaoRoutes
import turmaA.grupoB.LinkStage.ui.instituicao.sampleInstitutionInternships
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.Fade1
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.Red

@Composable
fun AssignMentorInstituicaoScreen(
    mentorId: String,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val internship = sampleInstitutionInternships.firstOrNull() ?: return
    var selectedMentor by remember { mutableStateOf<AdminMentor?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog && selectedMentor != null) {
        ConfirmationDialog(
            title = "Atribuir orientador?",
            body = "Tem a certeza que pretende atribuir ${selectedMentor!!.name} a este estágio?",
            confirmLabel = "Atribuir",
            onConfirm = {
                showConfirmDialog = false
                navController.navigate(InstituicaoRoutes.MENTOR_ASSIGNED_SUCCESS) {
                    popUpTo(InstituicaoRoutes.assignMentorRoute(mentorId)) { inclusive = true }
                }
            },
            onDismiss = { showConfirmDialog = false },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight),
    ) {
        SecondaryTopBar(
            title = "Atribuir orientador",
            onBack = { navController.popBackStack() },
        )

        // Selected internship/student card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
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
                        .background(Fade1),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        internship.studentAvatarInitials,
                        color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(internship.studentName, color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(internship.offerTitle, color = DarkGrey, fontSize = 13.sp)
                }
            }
        }

        Text(
            text = "Orientadores disponíveis",
            color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(bottom = 8.dp),
        ) {
            items(sampleMentors, key = { it.id }) { mentor ->
                MentorSelectionCard(
                    mentor = mentor,
                    isSelected = selectedMentor?.id == mentor.id,
                    onSelect = {
                        selectedMentor = if (selectedMentor?.id == mentor.id) null else mentor
                    },
                )
            }
        }

        // Bottom button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = selectedMentor != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkBlue,
                    disabledContainerColor = DarkGrey,
                ),
            ) {
                Text("Atribuir", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}

@Composable
private fun MentorSelectionCard(
    mentor: AdminMentor,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = if (isSelected) BorderStroke(2.dp, LightBlue) else null,
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(avatarColors[mentor.avatarColorIndex % avatarColors.size]),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(mentor.avatarInitials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(mentor.name, color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(mentor.department, color = DarkGrey, fontSize = 12.sp)
                    Text(mentor.institution, color = DarkGrey, fontSize = 12.sp)
                }
                // Availability badge
                val (label, color) = if (mentor.isAvailable)
                    "Disponível!" to Color(0xFF4CAF50)
                else
                    "Indisponível" to Red
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(color.copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                ) {
                    Text(label, color = color, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onSelect,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, if (isSelected) Red else LightBlue),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (isSelected) Red else LightBlue,
                ),
            ) {
                Text(
                    text = if (isSelected) "Remover seleção" else "Selecionar orientador",
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

// region Previews

@Preview(showSystemUi = true)
@Composable
private fun AssignMentorInstituicaoScreenPreview() {
    MaterialTheme {
        AssignMentorInstituicaoScreen(mentorId = "m1", navController = rememberNavController())
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun AssignMentorInstituicaoScreenLandscapePreview() {
    MaterialTheme {
        AssignMentorInstituicaoScreen(mentorId = "m1", navController = rememberNavController())
    }
}

// endregion
