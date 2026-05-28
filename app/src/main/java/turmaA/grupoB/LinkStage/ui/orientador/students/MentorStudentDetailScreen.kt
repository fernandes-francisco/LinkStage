package turmaA.grupoB.LinkStage.ui.orientador.students

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Grade
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import turmaA.grupoB.LinkStage.ui.admin.AdminStudent
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActivityLogCard
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActivityLogStatus
import turmaA.grupoB.LinkStage.ui.aluno.activity.InternshipHeader
import turmaA.grupoB.LinkStage.ui.aluno.activity.calculateInternshipProgress
import turmaA.grupoB.LinkStage.ui.common.LinkStageTabRow
import turmaA.grupoB.LinkStage.ui.common.SectionLabel
import turmaA.grupoB.LinkStage.ui.orientador.InternshipEvaluation
import turmaA.grupoB.LinkStage.ui.orientador.OrientadorRoutes
import turmaA.grupoB.LinkStage.ui.orientador.formatCheckpointDate
import turmaA.grupoB.LinkStage.ui.orientador.sampleEvaluation
import turmaA.grupoB.LinkStage.ui.orientador.sampleMentorStudents
import turmaA.grupoB.LinkStage.ui.orientador.sampleStudentInternship
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.Fade1
import turmaA.grupoB.LinkStage.ui.theme.Fade2
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.MediumBlue
import turmaA.grupoB.LinkStage.ui.theme.Red

@Composable
fun MentorStudentDetailScreen(
    studentId: String,
    navController: NavController,
) {
    val student = sampleMentorStudents.find { it.id == studentId } ?: return
    val evaluation = sampleEvaluation
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
    ) {
        // Header
        Column(modifier = Modifier.background(Color.White)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = DarkBlue)
                }
                Text(
                    text = "Lista de Alunos",
                    color = DarkBlue,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Fade1),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(student.avatarInitials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(student.name, color = DarkBlue, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(student.institutionCode, color = LightBlue, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LinkStageTabRow(
                tabs = listOf("Detalhes", "Trabalho\nDesenvolvido", "Avaliar"),
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it },
            )
        }

        AnimatedContent(
            targetState = selectedTab,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "tab_content",
        ) { tab ->
            when (tab) {
                0 -> StudentDetailsTab(student, navController)
                1 -> StudentWorkTab(navController)
                2 -> StudentEvaluateTab(student, evaluation)
            }
        }
    }
}

// region Tab 0 — Detalhes

@Composable
private fun StudentDetailsTab(
    student: AdminStudent,
    navController: NavController,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Fade2)
                .clickable { },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Enviar mensagem",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        InfoField(label = "Instituição", value = student.institution, trailingBadge = "ipvc")
        InfoFieldWithIcon(label = "Curso", value = student.course, icon = Icons.AutoMirrored.Outlined.MenuBook)
        InfoFieldWithIcon(label = "Média atual (0-20)", value = "${student.gpa}", icon = Icons.Outlined.Grade)
        InfoFieldWithIcon(label = "Email", value = student.email, icon = Icons.Outlined.Email)
        InfoFieldWithIcon(label = "Telemóvel", value = student.phone, icon = Icons.Outlined.Phone)

        Spacer(modifier = Modifier.height(12.dp))

        if (student.skills.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionLabel("Competências do Aluno")
                    Spacer(modifier = Modifier.height(8.dp))
                    student.skills.forEach { skill ->
                        Text("• $skill", color = DarkGrey, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }
        }

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

// region Tab 1 — Trabalho Desenvolvido

@Composable
private fun StudentWorkTab(navController: NavController) {
    val internship = sampleStudentInternship
    val progress = calculateInternshipProgress(internship.startDate, internship.endDate)

    var animationStarted by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationStarted) progress else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "progress",
    )
    LaunchedEffect(Unit) { animationStarted = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        InternshipHeader(
            internship = internship,
            animatedProgress = animatedProgress,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Atividade Recente",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
            ),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )

        internship.activityLogs.forEach { activityLog ->
            ActivityLogCard(
                activityLog = activityLog,
                onClick = {
                    navController.navigate(OrientadorRoutes.mentorCheckpointDetail(activityLog.id))
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// endregion

// region Tab 2 — Avaliar

@Composable
private fun StudentEvaluateTab(
    student: AdminStudent,
    evaluation: InternshipEvaluation,
) {
    val internship = sampleStudentInternship
    val progress = calculateInternshipProgress(internship.startDate, internship.endDate)

    var animationStarted by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationStarted) progress else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "progress_eval",
    )
    LaunchedEffect(Unit) { animationStarted = true }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        InternshipHeader(
            internship = internship,
            animatedProgress = animatedProgress,
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!evaluation.isCompleted) {
            NotCompletedCard()
        } else {
            CompletedEvaluationContent(student, evaluation)
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun NotCompletedCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = BackgroundLight),
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                Icons.Outlined.Info,
                contentDescription = null,
                tint = DarkGrey,
                modifier = Modifier.size(32.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Estágio não concluído",
                color = DarkBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "O aluno ainda não concluiu o estágio. A avaliação ficará disponível quando o estágio terminar.",
                color = DarkGrey,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
            )
        }
    }
}

