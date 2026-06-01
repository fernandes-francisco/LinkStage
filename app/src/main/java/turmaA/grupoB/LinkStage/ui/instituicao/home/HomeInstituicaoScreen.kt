package turmaA.grupoB.LinkStage.ui.instituicao.home

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.People
import androidx.compose.material.icons.outlined.PersonOff
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import turmaA.grupoB.LinkStage.ui.aluno.chat.Conversation
import turmaA.grupoB.LinkStage.ui.aluno.chat.avatarColors
import turmaA.grupoB.LinkStage.ui.aluno.chat.sampleConversations
import turmaA.grupoB.LinkStage.ui.aluno.offers.OfferItem
import turmaA.grupoB.LinkStage.ui.common.LinkStageLogo
import turmaA.grupoB.LinkStage.ui.instituicao.InstituicaoRoutes
import turmaA.grupoB.LinkStage.ui.theme.BackgroundLight
import turmaA.grupoB.LinkStage.ui.theme.BorderGrey
import turmaA.grupoB.LinkStage.ui.theme.DarkBlue
import turmaA.grupoB.LinkStage.ui.theme.DarkGrey
import turmaA.grupoB.LinkStage.ui.theme.LightBlue
import turmaA.grupoB.LinkStage.ui.theme.Red

private val sampleInstitutionOffers = listOf(
    OfferItem("1", "Designer de Produto", "ESTG-IPVC", "Tempo Inteiro", "5h atras", Color(0xFF1565C0), "E", duration = "6 Meses", area = "Design", location = "Porto"),
    OfferItem("2", "Programador Full-Stack", "ESTG-IPVC", "Remoto", "2d atras", Color(0xFF1565C0), "E", duration = "9 Meses", area = "Tecnologia", location = "Remoto"),
)

