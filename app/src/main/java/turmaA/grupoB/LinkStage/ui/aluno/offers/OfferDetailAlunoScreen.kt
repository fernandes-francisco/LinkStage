package turmaA.grupoB.LinkStage.ui.aluno.offers

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material.icons.outlined.Work
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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import turmaA.grupoB.LinkStage.ui.common.CheckItem
import turmaA.grupoB.LinkStage.ui.common.ContentSection
import turmaA.grupoB.LinkStage.ui.common.ContentSectionColored
import turmaA.grupoB.LinkStage.ui.common.LinkStageButton
import turmaA.grupoB.LinkStage.ui.common.SecondaryTopBar
import turmaA.grupoB.LinkStage.ui.common.LinkStageDialog
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.MediumBlue
import turmaA.grupoB.LinkStage.ui.theme.Red

// region Data model

data class OfferDetail(
    val id: String,
    val title: String,
    val company: String,
    val logoInitial: String,
    val logoColor: Color,
    val location: String,
    val duration: String,
    val type: String,
    val aboutCompany: String,
    val responsibilities: List<String>,
    val requirements: List<String>,
    val benefits: List<String>,
    val deadlineDays: Int,
    val applicantsCount: Int,
    val isFavourite: Boolean = false,
    val hasApplied: Boolean = false,
)

private val sampleOfferDetail = OfferDetail(
    id = "2",
    title = "UI/UX Designer",
    company = "Viana S.T.Arts",
    logoInitial = "V",
    logoColor = Color(0xFF212121),
    location = "Viana do Castelo, PT",
    duration = "6 Meses",
    type = "Remoto",
    aboutCompany = "Com o principal objetivo de realizar a reabilitação do antigo Matadouro Municipal de Viana do Castelo, visa transformar o edifício histórico num centro de ciência, arte e inovação.",
    responsibilities = listOf(
        "Realizar a prototipagem da app web.",
        "Colaborar com a equipa, com o objetivo cruzar habilidades.",
        "Desenvolver o nosso sistema de criação de dashboards.",
    ),
    requirements = listOf(
        "Experiência com Figma e prototipagem interativa.",
        "Portfólio do UI para demonstração.",
        "Comunicação excelente escrita e verbal em Inglês.",
    ),
    benefits = listOf(
        "Passe de Transporte Público",
        "Programa de Mentoria",
        "Mercado Competitivo",
    ),
    deadlineDays = 4,
    applicantsCount = 12,
)

// endregion

