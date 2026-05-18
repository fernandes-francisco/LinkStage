package turmaA.grupoB.LinkStage.ui.aluno.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BlueDark
import turmaA.grupoB.LinkStage.ui.theme.BlueLight
import turmaA.grupoB.LinkStage.ui.theme.Fade3
import turmaA.grupoB.LinkStage.ui.theme.RedAccent

// region Data models — replace with backend models later
data class Candidatura(
    val title: String,
    val company: String,
    val timeAgo: String,
    val status: CandidaturaStatus,
)

enum class CandidaturaStatus(val label: String) {
    ACEITE("Aceite"),
    RECUSADO("Recusado"),
    PENDENTE("Pendente"),
}

data class Entrega(
    val deadline: String,
    val title: String,
    val company: String,
)

data class MensagemPreview(
    val initials: String,
    val name: String,
    val preview: String,
    val avatarColor: Color,
)
// endregion

// region Mock data — replace with ViewModel state
private val mockCandidaturas = listOf(
    Candidatura("Call Center Indiano", "Rasheed", "3d atrás", CandidaturaStatus.ACEITE),
    Candidatura("Intermaché dos Arcos", "Intermarché", "2w atrás", CandidaturaStatus.RECUSADO),
)

private val mockEntregas = listOf(
    Entrega("Amanhã", "Apresentação de Cyber Segurança", "IPVC.Inc"),
    Entrega("Em 2 dias", "Ponto de controlo 25 projeto 4", "IPVC.Inc"),
)

private val mockMensagens = listOf(
    MensagemPreview("FF", "Francisco Fernandes", "Boa pergunta.", Color(0xFF30C9CD)),
    MensagemPreview("TA", "Tiago Alexandre", "Como assim?", Color(0xFF0E1572)),
    MensagemPreview("MA", "Miguel Azevedo", "Note-se.", Color(0xFF326B9B)),
    MensagemPreview("VS", "Viana S.T.Arts", "Altera a dashboard.", Color(0xFF30C9CD)),
)
// endregion

@Composable
fun HomeAlunoScreen(modifier: Modifier = Modifier) {
    val userName = "Tomás"
    val isEmEstagio = false

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        LinkStageLogo()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Olá, $userName",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = BlueDark,
            ),
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isEmEstagio) {
            EstagioAtivoSection()
        } else {
            CandidaturasSection(mockCandidaturas)
        }

        Spacer(modifier = Modifier.height(16.dp))

        EntregasCard(mockEntregas)

        Spacer(modifier = Modifier.height(24.dp))

        MensagensSection(mockMensagens)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun LinkStageLogo() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            "LINK",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = BlueDark,
            ),
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            "STAGE",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Light,
                color = BlueDark,
            ),
        )
    }
}

// region Candidaturas

@Composable
private fun CandidaturasSection(candidaturas: List<Candidatura>) {
    SectionHeader("Estado das candidaturas", "Ver todas")
    Spacer(modifier = Modifier.height(12.dp))
    candidaturas.forEach { candidatura ->
        CandidaturaCard(candidatura)
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun CandidaturaCard(candidatura: Candidatura) {
    val statusColor = when (candidatura.status) {
        CandidaturaStatus.ACEITE -> BlueLight
        CandidaturaStatus.RECUSADO -> RedAccent
        CandidaturaStatus.PENDENTE -> Color.Gray
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(BlueLight.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Outlined.Work,
                        contentDescription = null,
                        tint = BlueLight,
                        modifier = Modifier.size(18.dp),
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    candidatura.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .background(statusColor, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            ) {
                Text(
                    candidatura.status.label,
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                candidatura.company,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )
            Text(
                "• Candidatou-se ${candidatura.timeAgo}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
            )
        }
    }
}

// endregion

// region Estágio ativo (placeholder for when student is in an internship)

@Composable
private fun EstagioAtivoSection() {
    SectionHeader("Estado do estágio", "Ver detalhes")
    Spacer(modifier = Modifier.height(12.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Estágio em curso",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Nenhum estágio ativo de momento.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )
        }
    }
}

// endregion

// region Entregas

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
                    style = MaterialTheme.typography.labelMedium.copy(color = BlueLight),
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

// endregion

// region Mensagens

@Composable
private fun MensagensSection(mensagens: List<MensagemPreview>) {
    SectionHeader("Mensagens Recentes", "Ver todas")
    Spacer(modifier = Modifier.height(12.dp))
    mensagens.forEach { mensagem ->
        MensagemItem(mensagem)
    }
}

@Composable
private fun MensagemItem(mensagem: MensagemPreview) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(mensagem.avatarColor, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                mensagem.initials,
                color = Color.White,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                "${mensagem.initials} | ${mensagem.name}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
            )
            Text(
                mensagem.preview,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        Icon(
            Icons.AutoMirrored.Outlined.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.Gray,
        )
    }
}

// endregion

// region Shared

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
                color = BlueDark,
            ),
        )
        TextButton(onClick = onAction) {
            Text(
                actionText,
                color = BlueLight,
                style = MaterialTheme.typography.labelMedium,
            )
            Spacer(modifier = Modifier.width(2.dp))
            Icon(
                Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = BlueLight,
                modifier = Modifier.size(16.dp),
            )
        }
    }
}

// endregion