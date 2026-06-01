package turmaA.grupoB.LinkStage.ui.instituicao.offers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import turmaA.grupoB.LinkStage.ui.aluno.chat.avatarColors
import turmaA.grupoB.LinkStage.ui.aluno.home.ApplicationStatus
import turmaA.grupoB.LinkStage.ui.common.ConfirmationDialog
import turmaA.grupoB.LinkStage.ui.common.LinkStageTabRow
import turmaA.grupoB.LinkStage.ui.common.SectionLabel
import turmaA.grupoB.LinkStage.ui.instituicao.InstitutionApplication
import turmaA.grupoB.LinkStage.ui.instituicao.sampleInstitutionApplications
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.Fade1
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.MediumBlue
import turmaA.grupoB.LinkStage.ui.theme.Red

@Composable
fun ApplicationDetailInstituicaoScreen(
    applicationId: String,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val application = sampleInstitutionApplications.find { it.id == applicationId }
        ?: sampleInstitutionApplications.first()

    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight),
    ) {
        // Fixed header
        Column(modifier = Modifier.background(Color.White)) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = DarkBlue)
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text("Candidaturas", color = DarkBlue, fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
            }

            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Fade1),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(application.studentAvatarInitials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(application.studentName, color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(application.institution, color = LightBlue, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        }

        LinkStageTabRow(
            tabs = listOf("Detalhes", "Competências", "Gerir"),
            selectedIndex = selectedTab,
            onTabSelected = { selectedTab = it },
        )

        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> ApplicationDetailsTab(application)
                1 -> ApplicationSkillsTab(application)
                2 -> ApplicationManageTab(application)
            }
        }
    }
}

// region Tab 0 — Details

