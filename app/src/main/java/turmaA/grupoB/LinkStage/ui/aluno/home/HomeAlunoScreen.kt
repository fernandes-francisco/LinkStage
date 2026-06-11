package turmaA.grupoB.LinkStage.ui.aluno.home

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import turmaA.grupoB.LinkStage.R
import turmaA.grupoB.LinkStage.ui.aluno.AlunoRoutes
import turmaA.grupoB.LinkStage.ui.aluno.activity.ApplicationCard
import turmaA.grupoB.LinkStage.ui.aluno.activity.InternshipHeader
import turmaA.grupoB.LinkStage.ui.aluno.activity.calculateInternshipProgress
import turmaA.grupoB.LinkStage.ui.aluno.chat.ConversationItem
import turmaA.grupoB.LinkStage.ui.common.CommonTopBar
import turmaA.grupoB.LinkStage.ui.common.EvaluationNotificationModal
import turmaA.grupoB.LinkStage.ui.common.EvaluationPendingCard
import turmaA.grupoB.LinkStage.ui.common.LinkStageLogo
import turmaA.grupoB.LinkStage.ui.orientador.EvaluationState
import turmaA.grupoB.LinkStage.ui.orientador.InternshipEvaluation
import turmaA.grupoB.LinkStage.ui.orientador.InternshipType
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.Fade3
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.viewmodel.HomeViewModel

// region Data models

enum class ApplicationStatus(@StringRes val labelRes: Int) {
    ACCEPTED(R.string.applications_filter_accepted),
    REJECTED(R.string.applications_filter_rejected),
    PENDING(R.string.applications_filter_pending),
}

data class Entrega(
    val deadline: String,
    val title: String,
    val company: String,
)

// endregion

// region Mock data

private val mockEntregas = listOf(
    Entrega("Amanhã", "Apresentação de Cyber Segurança", "IPVC.Inc"),
    Entrega("Em 2 dias", "Ponto de controlo 25 projeto 4", "IPVC.Inc"),
)

// endregion