@Composable
private fun CompletedEvaluationContent(
    student: AdminStudent,
    evaluation: InternshipEvaluation,
) {
    val internship = sampleStudentInternship
    var observation by remember { mutableStateOf(evaluation.schoolMentorObservation) }
    var gradeText by remember { mutableStateOf(evaluation.schoolMentorGrade) }
    var gradeError by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text("Confirmar Avaliação", fontWeight = FontWeight.Bold, color = DarkBlue)
            },
            text = {
                Text(
                    "Tens a certeza que queres submeter a avaliação de ${student.name}?",
                    color = DarkGrey,
                )
            },
            confirmButton = {
                Button(
                    onClick = { showConfirmDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                ) {
                    Text("Submeter", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar", color = DarkBlue)
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp),
        )
    }

    // Submitted files
    Text(
        text = "Anexos entregues",
        color = DarkBlue,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(8.dp))

    internship.activityLogs.filter { it.hasSubmitted }.forEach { activityLog ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Column {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = LightBlue,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = activityLog.title,
                        color = DarkBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = formatCheckpointDate(activityLog.date),
                        color = DarkGrey,
                        fontSize = 12.sp,
                    )
                }
                activityLog.submittedFiles.forEach { file ->
                    ExpandableFileRow(file = file)
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // Evaluation form
    Text(
        text = "Avaliação",
        color = DarkBlue,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        modifier = Modifier.padding(horizontal = 16.dp),
    )
    Spacer(modifier = Modifier.height(12.dp))

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionLabel("Breve observação")
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Faça uma breve observação sobre o desempenho do aluno.",
            color = DarkGrey,
            fontSize = 12.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = observation,
            onValueChange = { observation = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Escreva aqui.", color = DarkGrey) },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LightBlue,
                unfocusedBorderColor = BorderGrey,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
            ),
            minLines = 4,
            maxLines = 8,
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        SectionLabel("Avaliação do Aluno")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = gradeText,
            onValueChange = {
                gradeText = it
                gradeError = false
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Insira a nota (0-20)", color = DarkGrey) },
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (gradeError) Red else LightBlue,
                unfocusedBorderColor = if (gradeError) Red else BorderGrey,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            isError = gradeError,
        )
        if (gradeError) {
            Spacer(modifier = Modifier.height(4.dp))
            Text("Introduz um valor entre 0 e 20", color = Red, fontSize = 12.sp)
        }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Fade2)
            .clickable {
                val grade = gradeText.toFloatOrNull()
                if (grade == null || grade < 0f || grade > 20f) {
                    gradeError = true
                } else {
                    showConfirmDialog = true
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Continuar",
            color = Color.White,
            fontWeight = FontWeight.SemiBold,
        )
    }

    Spacer(modifier = Modifier.height(8.dp))

    TextButton(
        onClick = { },
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text("Voltar", color = DarkGrey)
    }
}

// endregion

// region Shared — ExpandableFileRow

@Composable
fun ExpandableFileRow(file: turmaA.grupoB.LinkStage.ui.aluno.activity.CheckpointFile) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                Icons.Outlined.Description,
                contentDescription = null,
                tint = DarkBlue,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = file.name,
                color = DarkBlue,
                fontSize = 13.sp,
                modifier = Modifier.weight(1f),
            )
            Icon(
                if (expanded) Icons.Outlined.KeyboardArrowUp else Icons.Outlined.KeyboardArrowDown,
                contentDescription = null,
                tint = DarkGrey,
                modifier = Modifier.size(18.dp),
            )
        }
        AnimatedVisibility(visible = expanded) {
            Button(
                onClick = { },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .height(36.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
            ) {
                Text("Descarregar", color = Color.White, fontSize = 13.sp)
            }
        }
        HorizontalDivider(color = BorderGrey)
    }
}

// endregion

@Preview(showSystemUi = true)
@Composable
private fun MentorStudentDetailScreenPreview() {
    MaterialTheme {
        MentorStudentDetailScreen(studentId = "s1", navController = rememberNavController())
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun MentorStudentDetailScreenLandscapePreview() {
    MaterialTheme {
        MentorStudentDetailScreen(studentId = "s1", navController = rememberNavController())
    }
}
