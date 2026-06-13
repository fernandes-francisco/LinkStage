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
import turmaA.grupoB.LinkStage.data.remote.model.enums.InternshipStatus
import turmaA.grupoB.LinkStage.data.remote.model.internship.InternshipModel
import turmaA.grupoB.LinkStage.data.repository.application.ApplicationRepository
import turmaA.grupoB.LinkStage.data.repository.auth.AuthRepository
import turmaA.grupoB.LinkStage.data.repository.institution.InstitutionRepository
import turmaA.grupoB.LinkStage.data.repository.internship.InternshipRepository
import turmaA.grupoB.LinkStage.data.repository.offer.OfferRepository
import turmaA.grupoB.LinkStage.data.repository.student.StudentRepository
import turmaA.grupoB.LinkStage.ui.aluno.AlunoRoutes
import turmaA.grupoB.LinkStage.ui.aluno.activity.ActiveInternship
import turmaA.grupoB.LinkStage.ui.aluno.activity.ApplicationCard
import turmaA.grupoB.LinkStage.ui.aluno.activity.ApplicationItem
import turmaA.grupoB.LinkStage.ui.aluno.activity.InternshipHeader
import turmaA.grupoB.LinkStage.ui.aluno.activity.calculateInternshipProgress
import turmaA.grupoB.LinkStage.ui.aluno.chat.ConversationItem
import turmaA.grupoB.LinkStage.ui.common.CommonTopBar
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.Fade3
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.viewmodel.HomeViewModel
import turmaA.grupoB.LinkStage.viewmodel.application.StudentApplicationDetails
import turmaA.grupoB.LinkStage.viewmodel.application.StudentApplicationsUiState
import turmaA.grupoB.LinkStage.viewmodel.application.StudentApplicationsViewModel
import turmaA.grupoB.LinkStage.viewmodel.application.StudentApplicationsViewModelFactory
import turmaA.grupoB.LinkStage.viewmodel.auth.AuthUiState
import turmaA.grupoB.LinkStage.viewmodel.auth.AuthViewModel
import turmaA.grupoB.LinkStage.viewmodel.auth.AuthViewModelFactory
import turmaA.grupoB.LinkStage.viewmodel.internships.InternshipUiState
import turmaA.grupoB.LinkStage.viewmodel.internships.InternshipViewModel
import turmaA.grupoB.LinkStage.viewmodel.internships.InternshipViewModelFactory
import turmaA.grupoB.LinkStage.viewmodel.student.StudentUiState
import turmaA.grupoB.LinkStage.viewmodel.student.StudentViewModel
import turmaA.grupoB.LinkStage.viewmodel.student.StudentViewModelFactory
import turmaA.grupoB.LinkStage.data.remote.model.enums.ApplicationStatus as RemoteApplicationStatus
import java.time.LocalDate

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

@Composable
fun getMockEntregas(): List<Entrega> {
    return listOf(
        Entrega(stringResource(R.string.time_tomorrow), stringResource(R.string.mock_delivery_cybersec), "IPVC.Inc"),
        Entrega(stringResource(R.string.time_in_days, "2"), stringResource(R.string.mock_delivery_checkpoint), "IPVC.Inc"),
    )
}

// endregion

