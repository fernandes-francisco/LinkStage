package turmaA.grupoB.LinkStage.ui.aluno.activity

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import turmaA.grupoB.LinkStage.ui.common.LinkStageLogo
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.Red
import turmaA.grupoB.LinkStage.ui.aluno.home.ApplicationStatus
import turmaA.grupoB.LinkStage.viewmodel.HomeViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

// region Data models

data class ApplicationItem(
    val id: String,
    val offerTitle: String,
    val company: String,
    val appliedAgo: String,
    val status: ApplicationStatus,
)

enum class ActivityLogStatus { COMPLETED, PENDING }

data class ActivityLog(
    val id: String,
    val title: String,
    val description: String,
    val date: LocalDate,
    val status: ActivityLogStatus,
)

data class ActiveInternship(
    val title: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val activityLogs: List<ActivityLog>,
)

// endregion

// region Utils

fun calculateInternshipProgress(startDate: LocalDate, endDate: LocalDate): Float {
    val today = LocalDate.now()
    return when {
        today <= startDate -> 0f
        today >= endDate -> 1f
        else -> {
            val total = ChronoUnit.DAYS.between(startDate, endDate).toFloat()
            val elapsed = ChronoUnit.DAYS.between(startDate, today).toFloat()
            (elapsed / total).coerceIn(0f, 1f)
        }
    }
}

private fun formatDate(date: LocalDate): String {
    val monthNames = listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez")
    return "${monthNames[date.monthValue - 1]} ${date.dayOfMonth}"
}

// endregion

// region Main Screen

@Composable
fun RecentActivityAlunoScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(),
    onSubmitReport: () -> Unit = {},
    onAddActivityLog: () -> Unit = {},
) {
    val hasActiveInternship by homeViewModel.hasActiveInternship.collectAsState()
    val activeInternship by homeViewModel.activeInternship.collectAsState()
    val activeApplications by homeViewModel.activeApplications.collectAsState()
    val pastApplications by homeViewModel.pastApplications.collectAsState()

    if (hasActiveInternship && activeInternship != null) {
        Scaffold(
            modifier = modifier,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onAddActivityLog,
                    containerColor = DarkBlue,
                    contentColor = Color.White,
                    shape = CircleShape,
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar registo")
                }
            },
            containerColor = BackgroundLight,
        ) { innerPadding ->
            ActiveInternshipContent(
                internship = activeInternship!!,
                onSubmitReport = onSubmitReport,
                modifier = Modifier.padding(innerPadding),
            )
        }
    } else {
        ApplicationsContent(
            activeApplications = activeApplications,
            pastApplications = pastApplications,
            modifier = modifier,
        )
    }
}

// endregion

// region State 1: No internship — applications list

@Composable
private fun ApplicationsContent(
    activeApplications: List<ApplicationItem>,
    pastApplications: List<ApplicationItem>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        item { ActivityTopBar() }

        item {
            Text(
                text = "Atividade Recente",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }

        item {
            ApplicationSearchBar()
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            ApplicationSection(title = "Candidaturas ativas") {
                activeApplications.forEach { app ->
                    ApplicationCard(application = app)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            ApplicationSection(title = "Candidaturas passadas") {
                pastApplications.forEach { app ->
                    ApplicationCard(application = app)
                }
            }
        }
    }
}

@Composable
private fun ApplicationSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = LightBlue,
                ),
                modifier = Modifier.padding(bottom = 8.dp),
            )
            content()
        }
    }
}

@Composable
fun ApplicationCard(application: ApplicationItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Icon(
                imageVector = Icons.Outlined.Work,
                contentDescription = null,
                tint = LightBlue,
                modifier = Modifier.size(20.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = application.offerTitle,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(
                    text = application.company,
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkGrey,
                )
                Text(
                    text = "• Candidatou-se ${application.appliedAgo}",
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkGrey,
                )
            }
            StatusBadge(status = application.status)
        }

        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            color = BorderGrey,
        )
    }
}