@Composable
fun HomeInstituicaoScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val internships = turmaA.grupoB.LinkStage.ui.instituicao.sampleInstitutionInternships
    val activeOffersCount = 5
    val applicationsCount = 12
    val activeInternships = internships.count {
        it.status == turmaA.grupoB.LinkStage.ui.instituicao.InternshipStatus.IN_PROGRESS
    }
    val pendingEvaluations = internships.count {
        it.status == turmaA.grupoB.LinkStage.ui.instituicao.InternshipStatus.PENDING_REVIEW
    }
    val noMentorCount = internships.count {
        it.status == turmaA.grupoB.LinkStage.ui.instituicao.InternshipStatus.NO_MENTOR || !it.hasMentor
    }

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(InstituicaoRoutes.offerFormRoute("new")) },
                containerColor = LightBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp),
            ) {
                Icon(Icons.Default.Add, contentDescription = "Criar oferta")
            }
        },
        containerColor = BackgroundLight,
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                LinkStageLogo()
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Olá, ESTG-IPVC.",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DarkBlue,
                modifier = Modifier.padding(horizontal = 20.dp),
                maxLines = 2,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Section 1 — Resumo (stat cards 2x2)
            SectionTitle("Resumo")
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                StatCard(
                    icon = Icons.Outlined.Work,
                    label = "Ofertas ativas",
                    count = activeOffersCount,
                    color = LightBlue,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navController.navigate(InstituicaoRoutes.OFFERS) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true; restoreState = true
                        }
                    },
                )
                StatCard(
                    icon = Icons.Outlined.People,
                    label = "Candidaturas",
                    count = applicationsCount,
                    color = DarkBlue,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navController.navigate(InstituicaoRoutes.OFFERS) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true; restoreState = true
                        }
                    },
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                StatCard(
                    icon = Icons.Outlined.CalendarMonth,
                    label = "Estágios ativos",
                    count = activeInternships,
                    color = LightBlue,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navController.navigate(InstituicaoRoutes.ACTIVITY) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true; restoreState = true
                        }
                    },
                )
                StatCard(
                    icon = Icons.Outlined.RateReview,
                    label = "Por avaliar",
                    count = pendingEvaluations,
                    color = Red,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        navController.navigate(InstituicaoRoutes.ACTIVITY) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true; restoreState = true
                        }
                    },
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Section 2 — Ações pendentes
            SectionTitle("Ações pendentes")
            Spacer(modifier = Modifier.height(8.dp))

            if (applicationsCount > 0 || noMentorCount > 0) {
                if (applicationsCount > 0) {
                    ActionCard(
                        icon = Icons.Outlined.People,
                        iconBg = LightBlue.copy(alpha = 0.15f),
                        iconTint = LightBlue,
                        title = "$applicationsCount candidaturas aguardam decisão",
                        subtitle = "Reveja e tome uma decisão sobre as candidaturas recentes.",
                        onClick = {
                            navController.navigate(InstituicaoRoutes.OFFERS) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (noMentorCount > 0) {
                    ActionCard(
                        icon = Icons.Outlined.PersonOff,
                        iconBg = Red.copy(alpha = 0.12f),
                        iconTint = Red,
                        title = "$noMentorCount estágios necessitam de orientador",
                        subtitle = "Atribua um orientador para acompanhamento académico.",
                        onClick = {
                            navController.navigate(InstituicaoRoutes.ACTIVITY) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            } else {
                EmptyStateCard("Sem ações pendentes. Tudo em ordem.")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 3 — Ofertas Recentes
            SectionHeader(
                title = "Ofertas Recentes",
                onViewAll = {
                    navController.navigate(InstituicaoRoutes.OFFERS) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (sampleInstitutionOffers.isNotEmpty()) {
                sampleInstitutionOffers.take(2).forEach { offer ->
                    SimpleOfferCard(offer = offer)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            } else {
                EmptyStateCard("Ainda não foi criada nenhuma oferta.")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 4 — Mensagens Recentes
            SectionHeader(
                title = "Mensagens Recentes",
                onViewAll = {
                    navController.navigate(InstituicaoRoutes.MESSAGES) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (sampleConversations.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                ) {
                    sampleConversations.take(3).forEachIndexed { index, conversation ->
                        MessageRow(
                            conversation = conversation,
                            onClick = {
                                navController.navigate(InstituicaoRoutes.chatRoute(conversation.id))
                            },
                        )
                        if (index < sampleConversations.take(3).size - 1) {
                            HorizontalDivider(
                                color = BorderGrey,
                                modifier = Modifier.padding(horizontal = 16.dp),
                            )
                        }
                    }
                }
            } else {
                EmptyStateCard("Sem mensagens de momento.")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        color = DarkBlue,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        modifier = Modifier.padding(horizontal = 20.dp),
    )
}

@Composable
private fun SectionHeader(
    title: String,
    onViewAll: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = DarkBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
        )
        TextButton(onClick = onViewAll) {
            Text(
                text = "Ver todos →",
                color = LightBlue,
                fontSize = 13.sp,
            )
        }
    }
}

@Composable
private fun StatCard(
    icon: ImageVector,
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Card(
        modifier = modifier.clickable { onClick() },
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
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(22.dp),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "$count",
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                )
                Text(
                    text = label,
                    color = DarkGrey,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                )
            }
        }
    }
}

@Composable
private fun ActionCard(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() },
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
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp),
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = DarkBlue,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                )
                Text(
                    text = subtitle,
                    color = DarkGrey,
                    fontSize = 12.sp,
                )
            }
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = DarkGrey,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun SimpleOfferCard(offer: OfferItem) {
    var isFav by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(offer.logoColor),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = offer.logoInitial,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = offer.title,
                        color = DarkBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                    )
                    Text(
                        text = offer.company,
                        color = DarkGrey,
                        fontSize = 13.sp,
                    )
                }
                IconButton(onClick = { isFav = !isFav }) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorito",
                        tint = if (isFav) LightBlue else DarkGrey,
                    )
                }
            }

            HorizontalDivider(
                color = BorderGrey,
                modifier = Modifier.padding(horizontal = 12.dp),
            )

            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = DarkGrey,
                    modifier = Modifier.size(13.dp),
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Publicada ${offer.publishedAgo}",
                    color = DarkGrey,
                    fontSize = 12.sp,
                )
            }
        }
    }
}

@Composable
private fun MessageRow(
    conversation: Conversation,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(avatarColors[conversation.avatarColorIndex % avatarColors.size]),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = conversation.initials,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
        ) {
            Text(
                text = conversation.name,
                color = DarkBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            )
            Text(
                text = conversation.lastMessage,
                color = DarkGrey,
                fontSize = 13.sp,
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
            contentDescription = null,
            tint = DarkGrey,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
private fun EmptyStateCard(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Text(
            text = message,
            color = DarkGrey,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeInstituicaoScreenPreview() {
    MaterialTheme {
        HomeInstituicaoScreen(navController = rememberNavController())
    }
}

@Preview(showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun HomeInstituicaoScreenLandscapePreview() {
    MaterialTheme {
        HomeInstituicaoScreen(navController = rememberNavController())
    }
}