@Composable
fun HomeAlunoScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(AuthRepository())
    ),
    studentViewModel: StudentViewModel = viewModel(
        factory = StudentViewModelFactory(StudentRepository())
    ),
    studentApplicationsViewModel: StudentApplicationsViewModel = viewModel(
        factory = StudentApplicationsViewModelFactory(
            ApplicationRepository(),
            OfferRepository(),
            InstitutionRepository(),
        )
    ),
    internshipViewModel: InternshipViewModel = viewModel(
        factory = InternshipViewModelFactory(InternshipRepository())
    ),
) {
    val recentConversations by homeViewModel.recentConversations.collectAsState()

    val authUiState by authViewModel.uiState.collectAsState()
    val studentUiState by studentViewModel.uiState.collectAsState()
    val studentApplicationsUiState by studentApplicationsViewModel.uiState.collectAsState()
    val internshipUiState by internshipViewModel.uiState.collectAsState()

    val profile = (authUiState as? AuthUiState.Success)?.profile
    val userName = profile?.name ?: "Tomás"
    
    val mockEntregas = getMockEntregas()

    LaunchedEffect(Unit) {
        authViewModel.loadCurrentUserProfile()
    }

    LaunchedEffect(authUiState) {
        val state = authUiState

        if (state is AuthUiState.Success) {
            studentViewModel.loadStudentByUserId(state.profile.id)
        }
    }

    LaunchedEffect(studentUiState) {
        val state = studentUiState

        if (state is StudentUiState.Success) {
            studentApplicationsViewModel.loadApplicationsByStudent(state.student.id)
            internshipViewModel.getInternshipByStudent(state.student.id)
        }
    }

    val recentApplications = when (val state = studentApplicationsUiState) {
        is StudentApplicationsUiState.SuccessList -> state.applications.map {
            it.toApplicationItem()
        }

        else -> emptyList()
    }

    val activeInternshipModel = (internshipUiState as? InternshipUiState.SuccessList)
        ?.internships
        ?.firstOrNull { it.status == InternshipStatus.IN_PROGRESS }
    val activeInternship = activeInternshipModel?.toActiveInternship()

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

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (activeInternship != null) {
                // State B: Active internship
                val internship = activeInternship
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
            } else if (activeInternshipModel != null) {
                HomeSectionCard(
                    title = stringResource(R.string.home_active_internship),
                    actionText = stringResource(R.string.home_view_details),
                    onAction = { navController.navigate(AlunoRoutes.ACTIVITY) }
                ) {
                    Text(
                        text = stringResource(R.string.home_internship_dates_unavailable),
                        style = MaterialTheme.typography.bodyMedium,
                        color = DarkGrey,
                    )
                }
            } else {
                // State A: No internship — show recent applications
                HomeSectionCard(
                    title = stringResource(R.string.home_applications_title),
                    actionText = stringResource(R.string.common_view_all),
                    onAction = { navController.navigate(AlunoRoutes.ACTIVITY) }
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        when (val state = studentApplicationsUiState) {
                            StudentApplicationsUiState.Idle,
                            StudentApplicationsUiState.Loading -> Text(
                                text = stringResource(R.string.applications_loading),
                                style = MaterialTheme.typography.bodyMedium,
                                color = DarkGrey,
                            )

                            StudentApplicationsUiState.Empty -> Text(
                                text = stringResource(R.string.applications_empty),
                                style = MaterialTheme.typography.bodyMedium,
                                color = DarkGrey,
                            )

                            is StudentApplicationsUiState.Error -> Text(
                                text = state.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = DarkGrey,
                            )

                            is StudentApplicationsUiState.SuccessList -> {
                                recentApplications.take(2).forEach { application ->
                                    ApplicationCard(application = application)
                                }
                            }
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

private fun StudentApplicationDetails.toApplicationItem(): ApplicationItem {
    return ApplicationItem(
        id = application.id,
        offerTitle = offerTitle,
        company = institutionName,
        appliedAgo = application.createdAt,
        status = application.status.toUiApplicationStatus(),
    )
}

private fun RemoteApplicationStatus.toUiApplicationStatus(): ApplicationStatus {
    return when (this) {
        RemoteApplicationStatus.PENDING -> ApplicationStatus.PENDING
        RemoteApplicationStatus.ACCEPTED -> ApplicationStatus.ACCEPTED
        RemoteApplicationStatus.REJECTED -> ApplicationStatus.REJECTED
    }
}

private fun InternshipModel.toActiveInternship(): ActiveInternship? {
    val parsedStartDate = startDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
        ?: return null
    val parsedEndDate = endDate?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
        ?: return null

    return ActiveInternship(
        id = id,
        title = title,
        startDate = parsedStartDate,
        endDate = parsedEndDate,
        activityLogs = emptyList(),
    )
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