// region Main Screen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OfferDetailAlunoScreen(
    offerId: String,
    onBack: () -> Unit,
    onApply: (String) -> Unit = {},
    offer: OfferDetail = sampleOfferDetail,
) {
    var isFavourite by remember { mutableStateOf(offer.isFavourite) }
    var hasApplied by remember { mutableStateOf(offer.hasApplied) }
    var showApplyDialog by remember { mutableStateOf(false) }

    if (showApplyDialog) {
        ApplyConfirmDialog(
            offerTitle = offer.title,
            onConfirm = {
                showApplyDialog = false
                onApply(offerId)
            },
            onDismiss = { showApplyDialog = false },
        )
    }

    Scaffold(
        topBar = { SecondaryTopBar(title = "Detalhes da Oferta", onBack = onBack) },
        bottomBar = {
            OfferDetailBottomBar(
                hasApplied = hasApplied,
                isFavourite = isFavourite,
                deadlineDays = offer.deadlineDays,
                applicantsCount = offer.applicantsCount,
                onApply = { showApplyDialog = true },
                onFavouriteToggle = { isFavourite = !isFavourite },
            )
        },
        containerColor = BackgroundLight,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            OfferDetailHeader(offer = offer)

            Spacer(modifier = Modifier.height(12.dp))

            OfferMetaChips(offer = offer)

            Spacer(modifier = Modifier.height(16.dp))

            ContentSection(title = "Sobre a empresa") {
                Text(
                    text = offer.aboutCompany,
                    fontSize = 14.sp,
                    color = DarkGrey,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            ContentSection(title = "Responsabilidades") {
                offer.responsibilities.forEach { item ->
                    ResponsibilityItem(text = item)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            ContentSectionColored(title = "Requisitos") {
                offer.requirements.forEach { item ->
                    CheckItem(text = item)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            ContentSection(title = "Benefícios") {
                FlowRow(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    offer.benefits.forEach { benefit ->
                        BenefitChip(text = benefit)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// endregion

// region Components

@Composable
private fun OfferDetailHeader(offer: OfferDetail) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(offer.logoColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = offer.logoInitial,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = offer.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
            )
            Text(
                text = offer.company,
                fontSize = 14.sp,
                color = DarkGrey,
            )
        }
    }
}

@Composable
private fun OfferMetaChips(offer: OfferDetail) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        MetaChip(
            icon = Icons.Outlined.LocationOn,
            label = "Localização",
            value = offer.location,
            modifier = Modifier.weight(1f).fillMaxHeight(),
        )
        MetaChip(
            icon = Icons.Outlined.Schedule,
            label = "Duração",
            value = offer.duration,
            modifier = Modifier.weight(1f).fillMaxHeight(),
        )
        MetaChip(
            icon = Icons.Outlined.Work,
            label = "Tipo",
            value = offer.type,
            modifier = Modifier.weight(1f).fillMaxHeight(),
        )
    }
}

@Composable
fun MetaChip(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = LightBlue,
                modifier = Modifier.size(20.dp),
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = DarkGrey,
                textAlign = TextAlign.Center,
            )
            Text(
                text = value,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = DarkBlue,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun ResponsibilityItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(BackgroundLight),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.TaskAlt,
                contentDescription = null,
                tint = LightBlue,
                modifier = Modifier.size(20.dp),
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = DarkGrey,
            lineHeight = 20.sp,
            modifier = Modifier
                .weight(1f)
                .padding(top = 8.dp),
        )
    }
}

@Composable
fun BenefitChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, LightBlue, RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp),
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            color = DarkBlue,
            fontWeight = FontWeight.Medium,
        )
    }
}

@Composable
private fun OfferDetailBottomBar(
    hasApplied: Boolean,
    isFavourite: Boolean,
    deadlineDays: Int,
    applicantsCount: Int,
    onApply: () -> Unit,
    onFavouriteToggle: () -> Unit,
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LinkStageButton(
                    text = if (hasApplied) "Candidatura Enviada ✓" else "Candidatar",
                    onClick = { if (!hasApplied) onApply() },
                    enabled = !hasApplied,
                    modifier = Modifier.weight(1f),
                    height = 50.dp,
                    brush = if (hasApplied) turmaA.grupoB.LinkStage.ui.theme.Fade3 else turmaA.grupoB.LinkStage.ui.theme.Fade2
                )

                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, BorderGrey, RoundedCornerShape(12.dp))
                        .background(Color.White),
                    contentAlignment = Alignment.Center,
                ) {
                    IconButton(onClick = onFavouriteToggle) {
                        Icon(
                            imageVector = if (isFavourite) Icons.Default.Favorite
                            else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (isFavourite) Red else DarkGrey,
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Candidaturas terminam em $deadlineDays dias  •  $applicantsCount alunos candidataram-se",
                    fontSize = 12.sp,
                    color = DarkGrey,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun ApplyConfirmDialog(
    offerTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    LinkStageDialog(
        title = "Confirmar Candidatura",
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        confirmText = "Candidatar",
        dismissText = "Cancelar",
        content = {
            Text(
                text = "Tens a certeza que te queres candidatar a \"$offerTitle\"?",
                color = DarkGrey,
                lineHeight = 22.sp,
            )
        }
    )
}

// endregion

// region Previews

@Preview(name = "Offer Detail", showSystemUi = true)
@Composable
private fun OfferDetailScreenPreview() {
    MaterialTheme {
        OfferDetailAlunoScreen(
            offerId = "2",
            onBack = {},
        )
    }
}

// endregion