@Composable
fun HomeAlunoScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(),
) {
    val userName = "Tomás"
    val hasActiveInternship by homeViewModel.hasActiveInternship.collectAsState()
    val activeInternship by homeViewModel.activeInternship.collectAsState()
    val recentApplications by homeViewModel.recentApplications.collectAsState()
    val recentConversations by homeViewModel.recentConversations.collectAsState()
    val hasSeenResult by homeViewModel.hasSeenEvaluationResult.collectAsState()
    val hasDismissedModal by homeViewModel.hasDismissedEvaluationModal.collectAsState()

    // Evaluation sample data for demo
    val evaluation: InternshipEvaluation? = remember {
        InternshipEvaluation(
            internshipId = "int1",
            internshipType = InternshipType.COMPANY_SCHOOL,
            state = EvaluationState.COMPLETED,
            companyResponsibleGrade = 16.5f,
            companyResponsibleObservation = "Excelente desempenho técnico.",
            companyResponsibleName = "Ana Costa",
            companyMentorGrade = 15.0f,
            companyMentorObservation = "Bom trabalho em equipa.",
            companyMentorName = "Prof. Tiago Alexandre",
            schoolMentorGrade = 16f,
            schoolMentorObservation = "Bom desempenho global.",
            schoolMentorName = "Prof. Carvalho",
            hasSeenNotification = false,
        )
    }

    val showEvaluationModal = evaluation?.state == EvaluationState.COMPLETED &&
            !hasSeenResult &&
            !hasDismissedModal

    if (showEvaluationModal) {
        EvaluationNotificationModal(
            title = stringResource(R.string.home_eval_result_title),
            message = stringResource(R.string.home_eval_result_message),
            actionLabel = stringResource(R.string.home_result_action),
            onAction = {
                homeViewModel.setHasSeenEvaluationResult(true)
                navController.navigate(AlunoRoutes.internshipResultRoute(evaluation.internshipId))
            },
            onDismiss = { homeViewModel.setHasDismissedEvaluationModal(true) },
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
    ) {
        CommonTopBar()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Text(
                stringResource(R.string.home_greeting, userName),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                ),
            )
            Text(
                stringResource(R.string.home_welcome),
                style = MaterialTheme.typography.bodyMedium,
                color = DarkGrey
            )
        }

        if (evaluation?.state == EvaluationState.COMPLETED && !hasSeenResult) {
            EvaluationPendingCard(
                title = stringResource(R.string.home_result_available),
                message = stringResource(R.string.home_result_message),
                actionLabel = stringResource(R.string.home_result_action),
                isDanger = false,
                onClick = {
                    homeViewModel.setHasSeenEvaluationResult(true)
                    navController.navigate(AlunoRoutes.internshipResultRoute(evaluation.internshipId))
                },
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // DEBUG: toggle para testar os dois estados
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .border(1.dp, BorderGrey.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    stringResource(R.string.home_debug_mode),
                    style = MaterialTheme.typography.labelMedium,
                    color = DarkGrey,
                    modifier = Modifier.weight(1f),
                )
                Switch(
                    checked = hasActiveInternship,
                    onCheckedChange = { homeViewModel.toggleInternship() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = LightBlue,
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = DarkGrey.copy(alpha = 0.3f),
                    ),
                )
            }

            if (hasActiveInternship && activeInternship != null) {
                // State B: Active internship
                val internship = activeInternship!!
                val progress = calculateInternshipProgress(internship.startDate, internship.endDate)

                var animationStarted by remember { mutableStateOf(false) }
                val animatedProgress by animateFloatAsState(
                    targetValue = if (animationStarted) progress else 0f,
                    animationSpec = tween(durationMillis = 1000),
                    label = "home_progress",
                )
                LaunchedEffect(Unit) { animationStarted = true }

                HomeSectionCard(
                    title = stringResource(R.string.home_active_internship),
                    actionText = stringResource(R.string.home_view_details),
                    onAction = { navController.navigate(AlunoRoutes.ACTIVITY) }
                ) {
                    InternshipHeader(
                        internship = internship,
                        animatedProgress = animatedProgress,
                    )
                }

                EntregasCard(mockEntregas)
            } else {
                // State A: No internship — show recent applications
                HomeSectionCard(
                    title = stringResource(R.string.home_applications_title),
                    actionText = stringResource(R.string.common_view_all),
                    onAction = { navController.navigate(AlunoRoutes.ACTIVITY) }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        recentApplications.take(2).forEach { application ->
                            ApplicationCard(application = application)
                        }
                    }
                }

                HomeSectionCard(
                    title = stringResource(R.string.home_discover_title),
                    actionText = stringResource(R.string.home_discover_action),
                    onAction = { navController.navigate(AlunoRoutes.DISCOVER) }
                ) {
                    Text(
                        stringResource(R.string.home_discover_message),
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkGrey,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            // Recent messages — both states
            HomeSectionCard(
                title = stringResource(R.string.home_recent_messages),
                actionText = stringResource(R.string.common_view_all),
                onAction = { navController.navigate(AlunoRoutes.MESSAGES) }
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    recentConversations.take(3).forEach { conversation ->
                        ConversationItem(
                            conversation = conversation,
                            onClick = {
                                navController.navigate(AlunoRoutes.chatRoute(conversation.id))
                            },
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun HomeSectionCard(
    title: String,
    actionText: String,
    onAction: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        border = BorderStroke(1.dp, BorderGrey.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            SectionHeader(
                title = title,
                actionText = actionText,
                onAction = onAction
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

// region Components

@Composable
private fun EntregasCard(entregas: List<Entrega>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Fade3, RoundedCornerShape(16.dp))
            .padding(20.dp),
    ) {
        Column {
            Text(
                stringResource(R.string.home_deliveries),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            entregas.forEachIndexed { index, entrega ->
                Text(
                    entrega.deadline,
                    style = MaterialTheme.typography.labelMedium.copy(color = LightBlue),
                )
                Text(
                    entrega.title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                    ),
                )
                Text(
                    entrega.company,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.White.copy(alpha = 0.7f),
                    ),
                )
                if (index < entregas.lastIndex) {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionText: String,
    onAction: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
            ),
        )
        Row(
            modifier = Modifier.clickable { onAction() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                actionText,
                color = LightBlue,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = LightBlue,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

// endregion
