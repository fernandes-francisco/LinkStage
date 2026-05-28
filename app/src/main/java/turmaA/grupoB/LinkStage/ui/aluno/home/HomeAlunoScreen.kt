package turmaA.grupoB.LinkStage.ui.aluno.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import turmaA.grupoB.LinkStage.ui.aluno.AlunoRoutes
import turmaA.grupoB.LinkStage.ui.aluno.activity.ApplicationCard
import turmaA.grupoB.LinkStage.ui.aluno.activity.InternshipHeader
import turmaA.grupoB.LinkStage.ui.aluno.activity.calculateInternshipProgress
import turmaA.grupoB.LinkStage.ui.aluno.chat.ConversationItem
import turmaA.grupoB.LinkStage.ui.common.CommonTopBar
import turmaA.grupoB.LinkStage.ui.common.LinkStageLogo
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.Fade3
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.viewmodel.HomeViewModel

// region Data models

enum class ApplicationStatus(val label: String) {
    ACCEPTED("Aceite"),
    REJECTED("Recusado"),
    PENDING("Pendente"),
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState()),
    ) {
        CommonTopBar()

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                "Olá, $userName",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = DarkBlue,
                ),
            )

            // DEBUG: toggle para testar os dois estados
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkGrey.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    "DEBUG — Em estágio",
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

            Spacer(modifier = Modifier.height(16.dp))

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

                SectionHeader(
                    title = "Estágio Ativo",
                    actionText = "Ver detalhes",
                    onAction = {
                        navController.navigate(AlunoRoutes.ACTIVITY)
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))
                InternshipHeader(
                    internship = internship,
                    animatedProgress = animatedProgress,
                )

                Spacer(modifier = Modifier.height(16.dp))

                EntregasCard(mockEntregas)
            } else {
                // State A: No internship — show recent applications
                SectionHeader(
                    title = "Estado das candidaturas",
                    actionText = "Ver todas",
                    onAction = {
                        navController.navigate(AlunoRoutes.ACTIVITY)
                    },
                )
                Spacer(modifier = Modifier.height(8.dp))

                recentApplications.forEach { application ->
                    ApplicationCard(application = application)
                }

                Spacer(modifier = Modifier.height(8.dp))

                SectionHeader(
                    title = "Descobre Oportunidades",
                    actionText = "Explorar",
                    onAction = {
                        navController.navigate(AlunoRoutes.DISCOVER)
                    },
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Recent messages — both states
            SectionHeader(
                title = "Mensagens Recentes",
                actionText = "Ver todas",
                onAction = {
                    navController.navigate(AlunoRoutes.MESSAGES)
                },
            )
            Spacer(modifier = Modifier.height(8.dp))

            recentConversations.forEach { conversation ->
                ConversationItem(
                    conversation = conversation,
                    onClick = {
                        navController.navigate(AlunoRoutes.chatRoute(conversation.id))
                    },
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
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
                "Próximas entregas",
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
                fontWeight = FontWeight.SemiBold,
                color = DarkBlue,
            ),
        )
        TextButton(onClick = onAction) {
            Text(
                actionText,
                color = LightBlue,
                style = MaterialTheme.typography.labelMedium,
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