@Composable
private fun ApplicationDetailsTab(application: InstitutionApplication) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
    ) {
        InfoField(
            label = "Instituição",
            value = application.institution,
            trailingBadge = "ipvc",
        )
        InfoFieldWithIcon(
            label = "Curso",
            value = application.course,
            icon = Icons.AutoMirrored.Outlined.MenuBook,
        )
        InfoFieldWithIcon(
            label = "Média atual (0-20)",
            value = application.gpa,
            icon = Icons.Outlined.Grade,
        )
        InfoFieldWithIcon(
            label = "Email",
            value = application.email,
            icon = Icons.Outlined.Email,
        )
        InfoFieldWithIcon(
            label = "Telemóvel",
            value = application.phone,
            icon = Icons.Outlined.Phone,
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun InfoField(label: String, value: String, trailingBadge: String? = null) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            SectionLabel(label)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(value, color = DarkBlue, fontSize = 14.sp, modifier = Modifier.weight(1f))
                if (trailingBadge != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(MediumBlue)
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text(trailingBadge, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoFieldWithIcon(label: String, value: String, icon: ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            SectionLabel(label)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = DarkGrey, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(value, color = DarkBlue, fontSize = 14.sp)
            }
        }
    }
}

// endregion

// region Tab 1 — Skills

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ApplicationSkillsTab(application: InstitutionApplication) {
    var showMotivationDialog by remember { mutableStateOf(false) }

    if (showMotivationDialog) {
        MotivationLetterDialog(
            title = application.motivationLetterTitle,
            body = application.motivationLetterBody,
            onDismiss = { showMotivationDialog = false },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
    ) {
        // Personal statement
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SectionLabel("Declaração Pessoal")
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Breve apresentação do candidato e a sua motivação para o cargo.",
                color = DarkGrey, fontSize = 12.sp,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Text(
                text = application.personalStatement,
                color = DarkGrey, fontSize = 14.sp, lineHeight = 22.sp,
                modifier = Modifier.padding(14.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Technical skills
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SectionLabel("Competências técnicas")
        }
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            application.skills.forEach { skill ->
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(LightBlue.copy(alpha = 0.2f))
                        .border(1.dp, LightBlue, RoundedCornerShape(20.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                ) {
                    Text(skill, color = DarkBlue, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Motivation letter
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            SectionLabel("Carta de motivação")
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (application.hasMotivationLetter) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clickable { showMotivationDialog = true },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Description, contentDescription = null, tint = LightBlue, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Carta de motivação",
                        color = DarkBlue, fontWeight = FontWeight.SemiBold, fontSize = 14.sp,
                        modifier = Modifier.weight(1f),
                    )
                    Icon(Icons.Outlined.ChevronRight, contentDescription = null, tint = DarkGrey, modifier = Modifier.size(18.dp))
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Text(
                    text = "Sem carta de motivacao anexada.",
                    color = DarkGrey, fontSize = 14.sp,
                    modifier = Modifier.padding(14.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MotivationLetterDialog(
    title: String,
    body: String,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkBlue)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Carta de motivação",
                        color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Fechar", tint = Color.White)
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                ) {
                    Text(title, color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(body, color = DarkGrey, fontSize = 14.sp, lineHeight = 22.sp)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(
                            brush = androidx.compose.ui.graphics.SolidColor(DarkBlue),
                        ),
                    ) {
                        Text("Voltar", color = DarkBlue, fontWeight = FontWeight.SemiBold)
                    }
                    Button(
                        onClick = { },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                    ) {
                        Text("Descarregar", fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

// endregion

// region Tab 2 — Manage

@Composable
private fun ApplicationManageTab(application: InstitutionApplication) {
    var selectedStatus by remember { mutableStateOf(application.status) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val statusLabel = when (selectedStatus) {
        ApplicationStatus.ACCEPTED -> "aceite"
        ApplicationStatus.REJECTED -> "rejeitada"
        ApplicationStatus.PENDING -> "pendente"
    }

    if (showConfirmDialog) {
        ConfirmationDialog(
            title = "Atualizar candidatura?",
            body = "Tem a certeza que pretende marcar esta candidatura como $statusLabel?",
            confirmLabel = "Confirmar",
            onConfirm = { showConfirmDialog = false },
            onDismiss = { showConfirmDialog = false },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 8.dp),
    ) {
        // Current status card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LightBlue.copy(alpha = 0.08f)),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(LightBlue),
            ),
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Info, contentDescription = null, tint = LightBlue, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Estado atual:", color = DarkBlue, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    StatusBadgeDynamic(selectedStatus)
                }
                Text(
                    text = "Selecione uma decisão para esta candidatura.",
                    color = DarkGrey, fontSize = 12.sp,
                    modifier = Modifier.padding(start = 30.dp, top = 4.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Accept card
        val acceptSelected = selectedStatus == ApplicationStatus.ACCEPTED
        val acceptColor = Color(0xFF4CAF50)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { selectedStatus = ApplicationStatus.ACCEPTED },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (acceptSelected) acceptColor.copy(alpha = 0.08f) else Color.White,
            ),
            border = CardDefaults.outlinedCardBorder().copy(
                width = 2.dp,
                brush = androidx.compose.ui.graphics.SolidColor(
                    if (acceptSelected) acceptColor else acceptColor.copy(alpha = 0.3f),
                ),
            ),
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Default.CheckCircle, contentDescription = null,
                    tint = if (acceptSelected) acceptColor else acceptColor.copy(alpha = 0.5f),
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Aceite", color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Marcar esta candidatura a oferta como aceite.", color = DarkGrey, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Reject card
        val rejectSelected = selectedStatus == ApplicationStatus.REJECTED
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clickable { selectedStatus = ApplicationStatus.REJECTED },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (rejectSelected) Red.copy(alpha = 0.08f) else Color.White,
            ),
            border = CardDefaults.outlinedCardBorder().copy(
                width = 2.dp,
                brush = androidx.compose.ui.graphics.SolidColor(
                    if (rejectSelected) Red else Red.copy(alpha = 0.3f),
                ),
            ),
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Default.Cancel, contentDescription = null,
                    tint = if (rejectSelected) Red else Red.copy(alpha = 0.5f),
                    modifier = Modifier.size(24.dp),
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Rejeitado", color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Marcar esta candidatura a oferta como rejeitada.", color = DarkGrey, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Info notice
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = LightBlue.copy(alpha = 0.08f)),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.SolidColor(LightBlue.copy(alpha = 0.4f)),
            ),
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Icon(Icons.Outlined.Info, contentDescription = null, tint = LightBlue, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "A decisão apenas será registada no momento de fecho da candidatura.",
                    color = DarkGrey, fontSize = 12.sp, lineHeight = 18.sp,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Update button
        AnimatedVisibility(
            visible = selectedStatus != application.status,
            enter = fadeIn() + slideInVertically { it },
        ) {
            Button(
                onClick = { showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
            ) {
                Text("Atualizar estado", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun StatusBadgeDynamic(status: ApplicationStatus) {
    val (label, color) = when (status) {
        ApplicationStatus.ACCEPTED -> "Aceite" to Color(0xFF4CAF50)
        ApplicationStatus.REJECTED -> "Recusado" to Red
        ApplicationStatus.PENDING -> "Pendente" to Color(0xFFF5C518)
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(label, color = color, fontWeight = FontWeight.Bold, fontSize = 10.sp)
    }
}

// endregion

// region Previews

@Preview(showSystemUi = true)
@Composable
private fun ApplicationDetailInstituicaoScreenPreview() {
    MaterialTheme {
        ApplicationDetailInstituicaoScreen(applicationId = "a1", navController = rememberNavController())
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun ApplicationDetailInstituicaoScreenLandscapePreview() {
    MaterialTheme {
        ApplicationDetailInstituicaoScreen(applicationId = "a1", navController = rememberNavController())
    }
}

// endregion