@Composable
fun StatusBadge(status: ApplicationStatus) {
    val (label, color) = when (status) {
        ApplicationStatus.PENDING -> "Pendente" to DarkGrey
        ApplicationStatus.ACCEPTED -> "Aceite" to LightBlue
        ApplicationStatus.REJECTED -> "Recusado" to Red
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.15f))
            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = color,
        )
    }
}

// endregion

// region State 2: Active internship — progress + activity logs

@Composable
private fun ActiveInternshipContent(
    internship: ActiveInternship,
    onSubmitReport: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress = calculateInternshipProgress(internship.startDate, internship.endDate)

    var animationStarted by remember { mutableStateOf(false) }
    val animatedProgress by animateFloatAsState(
        targetValue = if (animationStarted) progress else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "progress_animation",
    )
    LaunchedEffect(Unit) { animationStarted = true }

    val daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), internship.endDate)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentPadding = PaddingValues(bottom = 80.dp),
    ) {
        item { ActivityTopBar() }

        item {
            InternshipHeader(
                internship = internship,
                animatedProgress = animatedProgress,
            )
        }

        item {
            Text(
                text = "Atividade Recente",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                ),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            )
        }

        items(internship.activityLogs, key = { it.id }) { activityLog ->
            ActivityLogCard(activityLog = activityLog)
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            ReportSubmissionCard(
                daysRemaining = daysRemaining,
                onSubmit = onSubmitReport,
            )
        }
    }
}

@Composable
fun InternshipHeader(
    internship: ActiveInternship,
    animatedProgress: Float,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
    ) {
        Text(
            text = "Estágio Ativo",
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = LightBlue,
            ),
        )
        Text(
            text = internship.title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
            ),
        )

        Spacer(modifier = Modifier.height(12.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = LightBlue,
            trackColor = BorderGrey,
            strokeCap = StrokeCap.Round,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "Início: ${formatDate(internship.startDate)}",
                style = MaterialTheme.typography.labelSmall,
                color = DarkGrey,
            )
            Text(
                text = "${(animatedProgress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = LightBlue,
            )
            Text(
                text = "Fim: ${formatDate(internship.endDate)}",
                style = MaterialTheme.typography.labelSmall,
                color = DarkGrey,
            )
        }
    }
}

@Composable
fun ActivityLogCard(activityLog: ActivityLog) {
    val isCompleted = activityLog.status == ActivityLogStatus.COMPLETED

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(if (isCompleted) LightBlue else BorderGrey),
                contentAlignment = Alignment.Center,
            ) {
                if (isCompleted) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Concluído",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = "Pendente",
                        tint = DarkGrey,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = activityLog.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = activityLog.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = DarkGrey,
                    lineHeight = 18.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Outlined.CalendarMonth,
                        contentDescription = null,
                        tint = DarkGrey,
                        modifier = Modifier.size(14.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${formatDate(activityLog.date)}, ${activityLog.date.year}",
                        style = MaterialTheme.typography.labelSmall,
                        color = DarkGrey,
                    )
                }
            }
        }
    }
}

@Composable
private fun ReportSubmissionCard(
    daysRemaining: Long,
    onSubmit: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "Submissão do relatório final",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
            ),
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "O teu estágio acaba em $daysRemaining dias. Prepara e dá upload do teu relatório final para validação dos orientadores.",
            style = MaterialTheme.typography.bodyMedium,
            color = DarkGrey,
            lineHeight = 20.sp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightBlue,
                contentColor = Color.White,
            ),
        ) {
            Icon(
                imageVector = Icons.Default.Upload,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Submeter",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            )
        }
    }
}

// endregion

// region Shared components

@Composable
private fun ActivityTopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        LinkStageLogo()
    }
}

@Composable
private fun ApplicationSearchBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
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
                .background(DarkBlue),
            contentAlignment = Alignment.Center,
        ) {
            Icon(Icons.Outlined.FilterList, contentDescription = "Filtros", tint = Color.White)
        }
    }
}

// endregion