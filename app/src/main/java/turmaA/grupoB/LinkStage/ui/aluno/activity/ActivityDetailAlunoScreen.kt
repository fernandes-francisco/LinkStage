package turmaA.grupoB.LinkStage.ui.aluno.activity

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import turmaA.grupoB.LinkStage.ui.common.CheckItem
import turmaA.grupoB.LinkStage.ui.common.ContentSection
import turmaA.grupoB.LinkStage.ui.common.ContentSectionColored
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.MediumBlue
import java.time.LocalDate

private val sampleActivityLog = ActivityLog(
    id = "2",
    title = "Ponto de Controlo 2",
    description = "Foquei-me em desenhar as primeiras mockups.",
    date = LocalDate.of(2026, 5, 5),
    status = ActivityLogStatus.PENDING,
    company = "Viana S.T.Arts",
    companyLogoInitial = "V",
    companyLogoColor = Color(0xFF212121),
    requirements = listOf(
        "PPT com o trabalho realizado.",
        "Relatório atualizado até ao ponto atual.",
        "Documentação adicional relevante.",
    ),
    hasSubmitted = false,
)

private fun formatDateUppercase(date: LocalDate): String {
    val monthNames = listOf("JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ")
    return "${monthNames[date.monthValue - 1]},${date.dayOfMonth}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityDetailAlunoScreen(
    checkpointId: String,
    onBack: () -> Unit,
    activityLog: ActivityLog = sampleActivityLog,
) {
    var hasSubmitted by remember { mutableStateOf(activityLog.hasSubmitted) }
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var fileName by remember { mutableStateOf<String?>(null) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        fileUri = uri
        fileName = uri?.lastPathSegment
    }

    val needsAttachment = activityLog.requirements.isNotEmpty()
    val canSubmit = !hasSubmitted && (!needsAttachment || fileUri != null)

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text("Submeter Atividade?", fontWeight = FontWeight.Bold, color = DarkBlue)
            },
            text = {
                Text(
                    "Tens a certeza que queres submeter esta atividade? Esta ação não pode ser desfeita.",
                    color = DarkGrey,
                    lineHeight = 22.sp,
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        hasSubmitted = true
                        showConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                ) {
                    Text("Submeter", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showConfirmDialog = false },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DarkBlue),
                ) {
                    Text("Cancelar")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalhes da Atividade",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = DarkBlue,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = DarkBlue)
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
                .padding(paddingValues),
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
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
                            .size(48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(activityLog.companyLogoColor),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            activityLog.companyLogoInitial,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(activityLog.title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = DarkBlue)
                        Text(activityLog.company, fontSize = 14.sp, color = LightBlue)
                    }
                }

                // Deadline row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Outlined.Schedule, contentDescription = null, tint = LightBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Prazo de Entrega", fontSize = 13.sp, color = DarkGrey)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        formatDateUppercase(activityLog.date),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Descrição
                ContentSection(title = "Descrição") {
                    Text(
                        text = activityLog.description,
                        fontSize = 14.sp,
                        color = DarkGrey,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Requisitos
                if (activityLog.requirements.isNotEmpty()) {
                    ContentSectionColored(title = "Requisitos") {
                        activityLog.requirements.forEach { req ->
                            CheckItem(text = req)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Anexos
                ContentSection(title = "Anexos") {
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                        if (hasSubmitted) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, LightBlue, RoundedCornerShape(12.dp))
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(Icons.Outlined.Description, contentDescription = null, tint = LightBlue)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Documento submetido", fontSize = 13.sp, color = DarkBlue, modifier = Modifier.weight(1f))
                                Text("Submetido", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = LightBlue)
                            }
                        } else if (fileUri != null) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, LightBlue, RoundedCornerShape(12.dp))
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Icon(Icons.Outlined.Description, contentDescription = null, tint = LightBlue)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        fileName ?: "ficheiro",
                                        fontSize = 13.sp,
                                        color = DarkBlue,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                                IconButton(onClick = { fileUri = null; fileName = null }) {
                                    Icon(Icons.Default.Close, contentDescription = "Remover", tint = DarkGrey)
                                }
                            }
                        } else {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { launcher.launch("*/*") },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                border = BorderStroke(1.dp, BorderGrey),
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp),
                                ) {
                                    Icon(
                                        Icons.Outlined.Description,
                                        contentDescription = null,
                                        tint = DarkGrey,
                                        modifier = Modifier.size(40.dp),
                                    )
                                    Text("Selecionar Ficheiros", fontWeight = FontWeight.Bold, color = DarkBlue, fontSize = 14.sp)
                                    Text("PDF, DOCX até 10MB", color = DarkGrey, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Submit button
            Button(
                onClick = { if (canSubmit) showConfirmDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasSubmitted) MediumBlue else DarkBlue,
                    disabledContainerColor = MediumBlue,
                    disabledContentColor = Color.White,
                ),
                enabled = canSubmit,
            ) {
                Text(
                    text = if (hasSubmitted) "Atividade Submetida ✓" else "Submeter",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ActivityDetailScreenPreview() {
    MaterialTheme {
        ActivityDetailAlunoScreen(checkpointId = "2", onBack = {})
    }
}
